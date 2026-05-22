package io.github.springartisan.maven;

import io.github.springartisan.core.generator.AuthScaffoldGenerator;
import io.github.springartisan.core.generator.AuthScaffoldGenerator.AuthFile;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Scaffold JWT authentication boilerplate.
 * Usage: mvn spring-artisan:auth
 */
@Mojo(name = "auth")
public class AuthMojo extends AbstractGeneratorMojo {

    /**
     * Name of the user entity (default: User)
     */
    @Parameter(defaultValue = "User", property = "entity")
    private String entity;

    /**
     * Field used as the login username (default: email)
     */
    @Parameter(defaultValue = "email", property = "usernameField")
    private String usernameField;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            var config = loadConfig();
            AuthScaffoldGenerator scaffold = new AuthScaffoldGenerator(config);
            List<AuthFile> files = scaffold.generateAll(entity, usernameField);

            for (AuthFile file : files) {
                Path dest = Paths.get(project.getBasedir().getAbsolutePath(),
                        config.getEffectiveOutputDir(), file.outputPath());
                Files.createDirectories(dest.getParent());
                Files.write(dest, file.content().getBytes());
                getLog().info("Generated: " + dest);
            }

            getLog().info("Auth scaffold complete. Add spring-boot-starter-security and jjwt-* dependencies.");
        } catch (Exception e) {
            throw new MojoExecutionException("Auth scaffold failed: " + e.getMessage(), e);
        }
    }
}
