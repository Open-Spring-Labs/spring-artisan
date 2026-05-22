package io.github.springartisan.core.generator;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;

public class ResourceNotFoundExceptionGenerator extends BaseGenerator {
    public ResourceNotFoundExceptionGenerator(GeneratorConfig config, TemplateEngine templateEngine) {
        super(config, templateEngine);
    }

    @Override
    public String getTemplateName() {
        return "resource-not-found-exception.ftl";
    }

    @Override
    public String getOutputPath(EntityDefinition entity) {
        String packagePath = packageToPath(config.getPackageBase() + ".exception");
        return packagePath + "/ResourceNotFoundException" + fileExtension();
    }
}
