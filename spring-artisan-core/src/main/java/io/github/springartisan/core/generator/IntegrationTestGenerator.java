package io.github.springartisan.core.generator;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;

public class IntegrationTestGenerator extends BaseGenerator {
    public IntegrationTestGenerator(GeneratorConfig config, TemplateEngine templateEngine) {
        super(config, templateEngine);
    }

    @Override
    public String getTemplateName() {
        return "integration-test" + templateSuffix();
    }

    @Override
    public String getOutputPath(EntityDefinition entity) {
        String packagePath = packageToPath(config.getTestPackage());
        return packagePath + "/" + entity.getName() + "IntegrationTest" + fileExtension();
    }
}
