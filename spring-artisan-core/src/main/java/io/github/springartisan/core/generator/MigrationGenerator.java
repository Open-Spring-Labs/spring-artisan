package io.github.springartisan.core.generator;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MigrationGenerator extends BaseGenerator {

    private static int sequenceCounter = 1;

    public MigrationGenerator(GeneratorConfig config, TemplateEngine templateEngine) {
        super(config, templateEngine);
    }

    @Override
    public String getTemplateName() {
        return "migration.sql.ftl";
    }

    @Override
    public String getOutputPath(EntityDefinition entity) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String tableName = entity.getTableName();
        return config.getResourcesDir() + "/db/migration/V" + timestamp + "__create_" + tableName + "_table.sql";
    }
}
