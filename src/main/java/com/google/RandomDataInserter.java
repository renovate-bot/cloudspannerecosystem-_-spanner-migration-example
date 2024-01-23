package com.google;

import com.google.models.Album;
import com.google.models.Singer;
import com.google.models.Song;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RandomDataInserter {

  private static final int INTERVAL_MILLIS = 5000;
  private final Dao dao;
  private final Random random;

  public RandomDataInserter(Dao dao) {
    this.dao = dao;
    this.random = new Random();
  }

  public void start() {
    while (true) {
      try {
        Singer singer = dao.insert(randomSinger());

        System.out.println("Successfully inserted: " + singer);

        Thread.sleep(INTERVAL_MILLIS);
      } catch (SQLException e) {
        System.err.println("Failed to insert random singer: " + e.getMessage());
        e.printStackTrace();
      } catch (InterruptedException e) {
        System.err.println("Failed to wait for interval of " + INTERVAL_MILLIS + ": " + e.getMessage());
      }
    }
  }

  private Singer randomSinger() {
    UUID uuid = UUID.randomUUID();
    int albumsSize = random.nextInt(4) + 1;
    List<Album> albums = new ArrayList<>(albumsSize);
    for (int i = 0; i < albumsSize; i++) {
      albums.add(randomAlbum());
    }

    return new Singer("FirstName " + uuid, "LastName" + uuid, albums);
  }

  private Album randomAlbum() {
    UUID uuid = UUID.randomUUID();
    int songsSize = random.nextInt(3);
    List<Song> songs = new ArrayList<>(songsSize);
    for (int i = 0; i < songsSize; i++) {
      songs.add(randomSong());
    }

    return new Album("Album " + uuid, songs);
  }

  private Song randomSong() {
    UUID uuid = UUID.randomUUID();
    return new Song("Song " + uuid, "{\"uuid\": \"" + uuid + "\"}");
  }

}
