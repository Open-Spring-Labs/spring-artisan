package io.github.springartisan.maven;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.generator.ModelGenerator;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;

@Mojo(name = "model", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ModelMojo extends AbstractGeneratorMojo {

    @Override
    public void execute() throws MojoExecutionException {
        try {
            GeneratorConfig config = loadConfig();
            TemplateEngine templateEngine = new TemplateEngine(config);
            EntityDefinition entity = createEntityDefinition(config);

            ModelGenerator generator = new ModelGenerator(config, templateEngine);
            String code = generator.generate(entity);

            String outputPath = "src/main/java/" + generator.getOutputPath(entity);
            writeFile(code, outputPath);

            getLog().info("✓ Generated model: " + name);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate model", e);
        }
    }
}
