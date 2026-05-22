package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.*;
import io.github.springartisan.core.model.EntityDefinition;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "resource",
    description = "Generate complete resource (model, service, repository, controller, DTO, test)"
)
public class MakeResourceCommand extends BaseGeneratorCommand {
    
    @Override
    protected CodeGenerator getGenerator() {
        // This command generates all 6 components
        // We'll handle this in the call() method instead
        return null;
    }

    @Override
    public Integer call() throws Exception {
        try {
            EntityDefinition entity = createEntityDefinition();
            entity.setWithService(true);
            entity.setWithRepository(true);
            
            // Generate all 6 components
            CodeGenerator[] generators = {
                new ModelGenerator(config, templateEngine),
                new RepositoryGenerator(config, templateEngine),
                new ServiceGenerator(config, templateEngine),
                new ControllerGenerator(config, templateEngine),
                new DTOGenerator(config, templateEngine),
                new TestGenerator(config, templateEngine)
            };
            
            for (CodeGenerator generator : generators) {
                String code = generator.generate(entity);
                writeGeneratedCode(code, generator, entity);
            }
            
            System.out.println("\n✓ Complete resource generated for " + entityName);
            return 0;
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
