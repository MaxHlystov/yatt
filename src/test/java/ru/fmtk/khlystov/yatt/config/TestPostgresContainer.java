package ru.fmtk.khlystov.yatt.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class TestPostgresContainer extends PostgreSQLContainer<TestPostgresContainer> {

    private static final String IMAGE_VERSION = "postgres:12.10";

    public TestPostgresContainer() {
        super(IMAGE_VERSION);
    }
}
