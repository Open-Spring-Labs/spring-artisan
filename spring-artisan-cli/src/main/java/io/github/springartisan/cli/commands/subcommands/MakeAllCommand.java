package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.config.ConfigLoader;
import io.github.springartisan.core.config.EntityYamlLoader;
import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.generator.*;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "all",
    description = "Generate all layers for every entity defined in the entities/ directory",
    mixinStandardHelpOptions = true
)
public class MakeAllCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"--with-tests"}, defaultValue = "false")
    private boolean withTests;

    @CommandLine.Option(names = {"--dry-run"}, defaultValue = "false")
    private boolean dryRun;

    @Override
    public Integer call() throws Exception {
        List<File> files = EntityYamlLoader.findAll();
        if (files.isEmpty()) {
            System.out.println("No entity definition files found in entities/ directory.");
            System.out.println("Create entities/User.yml to get started.");
            return 1;
        }

        GeneratorConfig config = ConfigLoader.load();
        TemplateEngine templateEngine = new TemplateEngine(config);

        for (File file : files) {
            System.out.println("\nProcessing: " + file.getName());
            EntityDefinition entity = EntityYamlLoader.parseFile(file, config);
            entity.setWithService(true);
            entity.setWithRepository(true);

            CodeGenerator[] generators = {
                new ModelGenerator(config, templateEngine),
                new RepositoryGenerator(config, templateEngine),
                new ServiceGenerator(config, templateEngine),
                new ControllerGenerator(config, templateEngine),
                new DTOGenerator(config, templateEngine)
            };

            for (CodeGenerator gen : generators) {
                write(gen, entity, config, false);
            }
            if (withTests) {
                write(new TestGenerator(config, templateEngine), entity, config, true);
            }
        }

        System.out.println("\n✓ Done. Generated " + files.size() + " entity/entities.");
        return 0;
    }

    private void write(CodeGenerator gen, EntityDefinition entity, GeneratorConfig config, boolean isTest)
            throws Exception {
        String code = gen.generate(entity);
        Path path = Paths.get(isTest ? config.getEffectiveTestOutputDir() : config.getEffectiveOutputDir(),
                gen.getOutputPath(entity));
        if (dryRun) {
            System.out.println("  [DRY RUN] Would generate: " + path);
        } else {
            Files.createDirectories(path.getParent());
            Files.write(path, code.getBytes());
            System.out.println("  ✓ " + path);
        }
    }
}
