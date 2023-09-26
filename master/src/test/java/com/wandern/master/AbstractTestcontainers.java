package com.wandern.master;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
public abstract class AbstractTestcontainers {

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("wandern-dao-unit-test")
                    .withUsername("wandern")
                    .withPassword("password");

    @DynamicPropertySource
    private static void registerDataSourseProperties(DynamicPropertyRegistry registry){
        registry.add(
                "spring.datasourse.url",
                postgreSQLContainer::getJdbcUrl
        );
        registry.add(
                "spring.datasourse.username",
                postgreSQLContainer::getUsername
        );
        registry.add(
                "spring.datasourse.password",
                postgreSQLContainer::getPassword
        );
    }
}
