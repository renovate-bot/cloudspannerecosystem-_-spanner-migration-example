package com.google;

import com.google.cloudsql.CloudSQLDataSource;
import com.google.dao.Dao;
import com.google.spanner.SpannerDataSource;
import java.util.Arrays;
import javax.sql.DataSource;

public class Main {

  public static void main(String[] args) {
    DatabaseChoice databaseChoice = parseDatabaseChoice(args);
    DataSource dataSource = dataSourceFrom(databaseChoice);
    Dao dao = new Dao(databaseChoice, dataSource);
    RandomDataInserter randomDataInserter = new RandomDataInserter(dao);

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
      case CLOUDSQL: return CloudSQLDataSource.createConnectionPool();
      case SPANNER: return SpannerDataSource.createConnectionPool();
      default:
        System.out.println("Unsupported database choice " + databaseChoice);
        System.exit(1);
    }
    return null;
  }
}


