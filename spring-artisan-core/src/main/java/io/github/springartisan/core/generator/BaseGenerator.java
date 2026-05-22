package io.github.springartisan.core.generator;

import freemarker.template.TemplateException;
import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;
import java.io.IOException;

public abstract class BaseGenerator implements CodeGenerator {
    protected final TemplateEngine templateEngine;
    protected final GeneratorConfig config;

    public BaseGenerator(GeneratorConfig config, TemplateEngine templateEngine) {
        this.config = config;
        this.templateEngine = templateEngine;
    }

    @Override
    public String generate(EntityDefinition entity) throws IOException, TemplateException {
        return templateEngine.render(getTemplateName(), entity);
    }

    @Override
    public abstract String getTemplateName();

    @Override
    public abstract String getOutputPath(EntityDefinition entity);

    protected String packageToPath(String packageName) {
        return packageName.replace(".", "/");
    }

    protected String templateSuffix() {
        return config.isKotlin() ? ".kt.ftl" : ".ftl";
    }

    protected String fileExtension() {
        return config.isKotlin() ? ".kt" : ".java";
    }
}
