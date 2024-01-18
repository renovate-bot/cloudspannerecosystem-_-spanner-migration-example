package com.google;

import javax.sql.DataSource;

public class Main {

  public static void main(String[] args) {
    DataSource dataSource = CloudSQLDataSource.createConnectionPool();
    Dao dao = new Dao(dataSource);
    RandomDataInserter randomDataInserter = new RandomDataInserter(dao);

    randomDataInserter.start();
  }
}


