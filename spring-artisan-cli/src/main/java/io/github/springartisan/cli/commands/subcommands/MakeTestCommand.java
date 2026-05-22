package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.IntegrationTestGenerator;
import io.github.springartisan.core.generator.TestGenerator;
import io.github.springartisan.core.model.EntityDefinition;
import picocli.CommandLine;

@CommandLine.Command(
    name = "test",
    description = "Generate JUnit test class"
)
public class MakeTestCommand extends BaseGeneratorCommand {

    @CommandLine.Option(names = {"--integration"},
            defaultValue = "false",
            description = "Generate @DataJpaTest integration test instead of unit test")
    private boolean integration;

    @Override
    protected CodeGenerator getGenerator() {
        return integration
                ? new IntegrationTestGenerator(config, templateEngine)
                : new TestGenerator(config, templateEngine);
    }

    @Override
    protected void configureEntity(EntityDefinition entity) {
        entity.setIntegrationTest(integration);
    }
}
