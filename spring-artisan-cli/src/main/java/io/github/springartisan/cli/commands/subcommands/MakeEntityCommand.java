package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.config.ConfigLoader;
import io.github.springartisan.core.config.EntityYamlLoader;
import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.generator.*;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "entity",
    description = "Generate all layers from an entities/<Name>.yml definition file",
    mixinStandardHelpOptions = true
)
public class MakeEntityCommand implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "Entity name (must match entities/<Name>.yml)")
    private String entityName;

    @CommandLine.Option(names = {"--with-tests"}, defaultValue = "false")
    private boolean withTests;

    @CommandLine.Option(names = {"--dry-run"}, defaultValue = "false")
    private boolean dryRun;

    @Override
    public Integer call() throws Exception {
        try {
            GeneratorConfig config = ConfigLoader.load();
            TemplateEngine templateEngine = new TemplateEngine(config);
            EntityDefinition entity = EntityYamlLoader.load(entityName, config);
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
                write(gen, entity, config);
            }
            if (withTests) {
                write(new TestGenerator(config, templateEngine), entity, config);
            }

            System.out.println("\n✓ Generated all layers for " + entityName + " from entities/" + entityName + ".yml");
            return 0;
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            return 1;
        }
    }

    private void write(CodeGenerator gen, EntityDefinition entity, GeneratorConfig config) throws Exception {
        boolean isTest = gen instanceof TestGenerator;
        String code = gen.generate(entity);
        Path path = Paths.get(isTest ? config.getEffectiveTestOutputDir() : config.getEffectiveOutputDir(),
                gen.getOutputPath(entity));
        if (dryRun) {
            System.out.println("[DRY RUN] Would generate: " + path);
        } else {
            Files.createDirectories(path.getParent());
            Files.write(path, code.getBytes());
            System.out.println("✓ Generated: " + path);
        }
    }
}
