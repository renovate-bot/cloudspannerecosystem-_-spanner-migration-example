package com.google;

import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import javax.sql.DataSource;

public class Main {

  public static void main(String[] args) throws SQLException {
    DatabaseChoice databaseChoice = parseDatabaseChoice(args);
    DataSource dataSource = dataSourceFrom(databaseChoice);
    Dao dao = new Dao(databaseChoice, dataSource);
    RandomDataInserter randomDataInserter = new RandomDataInserter(dao);

    // Only initializes the schema in CloudSQL PostgreSQL to reduce setup toil
    if (databaseChoice == DatabaseChoice.CLOUDSQL) {
      dao.ddlUpdate(cloudSqlSchema());
    }
    randomDataInserter.start();
  }

  private static DatabaseChoice parseDatabaseChoice(String[] args) {
    if (args.length == 0) {
      System.err.println("Please specify one of " + Arrays.toString(DatabaseChoice.values()));
      System.exit(1);
    }
    return DatabaseChoice.valueOf(args[0].toUpperCase());
  }

  private static DataSource dataSourceFrom(DatabaseChoice databaseChoice) {
    switch (databaseChoice) {
      case CLOUDSQL:
        return CloudSQLDataSource.createConnectionPool();
      case SPANNER:
        return SpannerDataSource.createConnectionPool();
      default:
        System.out.println("Unsupported database choice " + databaseChoice);
        System.exit(1);
    }
    return null;
  }

  private static String cloudSqlSchema() {
    try {
      URL url = Resources.getResource("schemas/postgresql_schema.sql");
      return Resources.toString(url, StandardCharsets.UTF_8);
    } catch (IOException e) {
      System.out.println("Could not load CloudSQL schema from file");
      System.exit(1);
    }
    return null;
  }
}


