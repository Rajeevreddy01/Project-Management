package com.pms.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * Render provides {@code DATABASE_URL} as {@code postgresql://user:pass@host/db}.
 * Spring Boot expects {@code spring.datasource.url} in JDBC form and separate credentials.
 * Also sets Hibernate dialect when PostgreSQL is used so startup does not fail if JDBC
 * metadata is temporarily unavailable.
 */
public class RenderDatabaseEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String PG_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";
    private static final String SOURCE_NAME = "renderPostgresOverrides";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String dbUrl = System.getenv("DB_URL");
        String databaseUrl = System.getenv("DATABASE_URL");

        Map<String, Object> props = new HashMap<>();

        if (dbUrl != null && dbUrl.startsWith("jdbc:postgresql:")) {
            props.put("spring.jpa.database-platform", PG_DIALECT);
        } else if (dbUrl != null && isPostgresUri(dbUrl)) {
            putParsedPostgres(props, dbUrl);
        } else if (databaseUrl != null && isPostgresUri(databaseUrl)) {
            putParsedPostgres(props, databaseUrl);
        }

        if (props.isEmpty()) {
            return;
        }

        if (environment.getPropertySources().contains(SOURCE_NAME)) {
            environment.getPropertySources().remove(SOURCE_NAME);
        }
        environment.getPropertySources().addFirst(new MapPropertySource(SOURCE_NAME, props));
    }

    private static boolean isPostgresUri(String url) {
        return url.startsWith("postgresql://") || url.startsWith("postgres://");
    }

    private static void putParsedPostgres(Map<String, Object> props, String postgresUrl) {
        ParsedConnection p = parsePostgresConnectionUri(postgresUrl);
        props.put("spring.datasource.url", p.jdbcUrl());
        props.put("spring.datasource.username", p.username());
        props.put("spring.datasource.password", p.password());
        props.put("spring.jpa.database-platform", PG_DIALECT);
    }

    private record ParsedConnection(String jdbcUrl, String username, String password) {}

    private static ParsedConnection parsePostgresConnectionUri(String postgresUrl) {
        String normalized = postgresUrl.replaceFirst("^postgres(ql)?://", "http://");
        try {
            URI uri = new URI(normalized);
            String host = uri.getHost();
            if (host == null || host.isEmpty()) {
                throw new IllegalArgumentException("missing host");
            }
            int port = uri.getPort() > 0 ? uri.getPort() : 5432;
            String path = uri.getPath();
            String database = (path != null && path.length() > 1) ? path.substring(1) : "";
            String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?sslmode=require", host, port, database);

            String user = "postgres";
            String pass = "";
            String userInfo = uri.getUserInfo();
            if (userInfo != null && !userInfo.isEmpty()) {
                int colon = userInfo.indexOf(':');
                if (colon >= 0) {
                    user = URLDecoder.decode(userInfo.substring(0, colon), StandardCharsets.UTF_8);
                    pass = URLDecoder.decode(userInfo.substring(colon + 1), StandardCharsets.UTF_8);
                } else {
                    user = URLDecoder.decode(userInfo, StandardCharsets.UTF_8);
                }
            }

            return new ParsedConnection(jdbcUrl, user, pass);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid PostgreSQL connection URL", e);
        }
    }
}
