package io.github.springartisan.cli.commands;

import io.github.springartisan.core.config.ConfigLoader;
import io.github.springartisan.core.config.GeneratorConfig;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "audit",
    description = "Check generated files for consistency (missing layers, mismatched names)",
    mixinStandardHelpOptions = true
)
public class AuditCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        GeneratorConfig config = ConfigLoader.load();

        String srcDir = config.getEffectiveOutputDir();
        String base = config.getPackageBase().replace(".", File.separator);

        Map<String, Set<String>> entities = new TreeMap<>();
        String[] layers = {"model", "service", "repository", "controller", "dto"};

        for (String layer : layers) {
            File dir = Paths.get(srcDir, base, layer).toFile();
            if (!dir.exists()) continue;
            File[] files = dir.listFiles((d, name) -> name.endsWith(".java") || name.endsWith(".kt"));
            if (files == null) continue;
            for (File f : files) {
                String name = f.getName().replaceAll("\\.(java|kt)$", "")
                        .replaceAll("(Service|Controller|Repository|DTO|Mapper|Resolver)$", "");
                entities.computeIfAbsent(name, k -> new TreeSet<>()).add(layer);
            }
        }

        if (entities.isEmpty()) {
            System.out.println("No generated files found. Nothing to audit.");
            return 0;
        }

        boolean allGood = true;
        System.out.println("Audit Results:");
        System.out.println("-".repeat(60));

        for (Map.Entry<String, Set<String>> entry : entities.entrySet()) {
            String entity = entry.getKey();
            Set<String> present = entry.getValue();
            List<String> warnings = new ArrayList<>();

            if (!present.contains("model"))
                warnings.add("missing model");
            if (present.contains("controller") && !present.contains("service"))
                warnings.add("controller exists but no service");
            if (present.contains("service") && !present.contains("repository"))
                warnings.add("service exists but no repository");
            if (present.contains("controller") && !present.contains("dto"))
                warnings.add("controller exists but no DTO");

            if (warnings.isEmpty()) {
                System.out.printf("  ✓ %-20s OK%n", entity);
            } else {
                allGood = false;
                System.out.printf("  ✗ %-20s %s%n", entity, String.join(", ", warnings));
            }
        }

        System.out.println("-".repeat(60));
        if (allGood) {
            System.out.println("All entities look consistent.");
        } else {
            System.out.println("Issues found. Run the missing generators to fix them.");
        }

        return allGood ? 0 : 1;
    }
}
