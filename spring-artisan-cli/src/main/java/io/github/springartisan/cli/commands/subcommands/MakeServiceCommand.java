package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.ServiceGenerator;
import io.github.springartisan.core.model.EntityDefinition;
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

    @CommandLine.Option(names = {"--reactive"},
            defaultValue = "false",
            description = "Generate reactive service using Mono/Flux")
    private boolean reactive;

    @Override
    protected CodeGenerator getGenerator() {
        return new ServiceGenerator(config, templateEngine);
    }

    @Override
    protected void configureEntity(EntityDefinition entity) {
        entity.setWithRepository(withRepository);
        entity.setReactive(reactive);
    }
}
