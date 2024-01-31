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

import java.util.List;
import java.util.Objects;

public class Album {

  private final long singerId;
  private final long albumId;
  private final String albumTitle;
  private final List<Song> songs;

  public Album(String albumTitle, List<Song> songs) {
    this(-1, -1, albumTitle, songs);
  }

  public Album(long singerId, long albumId, String albumTitle, List<Song> songs) {
    this.singerId = singerId;
    this.albumId = albumId;
    this.albumTitle = albumTitle;
    this.songs = songs;
  }

  public long getSingerId() {
    return singerId;
  }

  public long getAlbumId() {
    return albumId;
  }

  public String getAlbumTitle() {
    return albumTitle;
  }

  public List<Song> getSongs() {
    return songs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Album)) {
      return false;
    }
    Album album = (Album) o;
    return singerId == album.singerId && albumId == album.albumId && Objects.equals(albumTitle,
        album.albumTitle) && Objects.equals(songs, album.songs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(singerId, albumId, albumTitle, songs);
  }

  @Override
  public String toString() {
    return "Album{" + "singerId=" + singerId + ", albumId=" + albumId + ", albumTitle='"
        + albumTitle + '\'' + ", songs=" + songs + '}';
  }
}