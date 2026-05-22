package io.github.springartisan.maven;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.generator.ServiceGenerator;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.template.TemplateEngine;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;

@Mojo(name = "service", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ServiceMojo extends AbstractGeneratorMojo {

    @Override
    public void execute() throws MojoExecutionException {
        try {
            GeneratorConfig config = loadConfig();
            TemplateEngine templateEngine = new TemplateEngine(config);
            EntityDefinition entity = createEntityDefinition(config);

            ServiceGenerator generator = new ServiceGenerator(config, templateEngine);
            String code = generator.generate(entity);

            String outputPath = config.getEffectiveOutputDir() + "/" + generator.getOutputPath(entity);
            writeFile(code, outputPath);

            getLog().info("✓ Generated service: " + name);
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate service", e);
        }
    }
}
