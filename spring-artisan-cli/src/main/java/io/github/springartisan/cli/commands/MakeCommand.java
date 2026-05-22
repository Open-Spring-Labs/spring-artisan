package io.github.springartisan.cli.commands;

import io.github.springartisan.cli.commands.subcommands.*;
import picocli.CommandLine;

@CommandLine.Command(
    name = "make",
    description = "Generate code",
    subcommands = {
        MakeModelCommand.class,
        MakeControllerCommand.class,
        MakeServiceCommand.class,
        MakeRepositoryCommand.class,
        MakeDtoCommand.class,
        MakeTestCommand.class,
        MakeMapperCommand.class,
        MakeResolverCommand.class,
        MakeExceptionHandlerCommand.class,
        MakeEntityCommand.class,
        MakeAllCommand.class,
        MakeResourceCommand.class
    },
    mixinStandardHelpOptions = true
)
public class MakeCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Specify what to make:");
        System.out.println("  spring-artisan make model <name>              JPA entity");
        System.out.println("  spring-artisan make service <name>            Service class");
        System.out.println("  spring-artisan make controller <name>         REST controller");
        System.out.println("  spring-artisan make repository <name>         JPA repository");
        System.out.println("  spring-artisan make dto <name>                DTO class");
        System.out.println("  spring-artisan make mapper <name>             MapStruct mapper");
        System.out.println("  spring-artisan make resolver <name>           GraphQL resolver");
        System.out.println("  spring-artisan make test <name>               Unit / integration test");
        System.out.println("  spring-artisan make exception-handler         Global exception handler");
        System.out.println("  spring-artisan make entity <name>             All layers from entities/<Name>.yml");
        System.out.println("  spring-artisan make all                       All layers for every entities/*.yml");
        System.out.println("  spring-artisan make resource <name>           All 6 layers in one command");
        System.out.println("\nUse 'spring-artisan make <command> --help' for options");
    }
}
