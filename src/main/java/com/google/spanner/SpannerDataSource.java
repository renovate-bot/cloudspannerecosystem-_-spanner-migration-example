package com.google.spanner;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class SpannerDataSource {
  private static final String DATABASE_NAME_VAR = "SPANNER_DATABASE";
  public static DataSource createConnectionPool() {
    try {
      // Make sure the PG JDBC driver is loaded.
      Class.forName("org.postgresql.Driver");

      String databaseName = getEnvVariable(DATABASE_NAME_VAR);
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(String.format("jdbc:postgresql://localhost:5432/%s", databaseName));

      return new HikariDataSource(config);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private static String getEnvVariable(String name) {
    String value = System.getenv(name);
    if (value == null) {
      System.err.println("Error: Please set the environment variable " + name);
      System.exit(1);
    }
    return value;
  }
}
