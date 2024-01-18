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
    return singerId == album.singerId && albumId == album.albumId && Objects.equals(
        albumTitle, album.albumTitle) && Objects.equals(songs, album.songs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(singerId, albumId, albumTitle, songs);
  }

  @Override
  public String toString() {
    return "Album{" +
        "singerId=" + singerId +
        ", albumId=" + albumId +
        ", albumTitle='" + albumTitle + '\'' +
        ", songs=" + songs +
        '}';
  }
}