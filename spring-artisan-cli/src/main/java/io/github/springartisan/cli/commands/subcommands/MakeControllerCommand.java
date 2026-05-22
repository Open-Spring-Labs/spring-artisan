package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.ControllerGenerator;
import picocli.CommandLine;

@CommandLine.Command(
    name = "controller",
    description = "Generate REST controller class"
)
public class MakeControllerCommand extends BaseGeneratorCommand {

    @CommandLine.Option(names = {"--with-service"},
            defaultValue = "false",
            description = "Inject service dependency with full CRUD methods")
    private boolean withService;

    @Override
    protected CodeGenerator getGenerator() {
        return new ControllerGenerator(config, templateEngine);
    }

    @Override
    protected void configureEntity(io.github.springartisan.core.model.EntityDefinition entity) {
        entity.setWithService(withService);
    }
}
