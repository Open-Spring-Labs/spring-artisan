package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.config.ConfigLoader;
import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.generator.AuthScaffoldGenerator;
import io.github.springartisan.core.generator.AuthScaffoldGenerator.AuthFile;
import io.github.springartisan.core.generator.ModelGenerator;
import io.github.springartisan.core.generator.RepositoryGenerator;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.model.EntityField;
import io.github.springartisan.core.template.TemplateEngine;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "auth",
    description = "Scaffold JWT authentication (SecurityConfig, JwtUtil, AuthController, AuthService, DTOs)",
    mixinStandardHelpOptions = true
)
public class MakeAuthCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"--entity"}, defaultValue = "User",
            description = "Name of the user entity (default: User)")
    private String entity;

    @CommandLine.Option(names = {"--username-field"}, defaultValue = "email",
            description = "Field used as the login username (default: email)")
    private String usernameField;

    @CommandLine.Option(names = {"--dry-run"}, defaultValue = "false",
            description = "Preview files without writing them")
    private boolean dryRun;

    @Override
    public Integer call() throws Exception {
        try {
            GeneratorConfig config = ConfigLoader.load();
            TemplateEngine templateEngine = new TemplateEngine(config);

            checkAndOfferEntityGeneration(config, templateEngine);

            AuthScaffoldGenerator scaffold = new AuthScaffoldGenerator(config);
            List<AuthFile> files = scaffold.generateAll(entity, usernameField);

            for (AuthFile file : files) {
                Path dest = Paths.get(config.getEffectiveOutputDir(), file.outputPath());
                if (dryRun) {
                    System.out.println("[DRY RUN] Would generate: " + dest);
                } else {
                    Files.createDirectories(dest.getParent());
                    Files.write(dest, file.content().getBytes());
                    System.out.println("✓ Generated: " + dest);
                }
            }

            printDependencyReminder();
            return 0;

        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            return 1;
        }
    }

    private void checkAndOfferEntityGeneration(GeneratorConfig config, TemplateEngine templateEngine)
            throws Exception {
        String ext = config.isKotlin() ? ".kt" : ".java";
        String entityPath = config.getPackageBase().replace(".", "/") + "/model/" + entity + ext;
        Path entityFile = Paths.get(config.getEffectiveOutputDir(), entityPath);

        if (Files.exists(entityFile)) {
            return;
        }

        System.out.println("[WARN] No " + entity + " entity found at: " + entityFile);
        System.out.print("Generate " + entity + " entity with email + password fields? [Y/n]: ");

        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine().trim();

        if (answer.isEmpty() || answer.equalsIgnoreCase("y")) {
            EntityDefinition userEntity = new EntityDefinition(entity, config.getModelPackage());
            userEntity.setAuthor(config.getAuthor());
            userEntity.setUseLombok(config.isIncludeLombok());
            userEntity.setAddValidation(config.isIncludeValidation());
            userEntity.addField(EntityField.fromString("email:string"));
            userEntity.addField(EntityField.fromString("password:string"));
            userEntity.addField(EntityField.fromString("name:string"));
            userEntity.getFindByFields().add(usernameField);

            writeFile(new ModelGenerator(config, templateEngine), userEntity, config);
            writeFile(new RepositoryGenerator(config, templateEngine), userEntity, config);
        } else {
            System.out.println("[INFO] Skipping entity generation. Create " + entity
                    + " manually with at least '" + usernameField + "' and 'password' fields.");
        }
    }

    private void writeFile(io.github.springartisan.core.generator.CodeGenerator gen,
                           EntityDefinition entityDef, GeneratorConfig config) throws Exception {
        String code = gen.generate(entityDef);
        Path dest = Paths.get(config.getEffectiveOutputDir(), gen.getOutputPath(entityDef));
        if (dryRun) {
            System.out.println("[DRY RUN] Would generate: " + dest);
        } else {
            Files.createDirectories(dest.getParent());
            Files.write(dest, code.getBytes());
            System.out.println("✓ Generated: " + dest);
        }
    }

    private void printDependencyReminder() {
        System.out.println();
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println("Add these dependencies to your pom.xml if not present:");
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println("""
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-security</artifactId>
                </dependency>
                <dependency>
                    <groupId>io.jsonwebtoken</groupId>
                    <artifactId>jjwt-api</artifactId>
                    <version>0.11.5</version>
                </dependency>
                <dependency>
                    <groupId>io.jsonwebtoken</groupId>
                    <artifactId>jjwt-impl</artifactId>
                    <version>0.11.5</version>
                    <scope>runtime</scope>
                </dependency>
                <dependency>
                    <groupId>io.jsonwebtoken</groupId>
                    <artifactId>jjwt-jackson</artifactId>
                    <version>0.11.5</version>
                    <scope>runtime</scope>
                </dependency>""");
        System.out.println();
        System.out.println("Add to application.properties:");
        System.out.println("  jwt.secret=your-256-bit-secret-key-change-in-production");
        System.out.println("  jwt.expiration=86400000");
    }
}
