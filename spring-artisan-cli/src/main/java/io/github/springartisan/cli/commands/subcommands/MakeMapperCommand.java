package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.MapperGenerator;
import picocli.CommandLine;

@CommandLine.Command(
    name = "mapper",
    description = "Generate MapStruct mapper interface"
)
public class MakeMapperCommand extends BaseGeneratorCommand {

    @Override
    protected CodeGenerator getGenerator() {
        return new MapperGenerator(config, templateEngine);
    }
}
