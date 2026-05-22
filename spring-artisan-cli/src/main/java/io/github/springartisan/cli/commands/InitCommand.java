package io.github.springartisan.cli.commands;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import picocli.CommandLine;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandLine.Command(
        name = "init",
        description = "Initialize Spring Artisan in your project",
        mixinStandardHelpOptions = true
)
public class InitCommand implements Callable<Integer> {

    private static final String CONFIG_FILE = "spring-artisan.yml";
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public Integer call() {
        File currentDir = new File(System.getProperty("user.dir"));
        File configFile = new File(currentDir, CONFIG_FILE);

        if (configFile.exists()) {
            System.out.println("spring-artisan.yml already exists.");
            System.out.print("Overwrite? (y/N): ");
            String answer = readLine();
            if (!answer.trim().equalsIgnoreCase("y")) {
                System.out.println("Aborted.");
                return 0;
            }
        }

        File pomXml = new File(currentDir, "pom.xml");
        File buildGradle = new File(currentDir, "build.gradle");
        File buildGradleKts = new File(currentDir, "build.gradle.kts");

        ProjectInfo info;
        if (pomXml.exists()) {
            System.out.println("Detected Maven project (pom.xml)");
            info = parseMaven(pomXml);
        } else if (buildGradle.exists() || buildGradleKts.exists()) {
            System.out.println("Detected Gradle project");
            File gradleFile = buildGradle.exists() ? buildGradle : buildGradleKts;
            info = parseGradle(gradleFile, currentDir);
        } else {
            System.out.println("");
            System.out.println("No project file found (pom.xml or build.gradle).");
            System.out.println("Please run 'spring-artisan init' from your project root,");
            System.out.println("or create spring-artisan.yml manually:");
            printManualInstructions();
            return 1;
        }

        // Prompt for additional info
        System.out.println("");
        System.out.println("Detected package base: " + info.packageBase);
        System.out.println("");

        System.out.print("Author name [" + System.getProperty("user.name") + "]: ");
        String author = readLine().trim();
        if (author.isEmpty()) author = System.getProperty("user.name");

        System.out.print("API prefix [/api/v1]: ");
        String apiPrefix = readLine().trim();
        if (apiPrefix.isEmpty()) apiPrefix = "/api/v1";

        System.out.print("Language (java/kotlin) [java]: ");
        String language = readLine().trim();
        if (language.isEmpty() || (!language.equals("java") && !language.equals("kotlin"))) {
            language = "java";
        }

        // Write config
        try {
            String yaml = buildYaml(info.packageBase, author, apiPrefix, language);
            Files.write(configFile.toPath(), yaml.getBytes());
            System.out.println("");
            System.out.println("Created spring-artisan.yml successfully!");
            System.out.println("");
            System.out.println("Next steps:");
            System.out.println("  spring-artisan make model User --fields \"id:uuid,name:string,email:string\"");
            System.out.println("  spring-artisan make resource Order --fields \"id:uuid,amount:double\" --with-tests");
        } catch (IOException e) {
            System.err.println("Failed to write spring-artisan.yml: " + e.getMessage());
            return 1;
        }

        return 0;
    }

    private ProjectInfo parseMaven(File pomXml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Suppress XML parser warnings/errors from printing to stderr
            builder.setErrorHandler(null);
            Document doc = builder.parse(pomXml);
            doc.getDocumentElement().normalize();

            // Get groupId directly from <project>, not from <parent>
            String groupId = getDirectChildValue(doc, "project", "groupId");

            // If not defined directly, inherit from <parent>
            if (groupId == null || groupId.isEmpty()) {
                groupId = getNestedValue(doc, "parent", "groupId");
            }

            String artifactId = getDirectChildValue(doc, "project", "artifactId");
            return new ProjectInfo(buildPackageBase(groupId, artifactId));
        } catch (Exception e) {
            System.out.println("[WARN] Could not parse pom.xml: " + e.getMessage());
            return new ProjectInfo("com.example.app");
        }
    }

    private String getDirectChildValue(Document doc, String parentTag, String childTag) {
        NodeList parents = doc.getElementsByTagName(parentTag);
        if (parents.getLength() == 0) return null;
        NodeList children = parents.item(0).getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (childTag.equals(child.getNodeName())) {
                return child.getTextContent().trim();
            }
        }
        return null;
    }

    private String getNestedValue(Document doc, String parentTag, String childTag) {
        NodeList parents = doc.getElementsByTagName(parentTag);
        if (parents.getLength() == 0) return null;
        NodeList children = parents.item(0).getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (childTag.equals(child.getNodeName())) {
                return child.getTextContent().trim();
            }
        }
        return null;
    }

    private ProjectInfo parseGradle(File gradleFile, File currentDir) {
        try {
            String content = new String(Files.readAllBytes(gradleFile.toPath()));
            String group = extractGradleValue(content, "group");
            String projectName = extractProjectName(currentDir);
            if (projectName == null) projectName = currentDir.getName();
            return new ProjectInfo(buildPackageBase(group, projectName));
        } catch (Exception e) {
            System.out.println("[WARN] Could not parse build.gradle: " + e.getMessage());
            return new ProjectInfo("com.example.app");
        }
    }

    private String extractGradleValue(String content, String key) {
        Pattern p = Pattern.compile(key + "\\s*=\\s*['\"]([^'\"]+)['\"]");
        Matcher m = p.matcher(content);
        return m.find() ? m.group(1) : null;
    }

    private String extractProjectName(File currentDir) {
        for (String name : new String[]{"settings.gradle", "settings.gradle.kts"}) {
            File settings = new File(currentDir, name);
            if (settings.exists()) {
                try {
                    String content = new String(Files.readAllBytes(settings.toPath()));
                    String val = extractGradleValue(content, "rootProject\\.name");
                    if (val != null) return val;
                } catch (IOException ignored) {}
            }
        }
        return null;
    }

    private String buildPackageBase(String group, String artifact) {
        boolean hasGroup = group != null && !group.isEmpty();
        boolean hasArtifact = artifact != null && !artifact.isEmpty();

        if (!hasGroup && !hasArtifact) return "com.example.app";

        String cleanArtifact = hasArtifact
                ? artifact.toLowerCase().replaceAll("[^a-z0-9]", "")
                : "";

        if (!hasGroup) return cleanArtifact;
        if (!hasArtifact) return group;

        return group + "." + cleanArtifact;
    }

    private String buildYaml(String packageBase, String author, String apiPrefix, String language) {
        return "spring-artisan:\n"
                + "  package-base: " + packageBase + "\n"
                + "  author: " + author + "\n"
                + "  api-prefix: " + apiPrefix + "\n"
                + "  output-dir: src/main/java\n"
                + "  test-output-dir: src/test/java\n"
                + "  language: " + language + "\n"
                + "  generators:\n"
                + "    include-lombok: true\n"
                + "    include-validation: true\n"
                + "    test-framework: junit5\n";
    }

    private void printManualInstructions() {
        System.out.println("");
        System.out.println("  Create spring-artisan.yml in your project root with:");
        System.out.println("");
        System.out.println("  spring-artisan:");
        System.out.println("    package-base: com.yourcompany.yourapp");
        System.out.println("    author: Your Name");
        System.out.println("    api-prefix: /api/v1");
        System.out.println("    language: java");
        System.out.println("    generators:");
        System.out.println("      include-lombok: true");
        System.out.println("      include-validation: true");
    }

    private String readLine() {
        try {
            String line = reader.readLine();
            return line != null ? line : "";
        } catch (IOException e) {
            return "";
        }
    }

    private static class ProjectInfo {
        final String packageBase;

        ProjectInfo(String packageBase) {
            this.packageBase = packageBase;
        }
    }
}
