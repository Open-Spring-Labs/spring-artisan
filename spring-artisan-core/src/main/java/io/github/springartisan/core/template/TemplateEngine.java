package io.github.springartisan.core.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.model.EntityDefinition;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class TemplateEngine {
    private final Configuration configuration;
    private final GeneratorConfig config;

    public TemplateEngine(GeneratorConfig config) {
        this.config = config;
        this.configuration = new Configuration(Configuration.VERSION_2_3_32);
        initializeConfiguration();
    }

    private void initializeConfiguration() {
        try {
            // Load templates from classpath
            configuration.setClassForTemplateLoading(
                    TemplateEngine.class, 
                    "/templates"
            );
            configuration.setDefaultEncoding("UTF-8");
            configuration.setNumberFormat("0.##");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Freemarker configuration", e);
        }
    }

    public String render(String templateName, EntityDefinition entity) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templateName);
        
        Map<String, Object> dataModel = buildDataModel(entity);
        
        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);
        
        return writer.toString();
    }

    private Map<String, Object> buildDataModel(EntityDefinition entity) {
        Map<String, Object> model = new HashMap<>();
        
        // Entity info
        model.put("entityName", entity.getName());
        model.put("entityNameLower", entity.getEntityNameLower());
        model.put("packageName", entity.getPackageName());
        model.put("tableName", entity.getTableName());
        model.put("author", entity.getAuthor() != null ? entity.getAuthor() : config.getAuthor());
        
        // Config options
        model.put("useLombok", entity.isUseLombok() && config.isIncludeLombok());
        model.put("addValidation", entity.isAddValidation() && config.isIncludeValidation());
        model.put("apiPrefix", config.getApiPrefix());
        model.put("withService", entity.isWithService());
        model.put("withRepository", entity.isWithRepository());
        model.put("paginated", entity.isPaginated());
        model.put("secured", entity.isSecured());
        model.put("withOpenApi", entity.isWithOpenApi());
        model.put("reactive", entity.isReactive());
        model.put("withMigration", entity.isWithMigration());
        model.put("integrationTest", entity.isIntegrationTest());
        model.put("findByFields", entity.getFindByFields());
        model.put("belongsTo", entity.getBelongsTo());
        model.put("hasMany", entity.getHasMany());
        
        // Service and DTO names
        model.put("serviceName", entity.getServiceName());
        model.put("controllerName", entity.getControllerName());
        model.put("repositoryName", entity.getRepositoryName());
        model.put("dtoName", entity.getDtoName());
        
        // Fields and packages
        model.put("fields", entity.getFields());
        model.put("relationships", entity.getRelationships());
        model.put("imports", entity.getRequiredImports());
        
        model.put("isKotlin", config.isKotlin());

        model.put("servicePackage", config.getServicePackage());
        model.put("controllerPackage", config.getControllerPackage());
        model.put("repositoryPackage", config.getRepositoryPackage());
        model.put("dtoPackage", config.getDtoPackage());
        model.put("testPackage", config.getTestPackage());
        
        return model;
    }

    public void setCustomTemplateDir(String dir) {
        try {
            configuration.setDirectoryForTemplateLoading(new File(dir));
        } catch (IOException e) {
            throw new RuntimeException("Failed to set custom template directory", e);
        }
    }
}
