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

public class SpannerDataSource {

  private static final String PGADAPTER_HOST_VAR = "PGADAPTER_HOST";
  private static final String PGADAPTER_PORT_VAR = "PGADAPTER_PORT";
  private static final String DATABASE_NAME_VAR = "SPANNER_DATABASE";

  private SpannerDataSource() {
  }

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
