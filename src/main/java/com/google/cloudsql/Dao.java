package com.google.cloudsql;

import com.google.models.Album;
import com.google.models.Singer;
import com.google.models.Song;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class Dao {

  public static class Singers {

    private final DataSource dataSource;

    public Singers(DataSource dataSource) {
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

  public static class Albums {

    private final DataSource dataSource;

    public Albums(DataSource dataSource) {
      this.dataSource = dataSource;
    }

    public Album.Id insert(Album album) throws SQLException {
      try (Connection connection = dataSource.getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(
              "INSERT INTO albums (singer_id, album_id, album_title) VALUES (?, ?, ?)")) {
        preparedStatement.setInt(1, album.getId().getSingerId());
        preparedStatement.setLong(2, album.getId().getAlbumId());
        preparedStatement.setString(3, album.getAlbumTitle());

        preparedStatement.executeUpdate();

        return album.getId();
      }
    }

    public List<Album> findBySinger(int singerId) throws SQLException {
      try (Connection connection = dataSource.getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(
              "SELECT album_id, album_title FROM albums WHERE singer_id = ?")) {
        preparedStatement.setInt(1, singerId);

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

  public static class Songs {

    private final DataSource dataSource;

    public Songs(DataSource dataSource) {
      this.dataSource = dataSource;
    }

    public Song.Id insert(Song song) throws SQLException {
      try (Connection connection = dataSource.getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(
              "INSERT INTO songs (singer_id, album_id, song_id, song_name, song_data) VALUES (?, ?, ?, ?, CAST(? AS JSON))")) {
        preparedStatement.setInt(1, song.getId().getSingerId());
        preparedStatement.setLong(2, song.getId().getAlbumId());
        preparedStatement.setLong(3, song.getId().getSongId());
        preparedStatement.setString(4, song.getSongName());
        preparedStatement.setString(5, song.getSongData());

        preparedStatement.executeUpdate();

        return song.getId();
      }
    }

    public List<Song> findByAlbum(Album.Id albumId) throws SQLException {
      try (Connection connection = dataSource.getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(
              "SELECT song_id, song_name, song_data FROM songs WHERE singer_id = ? AND album_id = ?")) {
        preparedStatement.setInt(1, albumId.getSingerId());
        preparedStatement.setLong(2, albumId.getAlbumId());

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
          List<Song> result = new ArrayList<>();

          while (resultSet.next()) {
            long songId = resultSet.getLong("song_id");
            String songName = resultSet.getString("song_name");
            String songData = resultSet.getString("song_data");

            result.add(new Song(albumId, songId, songName, songData));
          }

          return result;
        }
      }
    }

    public int deleteAll() throws SQLException {
      try (Connection connection = dataSource.getConnection();
          PreparedStatement preparedStatement = connection.prepareStatement(
              "DELETE FROM songs")) {
        return preparedStatement.executeUpdate();
      }
    }
  }
}
