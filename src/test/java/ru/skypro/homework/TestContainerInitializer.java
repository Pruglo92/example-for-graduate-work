package ru.skypro.homework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public abstract class TestContainerInitializer {

    private static final String DATABASE_NAME = "postgres:15.3";
    private static final PostgreSQLContainer<?> container;
    @Autowired
    protected MockMvc mockMvc;

    static {
        container = new PostgreSQLContainer<>(DATABASE_NAME)
                .withDatabaseName("postgres")
                .withUsername("postgres")
                .withPassword("postgres");

        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}