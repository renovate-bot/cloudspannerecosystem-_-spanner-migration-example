/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google;

import com.google.models.Album;
import com.google.models.Singer;
import com.google.models.Song;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class RandomDataInserter {

  private static final int INTERVAL_MILLIS = 5000;
  private static final int MAX_ALBUMS_PER_SINGER = 4;
  private static final int MAX_SONGS_PER_ALBUM = 3;
  private final Dao dao;
  private final Random random;

  public RandomDataInserter(final Dao dao) {
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
        System.err.println(
            "Failed to wait for interval of " + INTERVAL_MILLIS + ": " + e.getMessage());
      }
    }
  }

  private Singer randomSinger() {
    UUID uuid = UUID.randomUUID();
    int albumsSize = random.nextInt(MAX_ALBUMS_PER_SINGER) + 1;
    List<Album> albums = new ArrayList<>(albumsSize);
    for (int i = 0; i < albumsSize; i++) {
      albums.add(randomAlbum());
    }

    return new Singer("FirstName " + uuid, "LastName" + uuid, albums);
  }

  private Album randomAlbum() {
    UUID uuid = UUID.randomUUID();
    int songsSize = random.nextInt(MAX_SONGS_PER_ALBUM);
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
