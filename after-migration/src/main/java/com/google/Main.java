package com.google;

import java.util.Arrays;
import javax.sql.DataSource;

public class Main {

  public static void main(String[] args) {
    DataSource dataSource = SpannerDataSource.createConnectionPool();
    Dao dao = new Dao(databaseChoice, dataSource);
    RandomDataInserter randomDataInserter = new RandomDataInserter(dao);

    randomDataInserter.start();
  }
}


