# Spring Artisan

A powerful code generator for Spring Boot projects, inspired by Laravel Artisan. Quickly scaffold models, services, controllers, repositories, DTOs, and tests with a single command.

## Features

✨ **Rapid Development**: Generate complete Spring Boot boilerplate in seconds
🎯 **Best Practices**: Follows Spring conventions and clean architecture patterns  
🛠️ **Customizable**: Support for Lombok, validation annotations, custom templates
🧪 **Test Ready**: Auto-generates JUnit 5 test classes
📦 **CLI & Maven**: Both standalone CLI and Maven plugin support
🔄 **Full Stack**: Generate model, service, repository, controller, DTO, and tests

## Installation

### Via Maven Central (Coming Soon)

```xml
<dependency>
    <groupId>io.github.springartisan</groupId>
    <artifactId>spring-artisan-cli</artifactId>
    <version>1.0.0</version>
</dependency>
```

### From Source

```bash
git clone https://github.com/yourusername/spring-artisan.git
cd spring-artisan
mvn clean install
```

### Standalone CLI

```bash
# Build standalone JAR
mvn clean package -DskipTests
java -jar spring-artisan-cli/target/spring-artisan.jar make --help
```

## Usage

### Basic Commands

#### Generate Model/Entity

```bash
spring-artisan make model User
spring-artisan make model Product --fields "id:uuid,name:string,price:double,stock:integer"
```

#### Generate Service

```bash
spring-artisan make service User
spring-artisan make service User --with-tests
```

#### Generate Controller

```bash
spring-artisan make controller User
spring-artisan make controller Product --api-prefix /api/v2
```

#### Generate Repository

```bash
spring-artisan make repository User
```

#### Generate DTO

```bash
spring-artisan make dto User
```

#### Generate Test

```bash
spring-artisan make test UserService
```

#### Generate Complete Resource (All 6 Files)

```bash
spring-artisan make resource Order \
  --fields "id:uuid,amount:double,status:string,userId:uuid" \
  --with-tests \
  --api-prefix /api/v1
```

### Field Types

Supported field types:
- `string` - String
- `integer` - Integer
- `long` - Long
- `double` - Double
- `boolean` - Boolean
- `date` - java.time.LocalDate
- `timestamp` - java.time.LocalDateTime
- `uuid` - UUID

### Example Usage

```bash
# Create a complete User resource
spring-artisan make resource User \
  --fields "id:uuid,name:string,email:string,age:integer" \
  --with-tests

# This generates:
# - src/main/java/com/example/app/model/User.java
# - src/main/java/com/example/app/service/UserService.java
# - src/main/java/com/example/app/repository/UserRepository.java
# - src/main/java/com/example/app/controller/UserController.java
# - src/main/java/com/example/app/dto/UserDTO.java
# - src/test/java/com/example/app/service/UserServiceTest.java
```

## Configuration

Create a `spring-artisan.yml` file in your project root:

```yaml
spring-artisan:
  package-base: com.example.app
  author: Your Name
  api-prefix: /api/v1
  output-dir: src/main/java
  test-output-dir: src/test/java
  generators:
    include-lombok: true
    include-validation: true
    test-framework: junit5
```

## Maven Plugin Usage

Add to your `pom.xml`:

```xml
<plugin>
    <groupId>io.github.springartisan</groupId>
    <artifactId>spring-artisan-maven-plugin</artifactId>
    <version>1.0.0</version>
</plugin>
```

Then use commands:

```bash
mvn spring-artisan:model -Dname=User
mvn spring-artisan:service -Dname=User -Dfields="id:uuid,name:string"
mvn spring-artisan:controller -Dname=User
```

## Generated Code Examples

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
    
    @Column(name = "name")
    @NotBlank(message = "name cannot be blank")
    private String name;
    
    @Column(name = "email", unique = true)
    @NotBlank(message = "email cannot be blank")
    private String email;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // ... timestamps and lifecycle methods
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
    public List<User> findAll() {
        return repository.findAll();
    }
    
    public User save(User entity) {
        return repository.save(entity);
    }
    
    // ... other CRUD methods
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
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User entity) {
        User saved = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    // ... other endpoints
}
```

### Repository

```java
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Custom queries can be added here
}
```

### Test

```java
@DisplayName("UserService Tests")
class UserServiceTest {
    
    private UserService service;
    
    @Mock
    private UserRepository repository;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new UserService(repository);
    }
    
    @Test
    @DisplayName("Should find all users")
    void testFindAll() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        var result = service.findAll();
        assertThat(result).isEmpty();
    }
    
    // ... more tests
}
```

## Customizing Templates

Place custom Freemarker templates in your project:

```bash
mkdir -p src/spring-artisan/templates
# Copy and modify templates from spring-artisan-core/src/main/resources/templates/
```

Then update `spring-artisan.yml`:

```yaml
spring-artisan:
  templates:
    custom-dir: src/spring-artisan/templates
```

## Project Structure

```
spring-artisan/
├── spring-artisan-core/           # Core generation engine
│   ├── model/                     # Entity definitions
│   ├── generator/                 # Code generators
│   ├── config/                    # Configuration management
│   ├── template/                  # Freemarker integration
│   └── resources/templates/       # Freemarker templates
├── spring-artisan-cli/            # Standalone CLI
│   └── commands/                  # Picocli commands
└── spring-artisan-maven-plugin/   # Maven plugin
    └── Mojos/                     # Maven goal implementations
```

## Technologies

- **Java 17+**
- **Freemarker** - Template engine
- **Picocli** - CLI framework
- **Lombok** - Boilerplate reduction
- **Spring Boot** - Framework support
- **JUnit 5** - Testing framework
- **Maven** - Build management

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

MIT License - see LICENSE file for details

## Support

For issues, questions, or suggestions, please open an issue on GitHub.

---

**Made with ❤️ for Spring Boot developers**
