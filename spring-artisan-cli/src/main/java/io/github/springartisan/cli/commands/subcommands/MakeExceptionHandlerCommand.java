package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.config.ConfigLoader;
import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.generator.ExceptionHandlerGenerator;
import io.github.springartisan.core.generator.ResourceNotFoundExceptionGenerator;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "exception-handler",
    description = "Generate global exception handler and ResourceNotFoundException",
    mixinStandardHelpOptions = true
)
public class MakeExceptionHandlerCommand implements Callable<Integer> {

    @CommandLine.Option(names = {"--dry-run"}, defaultValue = "false",
            description = "Preview files without writing them")
    private boolean dryRun;

    @Override
    public Integer call() throws Exception {
        try {
            GeneratorConfig config = ConfigLoader.load();
            TemplateEngine templateEngine = new TemplateEngine(config);

            // Use a dummy entity just to satisfy the generator interface
            EntityDefinition dummy = new EntityDefinition("App", config.getModelPackage());
            dummy.setAuthor(config.getAuthor());

            writeFile(new ExceptionHandlerGenerator(config, templateEngine), dummy, config, templateEngine);
            writeFile(new ResourceNotFoundExceptionGenerator(config, templateEngine), dummy, config, templateEngine);

            return 0;
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            return 1;
        }
    }

    private void writeFile(io.github.springartisan.core.generator.CodeGenerator gen,
                           EntityDefinition entity, GeneratorConfig config,
                           TemplateEngine templateEngine) throws Exception {
        String code = gen.generate(entity);
        Path path = Paths.get(config.getEffectiveOutputDir(), gen.getOutputPath(entity));
        if (dryRun) {
            System.out.println("[DRY RUN] Would generate: " + path);
        } else {
            Files.createDirectories(path.getParent());
            Files.write(path, code.getBytes());
            System.out.println("✓ Generated: " + path);
        }
    }
}
