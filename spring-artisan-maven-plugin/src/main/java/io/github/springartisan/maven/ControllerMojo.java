package io.github.springartisan.maven;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.generator.ControllerGenerator;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;

@Mojo(name = "controller", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ControllerMojo extends AbstractGeneratorMojo {

    @Override
    public void execute() throws MojoExecutionException {
        try {
            GeneratorConfig config = loadConfig();
            TemplateEngine templateEngine = new TemplateEngine(config);
            EntityDefinition entity = createEntityDefinition(config);

            ControllerGenerator generator = new ControllerGenerator(config, templateEngine);
            String code = generator.generate(entity);

            String outputPath = config.getEffectiveOutputDir() + "/" + generator.getOutputPath(entity);
            writeFile(code, outputPath);

            getLog().info("✓ Generated controller: " + name);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate controller", e);
        }
    }
}
