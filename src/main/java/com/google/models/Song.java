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

package com.google.models;

import java.util.Objects;

public class Song {

  private final long singerId;
  private final long albumId;
  private final long songId;
  private final String songName;
  private final String songData;

  public Song(String songName, String songData) {
    this(-1, -1, -1, songName, songData);
  }

  public Song(long singerId, long albumId, long songId, String songName, String songData) {
    this.singerId = singerId;
    this.albumId = albumId;
    this.songId = songId;
    this.songName = songName;
    this.songData = songData;
  }

  public long getSingerId() {
    return singerId;
  }

  public long getAlbumId() {
    return albumId;
  }

  public long getSongId() {
    return songId;
  }

  public String getSongName() {
    return songName;
  }

  public String getSongData() {
    return songData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Song)) {
      return false;
    }
    Song song = (Song) o;
    return singerId == song.singerId && albumId == song.albumId && songId == song.songId
        && Objects.equals(songName, song.songName) && Objects.equals(songData,
        song.songData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(singerId, albumId, songId, songName, songData);
  }

  @Override
  public String toString() {
    return "Song{" +
        "singerId=" + singerId +
        ", albumId=" + albumId +
        ", songId=" + songId +
        ", songName='" + songName + '\'' +
        ", songData='" + songData + '\'' +
        '}';
  }
}