# Spring Artisan

A powerful code generator for Spring Boot projects, inspired by Laravel Artisan. Scaffold models, services, controllers, repositories, DTOs, mappers, tests, and more with a single command.

## Features

- **Rapid Development** — Generate complete Spring Boot boilerplate in seconds
- **Best Practices** — Follows Spring conventions and clean architecture patterns
- **Flexible** — Generate individual layers or everything at once
- **Kotlin Support** — Generate Java or Kotlin source files
- **Entity YAML Definitions** — Define entities in YAML, generate all layers in one command
- **Relationships** — `@ManyToOne` and `@OneToMany` annotations generated automatically
- **Pagination** — `Page<T>` endpoints with a single flag
- **Reactive** — WebFlux `Mono`/`Flux` support
- **OpenAPI** — Swagger `@Operation` annotations on controllers
- **Security** — `@PreAuthorize` annotations on controller endpoints
- **Database Migrations** — Flyway SQL migration files alongside your entity
- **GraphQL** — Generate Spring GraphQL resolvers
- **MapStruct** — Generate mapper interfaces automatically
- **Global Error Handling** — `@RestControllerAdvice` exception handler in one command
- **Audit** — Check your project for missing or inconsistent layers
- **Dry Run** — Preview what would be generated before writing any files

---

## Installation

### Mac / Linux

```bash
curl -s https://raw.githubusercontent.com/Open-Spring-Labs/spring-artisan/master/install.sh | bash
```

### Windows (PowerShell)

```powershell
irm https://raw.githubusercontent.com/Open-Spring-Labs/spring-artisan/master/install.ps1 | iex
```

Restart your terminal after installation. The `spring-artisan` command is then available globally.

### Verify installation

```bash
spring-artisan --version
```

### Update to the latest version

```bash
spring-artisan update
```

---

## Quick Start

### 1. Initialize your project

Run from your project root (where `pom.xml` or `build.gradle` lives):

```bash
spring-artisan init
```

Spring Artisan reads your build file, detects the package, and walks you through a short setup:

```
Detected Maven project (pom.xml)
Detected package base: com.yourcompany.yourapp

Author name [PC-username]: Mawuli Agbenyo
API prefix [/api/v1]:
Language (java/kotlin) [java]:

Created spring-artisan.yml successfully!
```

### 2. Generate code

```bash
spring-artisan make model User --fields "id:uuid,name:string,email:string"
```

### 3. That's it

Files are created under `src/main/java/com/yourcompany/yourapp/model/User.java`, each stamped with your name in the header.

---

## Commands

### `init` — Initialize the project

```bash
spring-artisan init
```

Detects `pom.xml` or `build.gradle`, reads the project metadata, and creates `spring-artisan.yml`. If no build file is found, prints instructions to create the config manually.

---

### `make model` — JPA Entity

```bash
spring-artisan make model User --fields "id:uuid,name:string,email:string,age:integer"

# With a Flyway migration file
spring-artisan make model User --fields "id:uuid,name:string" --with-migration

# With relationships
spring-artisan make model Order --fields "id:uuid,amount:double" \
  --belongs-to User \
  --has-many Item
```

| Flag | Description |
|---|---|
| `--fields` | Field definitions: `name:type` comma-separated |
| `--with-migration` | Also generate a Flyway SQL migration file |
| `--belongs-to` | `@ManyToOne` relationships (comma-separated) |
| `--has-many` | `@OneToMany` relationships (comma-separated) |

---

### `make service` — Service Layer

```bash
# Bare service with stub methods
spring-artisan make service User

# Service wired to repository with full CRUD
spring-artisan make service User --with-repository

# Reactive service using Mono/Flux
spring-artisan make service User --with-repository --reactive
```

| Flag | Description |
|---|---|
| `--with-repository` | Inject repository, generate full CRUD methods |
| `--reactive` | Use `Mono`/`Flux` instead of `Optional`/`List` |

---

### `make controller` — REST Controller

```bash
# Bare controller with stub methods
spring-artisan make controller User

# Fully featured controller
spring-artisan make controller User \
  --with-service \
  --paginated \
  --secured \
  --with-openapi
```

| Flag | Description |
|---|---|
| `--with-service` | Inject service with full CRUD methods |
| `--paginated` | Use `Page<T>` instead of `List<T>` |
| `--secured` | Add `@PreAuthorize` on each endpoint |
| `--with-openapi` | Add `@Operation` and `@ApiResponse` annotations |
| `--reactive` | Use `Mono`/`Flux` return types |

---

### `make repository` — JPA Repository

```bash
spring-artisan make repository User

# With custom findBy methods
spring-artisan make repository User --find-by "email,status"

# With paginated findAll
spring-artisan make repository User --paginated
```

| Flag | Description |
|---|---|
| `--find-by` | Generate `findBy<Field>` methods (comma-separated) |
| `--paginated` | Add `findAll(Pageable)` method |

---

### `make dto` — Data Transfer Object

```bash
spring-artisan make dto User
```

---

### `make mapper` — MapStruct Mapper

```bash
spring-artisan make mapper User
```

Generates a MapStruct `UserMapper` interface with `toDTO()`, `toEntity()`, and `updateEntity()` methods.

> Requires `mapstruct` dependency in your `pom.xml`.

---

### `make resolver` — GraphQL Resolver

```bash
spring-artisan make resolver User
```

Generates a Spring GraphQL `@QueryMapping` / `@MutationMapping` resolver wired to `UserService`.

> Requires `spring-boot-starter-graphql` dependency.

---

### `make exception-handler` — Global Error Handler

```bash
spring-artisan make exception-handler
```

Generates two files in your `exception` package:
- `GlobalExceptionHandler` — handles `ResourceNotFoundException`, validation errors, and generic exceptions
- `ResourceNotFoundException` — standard 404 exception

---

### `make test` — Test Class

```bash
# Unit test (Mockito, default)
spring-artisan make test UserService

# Integration test (@DataJpaTest)
spring-artisan make test User --integration
```

| Flag | Description |
|---|---|
| `--integration` | Generate `@DataJpaTest` integration test instead of unit test |

---

### `make resource` — All Layers at Once

```bash
spring-artisan make resource User \
  --fields "id:uuid,name:string,email:string" \
  --with-tests
```

Generates all 6 files in one command:

```
src/main/java/com/yourcompany/yourapp/
├── model/User.java
├── service/UserService.java
├── repository/UserRepository.java
├── controller/UserController.java
└── dto/UserDTO.java

src/test/java/com/yourcompany/yourapp/
└── service/UserServiceTest.java
```

---

### `make entity` — Generate from YAML Definition

Define an entity in a YAML file, then generate all layers from it:

**1. Create `entities/User.yml` in your project root:**

```yaml
name: User
fields:
  - id: uuid
  - name: string
  - email: string:unique
  - age: integer
relationships:
  - belongs-to: Role
  - has-many: Order
```

**2. Generate:**

```bash
spring-artisan make entity User --with-tests
```

---

### `make all` — Generate from All YAML Definitions

```bash
spring-artisan make all
spring-artisan make all --with-tests --dry-run
```

Processes every file in the `entities/` directory and generates all layers for each.

---

### `list` — Show Generated Entities

```bash
spring-artisan list
```

```
Entity                model  service  repository  controller  dto  mapper  resolver
--------------------------------------------------------------------------------
Order                 ✓      ✓        ✓           ✓           ✓    -       -
Product               ✓      -        -            -           -    -       -
User                  ✓      ✓        ✓           ✓           ✓    ✓       -
```

---

### `audit` — Check for Missing Layers

```bash
spring-artisan audit
```

```
Audit Results:
------------------------------------------------------------
  ✓ Order                OK
  ✗ Product              service exists but no repository, controller exists but no DTO
  ✓ User                 OK
------------------------------------------------------------
Issues found. Run the missing generators to fix them.
```

---

## Global Flags

These flags work on every `make` command:

| Flag | Description |
|---|---|
| `--dry-run` | Preview files that would be generated without writing them |
| `--language` | `java` (default) or `kotlin` |
| `--with-tests` | Also generate a test class |
| `--api-prefix` | Override the API prefix for this command |
| `--path` | Custom output directory |

```bash
# Preview everything without writing
spring-artisan make resource User --fields "id:uuid,name:string" --dry-run

# Generate Kotlin files
spring-artisan make resource User --language kotlin
```

---

## Field Types

| Type | Java | Kotlin |
|---|---|---|
| `string` | `String` | `String` |
| `integer` | `Integer` | `Int` |
| `long` | `Long` | `Long` |
| `double` | `Double` | `Double` |
| `boolean` | `Boolean` | `Boolean` |
| `date` | `LocalDate` | `LocalDate` |
| `timestamp` | `LocalDateTime` | `LocalDateTime` |
| `uuid` | `UUID` | `UUID` |

Field modifiers (append with `:`):

```
id:uuid:unique       → unique constraint
email:string:unique  → unique constraint
name:string          → nullable by default
```

---

## Configuration

`spring-artisan init` generates this file automatically. All settings are optional.

```yaml
spring-artisan:
  package-base: com.yourcompany.yourapp   # base package for all generated classes
  author: Your Name                        # appears in every generated file header
  api-prefix: /api/v1                      # prefix for controller @RequestMapping
  output-dir: src/main/java               # main source output directory
  test-output-dir: src/test/java          # test source output directory
  language: java                           # java or kotlin
  generators:
    include-lombok: true                   # add @Data, @Builder, @RequiredArgsConstructor
    include-validation: true              # add @NotBlank and other validation annotations
    test-framework: junit5
```

---

## Generated Code Examples

All generated files include a header:

```java
/**
 * Generated by Spring Artisan
 * Author: Your Name
 */
```

### Model with relationships

```java
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();
}
```

### Paginated controller with OpenAPI and security

```java
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "User management API")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    @Operation(summary = "Get all users")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<User>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PostMapping
    @Operation(summary = "Create user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> create(@RequestBody User entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }
}
```

### MapStruct mapper

```java
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDTO toDTO(User entity);
    User toEntity(UserDTO dto);
    void updateEntity(UserDTO dto, @MappingTarget User entity);
}
```

### Global exception handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) { ... }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) { ... }
}
```

---

## Maven Plugin (Alternative to CLI)

Add to your project's `pom.xml`:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>io.github.springartisan</groupId>
      <artifactId>spring-artisan-maven-plugin</artifactId>
      <version>1.0.1</version>
    </plugin>
  </plugins>
</build>

<pluginRepositories>
  <pluginRepository>
    <id>github</id>
    <url>https://maven.pkg.github.com/Open-Spring-Labs/spring-artisan</url>
  </pluginRepository>
</pluginRepositories>
```

Add GitHub credentials to `~/.m2/settings.xml`:

```xml
<servers>
  <server>
    <id>github</id>
    <username>YOUR_GITHUB_USERNAME</username>
    <password>YOUR_GITHUB_TOKEN</password>
  </server>
</servers>
```

Then run:

```bash
mvn spring-artisan:model -Dname=User -Dfields="id:uuid,name:string"
mvn spring-artisan:service -Dname=User
mvn spring-artisan:controller -Dname=User
mvn spring-artisan:model -Dname=User -Dlanguage=kotlin
```

---

## Project Structure

```
spring-artisan/
├── spring-artisan-core/
│   ├── config/          # ConfigLoader, GeneratorConfig, EntityYamlLoader
│   ├── model/           # EntityDefinition, EntityField, FieldType
│   ├── generator/       # One generator class per artifact type
│   ├── template/        # Freemarker engine wrapper
│   └── resources/
│       └── templates/   # Java and Kotlin .ftl template files
├── spring-artisan-cli/
│   └── commands/        # Picocli command classes
├── spring-artisan-maven-plugin/
│   └── Mojos/           # Maven goal implementations
├── install.sh           # Mac/Linux installer
└── install.ps1          # Windows installer
```

## Technologies

- **Java 17+** / **Kotlin**
- **Freemarker** — Template engine
- **Picocli** — CLI framework
- **Lombok** — Boilerplate reduction
- **MapStruct** — Mapper generation
- **SnakeYAML** — Configuration and entity definition parsing
- **JUnit 5** — Test generation
- **Maven** — Build and plugin management

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

---

## License

MIT License — see [LICENSE](LICENSE) file for details.

## Support

For issues, questions, or suggestions, please [open an issue](https://github.com/Open-Spring-Labs/spring-artisan/issues) on GitHub.

---

**Made with ❤️ for the Spring Boot community**
