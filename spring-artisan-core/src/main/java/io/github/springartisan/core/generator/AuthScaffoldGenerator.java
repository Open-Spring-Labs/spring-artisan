package io.github.springartisan.core.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.github.springartisan.core.config.GeneratorConfig;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthScaffoldGenerator {

    public record AuthFile(String outputPath, String content) {}

    private final GeneratorConfig config;
    private final Configuration freemarker;

    public AuthScaffoldGenerator(GeneratorConfig config) {
        this.config = config;
        this.freemarker = new Configuration(Configuration.VERSION_2_3_32);
        freemarker.setClassForTemplateLoading(AuthScaffoldGenerator.class, "/templates");
        freemarker.setDefaultEncoding("UTF-8");
    }

    public List<AuthFile> generateAll(String userEntity, String usernameField)
            throws IOException, TemplateException {

        Map<String, Object> model = buildModel(userEntity, usernameField);
        String base = config.getPackageBase().replace(".", "/");
        String ext = config.isKotlin() ? ".kt" : ".java";

        List<AuthFile> files = new ArrayList<>();
        files.add(render("auth/security-config.ftl",   model, base + "/security/SecurityConfig" + ext));
        files.add(render("auth/jwt-util.ftl",           model, base + "/security/JwtUtil" + ext));
        files.add(render("auth/jwt-auth-filter.ftl",    model, base + "/security/JwtAuthFilter" + ext));
        files.add(render("auth/custom-user-details-service.ftl", model,
                         base + "/security/CustomUserDetailsService" + ext));
        files.add(render("auth/auth-controller.ftl",   model, base + "/controller/AuthController" + ext));
        files.add(render("auth/auth-service.ftl",      model, base + "/service/AuthService" + ext));
        files.add(render("auth/login-request.ftl",     model, base + "/dto/LoginRequest" + ext));
        files.add(render("auth/register-request.ftl",  model, base + "/dto/RegisterRequest" + ext));
        files.add(render("auth/auth-response.ftl",     model, base + "/dto/AuthResponse" + ext));
        return files;
    }

    private AuthFile render(String templateName, Map<String, Object> model, String outputPath)
            throws IOException, TemplateException {
        Template template = freemarker.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        template.process(model, writer);
        return new AuthFile(outputPath, writer.toString());
    }

    private Map<String, Object> buildModel(String userEntity, String usernameField) {
        Map<String, Object> model = new HashMap<>();
        model.put("packageBase", config.getPackageBase());
        model.put("author", config.getAuthor());
        model.put("userEntity", userEntity);
        model.put("usernameField", usernameField);
        return model;
    }
}
