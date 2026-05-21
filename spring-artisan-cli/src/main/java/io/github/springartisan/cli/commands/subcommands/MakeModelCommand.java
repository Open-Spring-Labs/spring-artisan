package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.ModelGenerator;
import picocli.CommandLine;

@CommandLine.Command(
    name = "model",
    description = "Generate JPA entity/model class"
)
public class MakeModelCommand extends BaseGeneratorCommand {
    
    @Override
    protected CodeGenerator getGenerator() {
        return new ModelGenerator(config, templateEngine);
    }
}
