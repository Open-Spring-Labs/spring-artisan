package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.RepositoryGenerator;
import picocli.CommandLine;

@CommandLine.Command(
    name = "repository",
    description = "Generate Spring Data JPA repository interface"
)
public class MakeRepositoryCommand extends BaseGeneratorCommand {
    
    @Override
    protected CodeGenerator getGenerator() {
        return new RepositoryGenerator(config, templateEngine);
    }
}
