package com.google;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class SpannerDataSource {

  private static final String PGADAPTER_HOST_VAR = "PGADAPTER_HOST";
  private static final String PGADAPTER_PORT_VAR = "PGADAPTER_PORT";
  private static final String DATABASE_NAME_VAR = "SPANNER_DATABASE";

  public static DataSource createConnectionPool() {
    try {
      // Make sure the PG JDBC driver is loaded.
      Class.forName("org.postgresql.Driver");

      String databaseName = getEnvVariable(DATABASE_NAME_VAR);
      String pgAdapterHost = getEnvVariable(PGADAPTER_HOST_VAR);
      int pgAdapterPort = Integer.parseInt(getEnvVariable(PGADAPTER_PORT_VAR));
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(
          String.format("jdbc:postgresql://%s:%d/%s", pgAdapterHost, pgAdapterPort, databaseName));

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
