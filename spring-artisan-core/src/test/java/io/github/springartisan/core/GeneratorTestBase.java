package io.github.springartisan.core;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.model.EntityField;
import io.github.springartisan.core.template.TemplateEngine;

public abstract class GeneratorTestBase {

    protected GeneratorConfig config = GeneratorConfig.builder()
            .packageBase("com.test.app")
            .author("Test Author")
            .apiPrefix("/api/v1")
            .includeLombok(true)
            .includeValidation(true)
            .language("java")
            .build();

    protected TemplateEngine templateEngine = new TemplateEngine(config);

    protected EntityDefinition entity(String name) {
        EntityDefinition e = new EntityDefinition(name, config.getModelPackage());
        e.setAuthor(config.getAuthor());
        e.setUseLombok(config.isIncludeLombok());
        e.setAddValidation(config.isIncludeValidation());
        e.addField(EntityField.fromString("id:uuid"));
        e.addField(EntityField.fromString("name:string"));
        e.addField(EntityField.fromString("email:string"));
        return e;
    }
}
