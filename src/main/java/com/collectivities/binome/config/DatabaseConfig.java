package com.collectivities.binome.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    @Bean
    public Connection connection() throws SQLException {
        String host = "localhost";
        String port = "5432";
        String dbName = "collectivity";
        String username = "postgres";
        String password = "ReBorN!17";

        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Connecting to database: " + url);
            System.out.println("Username: " + username);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found", e);
        }
    }
}