package org.example.barbershopbackend.debug;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DbTestRunner implements CommandLineRunner {

    private final DataSource dataSource;

    public DbTestRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✅ Connected to SQL!");
            System.out.println("URL: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            System.err.println("❌ FAILED to connect to SQL");
            e.printStackTrace();
        }
    }
}
