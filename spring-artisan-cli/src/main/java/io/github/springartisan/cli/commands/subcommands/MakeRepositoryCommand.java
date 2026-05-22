package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.RepositoryGenerator;
import io.github.springartisan.core.model.EntityDefinition;
import picocli.CommandLine;

import java.util.Arrays;

@CommandLine.Command(
    name = "repository",
    description = "Generate Spring Data JPA repository interface"
)
public class MakeRepositoryCommand extends BaseGeneratorCommand {

    @CommandLine.Option(names = {"--find-by"},
            description = "Generate findBy methods (comma-separated fields): --find-by email,status")
    private String findBy;

    @CommandLine.Option(names = {"--paginated"},
            defaultValue = "false",
            description = "Add paginated findAll method")
    private boolean paginated;

    @Override
    protected CodeGenerator getGenerator() {
        return new RepositoryGenerator(config, templateEngine);
    }

    @Override
    protected void configureEntity(EntityDefinition entity) {
        entity.setPaginated(paginated);
        if (findBy != null && !findBy.isEmpty()) {
            Arrays.stream(findBy.split(",")).map(String::trim).forEach(entity.getFindByFields()::add);
        }
    }
}
