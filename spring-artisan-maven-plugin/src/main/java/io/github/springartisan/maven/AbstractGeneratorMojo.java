package io.github.springartisan.maven;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.model.EntityField;
import io.github.springartisan.core.template.TemplateEngine;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    protected MavenProject project;

    /**
     * Entity name
     */
    @Parameter(property = "name", required = true)
    protected String name;

    /**
     * Fields: id:uuid,name:string,email:string
     */
    @Parameter(property = "fields")
    protected String fields;

    /**
     * Output directory
     */
    @Parameter(defaultValue = "src/main/java", property = "outputDir")
    protected String outputDir;

    /**
     * Target language: java or kotlin
     */
    @Parameter(defaultValue = "java", property = "language")
    protected String language;

    protected GeneratorConfig loadConfig() {
        return GeneratorConfig.builder()
                .packageBase("com.example.app")
                .author("Spring Artisan")
                .apiPrefix("/api/v1")
                .includeLombok(true)
                .includeValidation(true)
                .outputDir(outputDir)
                .language(language)
                .build();
    }

    protected EntityDefinition createEntityDefinition(GeneratorConfig config) {
        EntityDefinition entity = new EntityDefinition(
                name,
                config.getModelPackage()
        );
        entity.setAuthor(config.getAuthor());
        entity.setUseLombok(config.isIncludeLombok());
        entity.setAddValidation(config.isIncludeValidation());

        // Parse fields if provided
        if (fields != null && !fields.isEmpty()) {
            String[] fieldDefs = fields.split(",");
            for (String fieldDef : fieldDefs) {
                EntityField field = EntityField.fromString(fieldDef);
                entity.addField(field);
            }
        }

        return entity;
    }

    protected void writeFile(String content, String relativePath) throws IOException {
        Path filePath = Paths.get(project.getBasedir().getAbsolutePath(), relativePath);
        
        // Create directories if needed
        Files.createDirectories(filePath.getParent());
        
        // Write file
        Files.write(filePath, content.getBytes());
        
        getLog().info("Generated: " + filePath);
    }
}
