package com.google.spanner;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class SpannerDataSource {
  private static final String DATABASE_NAME = System.getenv("SPANNER_DATABASE");
  public static DataSource createConnectionPool() {
    try {
      // Make sure the PG JDBC driver is loaded.
      Class.forName("org.postgresql.Driver");

      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(String.format("jdbc:postgresql://localhost:5432/%s", DATABASE_NAME));

      return new HikariDataSource(config);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
