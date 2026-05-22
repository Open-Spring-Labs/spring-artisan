# Spring Artisan

A powerful code generator for Spring Boot projects, inspired by Laravel Artisan. Scaffold models, services, controllers, repositories, DTOs, and tests with a single command.

## Features

- **Rapid Development** — Generate complete Spring Boot boilerplate in seconds
- **Best Practices** — Follows Spring conventions and clean architecture patterns
- **Customizable** — Configure via `spring-artisan.yml` in your project root
- **Kotlin Support** — Generate Java or Kotlin source files
- **Test Ready** — Auto-generates JUnit 5 test classes
- **Full Stack** — Generate model, service, repository, controller, DTO, and tests together

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

This checks GitHub for the latest release and updates automatically if a newer version is available.

---

## Quick Start

### 1. Initialize your project

Run this from your project root (where `pom.xml` or `build.gradle` lives):

```bash
spring-artisan init
```

Spring Artisan detects your project, reads the `groupId` and `artifactId` from your build file, and walks you through a short setup:

```
Detected Maven project (pom.xml)
Detected package base: com.yourcompany.yourapp

Author name [PC-username]: Mawuli Agbenyo
API prefix [/api/v1]:
Language (java/kotlin) [java]:

Created spring-artisan.yml successfully!
```

This creates a `spring-artisan.yml` in your project root. If no `pom.xml` or `build.gradle` is found, it prints instructions to create the file manually.

### 2. Generate code

```bash
spring-artisan make model User --fields "id:uuid,name:string,email:string"
```

### 3. That's it

Files are created under `src/main/java/com/yourcompany/yourapp/model/User.java`, each stamped with your name in the header.

---

## Commands

### Generate a single component

```bash
spring-artisan make model User --fields "id:uuid,name:string,email:string,age:integer"
spring-artisan make service User
spring-artisan make controller User
spring-artisan make repository User
spring-artisan make dto User
spring-artisan make test UserService
```

### Generate all components at once (recommended)

```bash
spring-artisan make resource User \
  --fields "id:uuid,name:string,email:string,age:integer" \
  --with-tests
```

This creates 6 files in one command:

```
src/main/java/com/yourcompany/yourapp/
├── model/UserRepository.java
├── service/UserService.java
├── repository/UserRepository.java
├── controller/UserController.java
└── dto/UserDTO.java

src/test/java/com/yourcompany/yourapp/
└── service/UserServiceTest.java
```

### Options

| Option | Short | Description |
|---|---|---|
| `--fields` | `-f` | Field definitions: `name:type,name:type` |
| `--with-tests` | `-w` | Also generate a test class |
| `--api-prefix` | `-a` | Override API prefix for this command |
| `--language` | `-l` | `java` (default) or `kotlin` |
| `--path` | `-p` | Custom output directory |

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

---

## Configuration

`spring-artisan init` generates this file automatically. You can also create or edit it by hand. All settings are optional — Spring Artisan uses sensible defaults for anything not specified.

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

## Kotlin Support

Pass `--language kotlin` to generate `.kt` files. Output is automatically written to `src/main/kotlin` and `src/test/kotlin`.

```bash
spring-artisan make resource Order \
  --fields "id:uuid,amount:double,status:string" \
  --language kotlin \
  --with-tests
```

Or set it permanently in `spring-artisan.yml`:

```yaml
spring-artisan:
  package-base: com.yourcompany.yourapp
  language: kotlin
```

---

## Generated Code Examples

All generated files include a header with your name:

```java
/**
 * Generated by Spring Artisan
 * Author: Your Name
 */
```

### Model (Entity)

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "name cannot be blank")
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    @NotBlank(message = "email cannot be blank")
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { ... }

    @PreUpdate
    protected void onUpdate() { ... }
}
```

### Service

```java
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository repository;

    @Transactional(readOnly = true)
    public List<User> findAll() { return repository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) { return repository.findById(id); }

    public User save(User entity) { return repository.save(entity); }

    public void deleteById(UUID id) { repository.deleteById(id); }
}
```

### Controller

```java
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<User>> getAll() { ... }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable UUID id) { ... }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User entity) { ... }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody User entity) { ... }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) { ... }
}
```

### Repository

```java
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Add custom queries here
}
```

---

## Maven Plugin (Alternative to CLI)

If you prefer to generate code via Maven goals instead of the CLI, add the plugin to your `pom.xml`:

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
mvn spring-artisan:model -Dname=User -Dfields="id:uuid,name:string,email:string"
mvn spring-artisan:service -Dname=User
mvn spring-artisan:controller -Dname=User
mvn spring-artisan:model -Dname=User -Dlanguage=kotlin
```

---

## Project Structure

```
spring-artisan/
├── spring-artisan-core/           # Core generation engine
│   ├── config/                    # Configuration loading (spring-artisan.yml)
│   ├── model/                     # Entity and field definitions
│   ├── generator/                 # Code generators (one per artifact type)
│   ├── template/                  # Freemarker integration
│   └── resources/templates/       # Java and Kotlin Freemarker templates
├── spring-artisan-cli/            # Standalone CLI (Picocli)
├── spring-artisan-maven-plugin/   # Maven plugin
├── install.sh                     # Mac/Linux installer
└── install.ps1                    # Windows installer
```

## Technologies

- **Java 17+**
- **Freemarker** — Template engine
- **Picocli** — CLI framework
- **Lombok** — Boilerplate reduction
- **SnakeYAML** — Configuration parsing
- **JUnit 5** — Test generation
- **Maven** — Build and plugin management

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Commit your changes (`git commit -m "Add my feature"`)
4. Push to the branch (`git push origin feature/my-feature`)
5. Open a Pull Request

---

## License

MIT License — see [LICENSE](LICENSE) file for details.

## Support

For issues, questions, or suggestions, please [open an issue](https://github.com/Open-Spring-Labs/spring-artisan/issues) on GitHub.

---

**Made with ❤️ for the Spring Boot community**
