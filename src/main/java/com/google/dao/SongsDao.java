package com.google.dao;

import com.google.models.Album;
import com.google.models.Song;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class SongsDao {
  private final DataSource dataSource;

  public SongsDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Song.Id insert(Song song) throws SQLException {
    try (Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO songs (singer_id, album_id, song_id, song_name, song_data) VALUES (?, ?, ?, ?, CAST(? AS JSON))")) {
      preparedStatement.setLong(1, song.getId().getSingerId());
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
      preparedStatement.setLong(1, albumId.getSingerId());
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
