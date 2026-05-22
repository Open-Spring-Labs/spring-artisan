package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.config.ConfigLoader;
import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.generator.*;
import io.github.springartisan.core.model.EntityDefinition;
import io.github.springartisan.core.model.EntityField;
import io.github.springartisan.core.template.TemplateEngine;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class BaseGeneratorCommand implements Callable<Integer> {
    
    protected GeneratorConfig config;
    protected TemplateEngine templateEngine;

    @CommandLine.Parameters(index = "0", arity = "1", description = "Entity name")
    protected String entityName;

    @CommandLine.Option(names = {"-f", "--fields"}, 
            description = "Fields: id:uuid,name:string,email:string")
    protected String fields;

    @CommandLine.Option(names = {"-p", "--path"}, 
            description = "Custom output directory")
    protected String outputPath;

    @CommandLine.Option(names = {"-w", "--with-tests"}, 
            defaultValue = "false",
            description = "Generate test class")
    protected boolean withTests;

    @CommandLine.Option(names = {"-a", "--api-prefix"},
            description = "Custom API prefix")
    protected String apiPrefix;

    @CommandLine.Option(names = {"-l", "--language"},
            description = "Target language: java or kotlin")
    protected String language;

    protected BaseGeneratorCommand() {
        this.config = ConfigLoader.load();
        this.templateEngine = new TemplateEngine(config);
    }

    protected EntityDefinition createEntityDefinition() {
        EntityDefinition entity = new EntityDefinition(
                entityName, 
                config.getModelPackage()
        );
        entity.setAuthor(config.getAuthor());
        entity.setUseLombok(config.isIncludeLombok());
        entity.setAddValidation(config.isIncludeValidation());

        // Parse fields
        if (fields != null && !fields.isEmpty()) {
            String[] fieldDefs = fields.split(",");
            for (String fieldDef : fieldDefs) {
                EntityField field = EntityField.fromString(fieldDef);
                entity.addField(field);
            }
        }

        configureEntity(entity);
        return entity;
    }

    protected void configureEntity(EntityDefinition entity) {
        // subclasses override to set flags like withService, withRepository
    }

    protected void writeGeneratedCode(String code, CodeGenerator generator, EntityDefinition entity)
            throws IOException {
        String outputDir = this.outputPath != null ?
                this.outputPath :
                (generator instanceof TestGenerator ? config.getEffectiveTestOutputDir() : config.getEffectiveOutputDir());

        Path outputPathFull = Paths.get(outputDir, generator.getOutputPath(entity));
        
        // Create directories if they don't exist
        Files.createDirectories(outputPathFull.getParent());
        
        // Write file
        Files.write(outputPathFull, code.getBytes());
        
        System.out.println("✓ Generated: " + outputPathFull);
    }

    protected abstract CodeGenerator getGenerator();

    @Override
    public Integer call() throws Exception {
        try {
            if (language != null) {
                config.setLanguage(language);
                templateEngine = new TemplateEngine(config);
            }
            EntityDefinition entity = createEntityDefinition();
            CodeGenerator generator = getGenerator();
            String code = generator.generate(entity);
            writeGeneratedCode(code, generator, entity);

            if (withTests && !(generator instanceof TestGenerator)) {
                TestGenerator testGen = new TestGenerator(config, templateEngine);
                String testCode = testGen.generate(entity);
                writeGeneratedCode(testCode, testGen, entity);
            }

            return 0;
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
