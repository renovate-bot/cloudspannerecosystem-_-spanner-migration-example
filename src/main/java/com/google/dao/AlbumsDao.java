package com.google.dao;

import com.google.models.Album;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class AlbumsDao {

  private final DataSource dataSource;

  public AlbumsDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Album.Id insert(Album album) throws SQLException {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO albums (singer_id, album_id, album_title) VALUES (?, ?, ?)")) {
      preparedStatement.setLong(1, album.getId().getSingerId());
      preparedStatement.setLong(2, album.getId().getAlbumId());
      preparedStatement.setString(3, album.getAlbumTitle());

      preparedStatement.executeUpdate();

      return album.getId();
    }
  }

  public List<Album> findBySinger(long singerId) throws SQLException {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT album_id, album_title FROM albums WHERE singer_id = ?")) {
      preparedStatement.setLong(1, singerId);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        List<Album> result = new ArrayList<>();

        while (resultSet.next()) {
          long albumId = resultSet.getLong("album_id");
          String albumTitle = resultSet.getString("album_title");

          result.add(new Album(singerId, albumId, albumTitle));
        }

        return result;
      }
    }
  }

  public int deleteAll() throws SQLException {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "DELETE FROM albums")) {
      return preparedStatement.executeUpdate();
    }
  }
}
