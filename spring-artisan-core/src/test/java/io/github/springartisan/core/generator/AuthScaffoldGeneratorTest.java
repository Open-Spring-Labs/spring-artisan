package io.github.springartisan.core.generator;

import io.github.springartisan.core.config.GeneratorConfig;
import io.github.springartisan.core.generator.AuthScaffoldGenerator.AuthFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class AuthScaffoldGeneratorTest {

    private AuthScaffoldGenerator generator;

    @BeforeEach
    void setUp() {
        GeneratorConfig config = GeneratorConfig.builder()
                .packageBase("com.test.app")
                .author("Test Author")
                .language("java")
                .build();
        generator = new AuthScaffoldGenerator(config);
    }

    private Map<String, String> generateMap() throws Exception {
        List<AuthFile> files = generator.generateAll("User", "email");
        return files.stream().collect(Collectors.toMap(AuthFile::outputPath, AuthFile::content));
    }

    @Test
    void generatesNineFiles() throws Exception {
        List<AuthFile> files = generator.generateAll("User", "email");
        assertThat(files).hasSize(9);
    }

    @Test
    void allOutputPathsAreUnique() throws Exception {
        List<AuthFile> files = generator.generateAll("User", "email");
        long unique = files.stream().map(AuthFile::outputPath).distinct().count();
        assertThat(unique).isEqualTo(9);
    }

    @Test
    void securityConfigIsGenerated() throws Exception {
        Map<String, String> files = generateMap();
        String content = files.get("com/test/app/security/SecurityConfig.java");
        assertThat(content).isNotNull()
                .contains("@Configuration")
                .contains("@EnableWebSecurity")
                .contains("SecurityFilterChain")
                .contains("SessionCreationPolicy.STATELESS")
                .contains("JwtAuthFilter")
                .contains("package com.test.app.security;");
    }

    @Test
    void jwtUtilIsGenerated() throws Exception {
        Map<String, String> files = generateMap();
        String content = files.get("com/test/app/security/JwtUtil.java");
        assertThat(content).isNotNull()
                .contains("@Component")
                .contains("generateToken")
                .contains("isTokenValid")
                .contains("extractUsername")
                .contains("package com.test.app.security;");
    }

    @Test
    void jwtAuthFilterIsGenerated() throws Exception {
        Map<String, String> files = generateMap();
        String content = files.get("com/test/app/security/JwtAuthFilter.java");
        assertThat(content).isNotNull()
                .contains("OncePerRequestFilter")
                .contains("Bearer ")
                .contains("doFilterInternal")
                .contains("package com.test.app.security;");
    }

    @Test
    void customUserDetailsServiceIsGenerated() throws Exception {
        Map<String, String> files = generateMap();
        String content = files.get("com/test/app/security/CustomUserDetailsService.java");
        assertThat(content).isNotNull()
                .contains("UserDetailsService")
                .contains("loadUserByUsername")
                .contains("UserRepository")
                .contains("findByEmail")
                .contains("package com.test.app.security;");
    }

    @Test
    void authControllerIsGenerated() throws Exception {
        Map<String, String> files = generateMap();
        String content = files.get("com/test/app/controller/AuthController.java");
        assertThat(content).isNotNull()
                .contains("@RestController")
                .contains("/api/auth")
                .contains("/register")
                .contains("/login")
                .contains("package com.test.app.controller;");
    }

    @Test
    void authServiceIsGenerated() throws Exception {
        Map<String, String> files = generateMap();
        String content = files.get("com/test/app/service/AuthService.java");
        assertThat(content).isNotNull()
                .contains("@Service")
                .contains("register(RegisterRequest")
                .contains("login(LoginRequest")
                .contains("passwordEncoder.encode")
                .contains("package com.test.app.service;");
    }

    @Test
    void loginRequestIsGenerated() throws Exception {
        Map<String, String> files = generateMap();
        String content = files.get("com/test/app/dto/LoginRequest.java");
        assertThat(content).isNotNull()
                .contains("private String email")
                .contains("private String password")
                .contains("package com.test.app.dto;");
    }

    @Test
    void registerRequestIsGenerated() throws Exception {
        Map<String, String> files = generateMap();
        String content = files.get("com/test/app/dto/RegisterRequest.java");
        assertThat(content).isNotNull()
                .contains("private String email")
                .contains("private String password")
                .contains("private String name")
                .contains("package com.test.app.dto;");
    }

    @Test
    void authResponseIsGenerated() throws Exception {
        Map<String, String> files = generateMap();
        String content = files.get("com/test/app/dto/AuthResponse.java");
        assertThat(content).isNotNull()
                .contains("private String token")
                .contains("Bearer")
                .contains("package com.test.app.dto;");
    }

    @Test
    void usernameFieldIsAppliedToUserDetailsService() throws Exception {
        GeneratorConfig config = GeneratorConfig.builder()
                .packageBase("com.test.app").author("Test").language("java").build();
        AuthScaffoldGenerator gen = new AuthScaffoldGenerator(config);
        List<AuthFile> files = gen.generateAll("User", "username");
        String uds = files.stream()
                .filter(f -> f.outputPath().contains("CustomUserDetailsService"))
                .findFirst().orElseThrow().content();
        assertThat(uds).contains("findByUsername").contains("getUsername");
    }

    @Test
    void customEntityNameIsApplied() throws Exception {
        List<AuthFile> files = generator.generateAll("Account", "email");
        String service = files.stream()
                .filter(f -> f.outputPath().contains("AuthService"))
                .findFirst().orElseThrow().content();
        assertThat(service).contains("AccountRepository").contains("Account user");
    }

    @Test
    void allFilesContainAuthorHeader() throws Exception {
        List<AuthFile> files = generator.generateAll("User", "email");
        for (AuthFile file : files) {
            assertThat(file.content())
                    .as("Author header missing in: " + file.outputPath())
                    .contains("Author: Test Author");
        }
    }

    @Test
    void kotlinModeGeneratesKtExtensions() throws Exception {
        GeneratorConfig kotlinConfig = GeneratorConfig.builder()
                .packageBase("com.test.app").author("Test").language("kotlin").build();
        AuthScaffoldGenerator gen = new AuthScaffoldGenerator(kotlinConfig);
        List<AuthFile> files = gen.generateAll("User", "email");
        assertThat(files).allMatch(f -> f.outputPath().endsWith(".kt"));
    }
}
