/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

  private CloudSQLDataSource() {
  }

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
