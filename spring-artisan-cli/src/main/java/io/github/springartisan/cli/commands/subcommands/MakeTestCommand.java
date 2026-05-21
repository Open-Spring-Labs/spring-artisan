package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.TestGenerator;
import picocli.CommandLine;

@CommandLine.Command(
    name = "test",
    description = "Generate JUnit test class"
)
public class MakeTestCommand extends BaseGeneratorCommand {
    
    @Override
    protected CodeGenerator getGenerator() {
        return new TestGenerator(config, templateEngine);
    }
}
