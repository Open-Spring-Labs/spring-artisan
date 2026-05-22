package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.MigrationGenerator;
import io.github.springartisan.core.generator.ModelGenerator;
import io.github.springartisan.core.model.EntityDefinition;
import picocli.CommandLine;

import java.util.Arrays;

@CommandLine.Command(
    name = "model",
    description = "Generate JPA entity/model class"
)
public class MakeModelCommand extends BaseGeneratorCommand {

    @CommandLine.Option(names = {"--with-migration"},
            defaultValue = "false",
            description = "Also generate a Flyway migration SQL file")
    private boolean withMigration;

    @CommandLine.Option(names = {"--belongs-to"},
            description = "ManyToOne relationships (comma-separated): --belongs-to User,Category")
    private String belongsTo;

    @CommandLine.Option(names = {"--has-many"},
            description = "OneToMany relationships (comma-separated): --has-many Order,Comment")
    private String hasMany;

    @Override
    protected CodeGenerator getGenerator() {
        return new ModelGenerator(config, templateEngine);
    }

    @Override
    protected void configureEntity(EntityDefinition entity) {
        entity.setWithMigration(withMigration);
        if (belongsTo != null && !belongsTo.isEmpty()) {
            Arrays.stream(belongsTo.split(",")).map(String::trim).forEach(entity.getBelongsTo()::add);
        }
        if (hasMany != null && !hasMany.isEmpty()) {
            Arrays.stream(hasMany.split(",")).map(String::trim).forEach(entity.getHasMany()::add);
        }
    }

    @Override
    public Integer call() throws Exception {
        try {
            if (language != null) {
                config.setLanguage(language);
                templateEngine = new io.github.springartisan.core.template.TemplateEngine(config);
            }
            EntityDefinition entity = createEntityDefinition();

            writeGeneratedCode(new ModelGenerator(config, templateEngine).generate(entity),
                    new ModelGenerator(config, templateEngine), entity);

            if (withMigration) {
                MigrationGenerator migGen = new MigrationGenerator(config, templateEngine);
                writeGeneratedCode(migGen.generate(entity), migGen, entity);
            }
            return 0;
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
