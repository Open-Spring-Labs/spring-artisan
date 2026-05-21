package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.ServiceGenerator;
import picocli.CommandLine;

@CommandLine.Command(
    name = "service",
    description = "Generate service layer class"
)
public class MakeServiceCommand extends BaseGeneratorCommand {
    
    @Override
    protected CodeGenerator getGenerator() {
        return new ServiceGenerator(config, templateEngine);
    }
}
