package io.github.springartisan.core.generator;

import io.github.springartisan.core.model.EntityDefinition;
import freemarker.template.TemplateException;
import java.io.IOException;

public interface CodeGenerator {
    /**
     * Generate code for the given entity
     * @param entity The entity to generate code for
     * @return Generated Java code as string
     * @throws IOException If template loading fails
     * @throws TemplateException If template processing fails
     */
    String generate(EntityDefinition entity) throws IOException, TemplateException;

    /**
     * Get the template name used by this generator
     * @return Template filename
     */
    String getTemplateName();

    /**
     * Get the output file path relative to source directory
     * @param entity The entity
     * @return Relative path like "com/example/User.java"
     */
    String getOutputPath(EntityDefinition entity);
}
