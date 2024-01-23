package com.google;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class CloudSQLDataSource {
  // Note: Saving credentials in environment variables is convenient, but not
  // secure - consider a more secure solution such as
  // Cloud Secret Manager (https://cloud.google.com/secret-manager) to help
  // keep secrets safe.
  private static final String DATABASE_NAME_VAR = "CLOUDSQL_DATABASE";
  private static final String INSTANCE_CONNECTION_NAME_VAR = "CLOUDSQL_INSTANCE_CONNECTION_NAME";
  private static final String USERNAME_VAR = "CLOUDSQL_USERNAME";
  private static final String PASSWORD_VAR = "CLOUDSQL_PASSWORD";

  public static DataSource createConnectionPool() {
    String databaseName = getEnvVariable(DATABASE_NAME_VAR);
    String instanceConnectionName = getEnvVariable(INSTANCE_CONNECTION_NAME_VAR);
    String username = getEnvVariable(USERNAME_VAR);
    String password = getEnvVariable(PASSWORD_VAR);

    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(String.format("jdbc:postgresql:///%s", databaseName));
    config.setUsername(username);
    config.setPassword(password);
    config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
    config.addDataSourceProperty("cloudSqlInstance", instanceConnectionName);

    return new HikariDataSource(config);
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
