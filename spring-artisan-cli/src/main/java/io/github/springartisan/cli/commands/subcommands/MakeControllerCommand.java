package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.ControllerGenerator;
import picocli.CommandLine;

@CommandLine.Command(
    name = "controller",
    description = "Generate REST controller class"
)
public class MakeControllerCommand extends BaseGeneratorCommand {
    
    @Override
    protected CodeGenerator getGenerator() {
        return new ControllerGenerator(config, templateEngine);
    }
}
