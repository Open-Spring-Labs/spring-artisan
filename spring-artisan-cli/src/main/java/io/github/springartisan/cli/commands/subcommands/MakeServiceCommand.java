package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.ServiceGenerator;
import picocli.CommandLine;

@CommandLine.Command(
    name = "service",
    description = "Generate service layer class"
)
public class MakeServiceCommand extends BaseGeneratorCommand {

    @CommandLine.Option(names = {"--with-repository"},
            defaultValue = "false",
            description = "Inject repository dependency with full CRUD methods")
    private boolean withRepository;

    @Override
    protected CodeGenerator getGenerator() {
        return new ServiceGenerator(config, templateEngine);
    }

    @Override
    protected void configureEntity(io.github.springartisan.core.model.EntityDefinition entity) {
        entity.setWithRepository(withRepository);
    }
}
