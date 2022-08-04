package ru.fmtk.khlystov.yatt.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class RepositoryInContainerTest {

    @Container
    protected static TestPostgresContainer postgresContainer = new TestPostgresContainer();

    @DynamicPropertySource
    protected static void resgiterDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
                + postgresContainer.getHost() + ":" + postgresContainer.getFirstMappedPort()
                + "/" + postgresContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", () -> postgresContainer.getUsername());
        registry.add("spring.r2dbc.password", () -> postgresContainer.getPassword());


        registry.add("yatt.db.url", postgresContainer::getJdbcUrl);
        registry.add("yatt.db.username", postgresContainer::getUsername);
        registry.add("yatt.db.password", postgresContainer::getPassword);

        registry.add("yatt.db.r2db-url", () -> "r2dbc:postgresql://"
                + postgresContainer.getHost() + ":" + postgresContainer.getFirstMappedPort()
                + "/" + postgresContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", postgresContainer::getUsername);
        registry.add("spring.r2dbc.password", postgresContainer::getPassword);
    }
}
