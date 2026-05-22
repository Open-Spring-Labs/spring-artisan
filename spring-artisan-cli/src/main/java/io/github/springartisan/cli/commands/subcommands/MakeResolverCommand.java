package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.ResolverGenerator;
import io.github.springartisan.core.model.EntityDefinition;
import picocli.CommandLine;

@CommandLine.Command(
    name = "resolver",
    description = "Generate GraphQL resolver"
)
public class MakeResolverCommand extends BaseGeneratorCommand {

    @Override
    protected CodeGenerator getGenerator() {
        return new ResolverGenerator(config, templateEngine);
    }

    @Override
    protected void configureEntity(EntityDefinition entity) {
        entity.setWithService(true);
    }
}
