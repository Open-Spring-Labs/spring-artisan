package io.github.springartisan.cli.commands.subcommands;

import io.github.springartisan.core.generator.CodeGenerator;
import io.github.springartisan.core.generator.ControllerGenerator;
import io.github.springartisan.core.model.EntityDefinition;
import picocli.CommandLine;

@CommandLine.Command(
    name = "controller",
    description = "Generate REST controller class"
)
public class MakeControllerCommand extends BaseGeneratorCommand {

    @CommandLine.Option(names = {"--with-service"},
            defaultValue = "false",
            description = "Inject service dependency with full CRUD methods")
    private boolean withService;

    @CommandLine.Option(names = {"--paginated"},
            defaultValue = "false",
            description = "Use Page<T> instead of List<T> for collection endpoints")
    private boolean paginated;

    @CommandLine.Option(names = {"--secured"},
            defaultValue = "false",
            description = "Add @PreAuthorize security annotations")
    private boolean secured;

    @CommandLine.Option(names = {"--with-openapi"},
            defaultValue = "false",
            description = "Add OpenAPI/Swagger @Operation annotations")
    private boolean withOpenApi;

    @CommandLine.Option(names = {"--reactive"},
            defaultValue = "false",
            description = "Generate reactive controller using Mono/Flux")
    private boolean reactive;

    @Override
    protected CodeGenerator getGenerator() {
        return new ControllerGenerator(config, templateEngine);
    }

    @Override
    protected void configureEntity(EntityDefinition entity) {
        entity.setWithService(withService);
        entity.setPaginated(paginated);
        entity.setSecured(secured);
        entity.setWithOpenApi(withOpenApi);
        entity.setReactive(reactive);
    }
}
