package com.google;

import static com.google.DatabaseChoice.SPANNER;

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

  private static final String INSERT_SINGER_QUERY = "INSERT INTO singers (singer_id, first_name, last_name) VALUES (DEFAULT, ?, ?) RETURNING singer_id";
  private static final String INSERT_ALBUM_QUERY = "INSERT INTO albums (singer_id, album_id, album_title) VALUES (?, DEFAULT, ?) RETURNING album_id";
  private static final String INSERT_SONG_QUERY = "INSERT INTO songs (singer_id, album_id, song_id, song_name, song_data) VALUES (?, ?, DEFAULT, ?, CAST(? AS JSONB)) RETURNING song_id";
  private final DataSource dataSource;

  public Dao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Inserts a singer, all of its albums and songs within a single transaction.
   * Returns a copy of the inserted singer, albums and songs with ids populated.
   *
   * @param singer the singer to be inserted. Ids will be ignored as these are generated.
   * @return the inserted singer.
   * @throws SQLException if an error occurred when inserting any of the rows.
   */
  public Singer insert(Singer singer) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      try {
        long singerId = insertSinger(connection, singer);

        List<Album> albums = new ArrayList<>();
        for (Album album : singer.getAlbums()) {
          long albumId = insertAlbum(connection, singerId, album);

          List<Song> songs = new ArrayList<>();
          for (Song song : album.getSongs()) {
            long songId = insertSong(connection, singerId, albumId, song);
            songs.add(new Song(singerId, albumId, songId, song.getSongName(), song.getSongData()));
          }

          albums.add(new Album(singerId, albumId, album.getAlbumTitle(), songs));
        }

        connection.commit();

        return new Singer(singerId, singer.getFirstName(), singer.getLastName(), albums);
      } catch (SQLException e) {
        connection.rollback();
        throw e;
      } finally {
        connection.setAutoCommit(false);
      }
    }
  }

  private long insertSinger(Connection connection, Singer singer) throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SINGER_QUERY)) {
      preparedStatement.setString(1, singer.getFirstName());
      preparedStatement.setString(2, singer.getLastName());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getLong("singer_id");
        } else {
          throw new SQLException("Creation failed for " + singer);
        }
      }
    }
  }

  public long insertAlbum(Connection connection, long singerId, Album album) throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ALBUM_QUERY)) {
      preparedStatement.setLong(1, singerId);
      preparedStatement.setString(2, album.getAlbumTitle());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getLong("album_id");
        } else {
          throw new SQLException("Creation failed for " + album);
        }
      }
    }
  }

  public long insertSong(Connection connection, long singerId, long albumId, Song song)
      throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SONG_QUERY)) {
      preparedStatement.setLong(1, singerId);
      preparedStatement.setLong(2, albumId);
      preparedStatement.setString(3, song.getSongName());
      preparedStatement.setString(4, song.getSongData());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getLong("song_id");
        } else {
          throw new SQLException("Creation failed for " + song);
        }
      }
    }
  }
}
