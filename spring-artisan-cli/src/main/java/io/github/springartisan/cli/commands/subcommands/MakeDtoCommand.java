package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.DTOGenerator;
import picocli.CommandLine;

@CommandLine.Command(
    name = "dto",
    description = "Generate Data Transfer Object class"
)
public class MakeDtoCommand extends BaseGeneratorCommand {
    
    @Override
    protected CodeGenerator getGenerator() {
        return new DTOGenerator(config, templateEngine);
    }
}
