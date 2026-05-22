package io.github.springartisan.core.generator;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;

public class ResolverGenerator extends BaseGenerator {
    public ResolverGenerator(GeneratorConfig config, TemplateEngine templateEngine) {
        super(config, templateEngine);
    }

    @Override
    public String getTemplateName() {
        return "resolver" + templateSuffix();
    }

    @Override
    public String getOutputPath(EntityDefinition entity) {
        String packagePath = packageToPath(config.getPackageBase() + ".resolver");
        return packagePath + "/" + entity.getName() + "Resolver" + fileExtension();
    }
}
