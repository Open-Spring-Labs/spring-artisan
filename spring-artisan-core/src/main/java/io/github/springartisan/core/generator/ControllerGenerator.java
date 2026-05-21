package io.github.springartisan.core.generator;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;

public class ControllerGenerator extends BaseGenerator {
    public ControllerGenerator(GeneratorConfig config, TemplateEngine templateEngine) {
        super(config, templateEngine);
    }

    @Override
    public String getTemplateName() {
        return "controller.ftl";
    }

    @Override
    public String getOutputPath(EntityDefinition entity) {
        String packagePath = packageToPath(config.getControllerPackage());
        return packagePath + "/" + entity.getControllerName() + ".java";
    }
}
