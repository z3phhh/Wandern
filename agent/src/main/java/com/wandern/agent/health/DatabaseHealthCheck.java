//package com.wandern.agent.health;
//
//import com.wandern.serviceregistrystarter.health.HealthCheck;
//import com.wandern.serviceregistrystarter.health.HealthStatus;
//import com.wandern.serviceregistrystarter.health.Status;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;

/*
@Component
public class DatabaseHealthCheck implements HealthCheck {
    private final DataSource dataSource;

    @Autowired
    public DatabaseHealthCheck(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public HealthStatus check() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1000)) {
                return new HealthStatus(Status.UP, "Database is up");
            } else {
                return new HealthStatus(Status.DOWN, "Failed to validate database connection");
            }
        } catch (SQLException e) {
            return new HealthStatus(Status.DOWN, "Database error: " + e.getMessage());
        }
    }
}*/
