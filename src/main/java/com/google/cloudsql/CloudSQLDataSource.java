package com.google.cloudsql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class CloudSQLDataSource {
  // Note: Saving credentials in environment variables is convenient, but not
  // secure - consider a more secure solution such as
  // Cloud Secret Manager (https://cloud.google.com/secret-manager) to help
  // keep secrets safe.
  private static final String DATABASE_NAME = System.getenv("DB_NAME");
  private static final String INSTANCE_CONNECTION_NAME = System.getenv("INSTANCE_CONNECTION_NAME");
  private static final String USERNAME = System.getenv("DB_USERNAME");
  private static final String PASSWORD = System.getenv("DB_PASSWORD");

  public static DataSource createConnectionPool() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(String.format("jdbc:postgresql:///%s", DATABASE_NAME));
    config.setUsername(USERNAME);
    config.setPassword(PASSWORD);
    config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory");
    config.addDataSourceProperty("cloudSqlInstance", INSTANCE_CONNECTION_NAME);

    return new HikariDataSource(config);
  }
}
