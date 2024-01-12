package com.google.dao;

import com.google.models.Singer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class SingersDao {

  private final DataSource dataSource;

  public SingersDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public int insert(Singer singer) throws SQLException {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO singers (singer_id, first_name, last_name) VALUES (DEFAULT, ?, ?) RETURNING singer_id")) {
      preparedStatement.setString(1, singer.getFirstName());
      preparedStatement.setString(2, singer.getLastName());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt("singer_id");
        } else {
          throw new SQLException("Creation failed for " + singer);
        }
      }
    }
  }

  public List<Singer> findAll() throws SQLException {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT singer_id, first_name, last_name FROM singers");
        ResultSet resultSet = preparedStatement.executeQuery()) {
      List<Singer> result = new ArrayList<>();

      while (resultSet.next()) {
        int singerId = resultSet.getInt("singer_id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");

        result.add(new Singer(singerId, firstName, lastName));
      }

      return result;
    }
  }

  public int deleteAll() throws SQLException {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "DELETE FROM singers")) {
      return preparedStatement.executeUpdate();
    }
  }
}
