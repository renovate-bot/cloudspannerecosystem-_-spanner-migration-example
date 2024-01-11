package com.google.models;

import java.util.Objects;

public class Song {
  public static class Id {
    private final int singerId;
    private final long albumId;
    private final long songId;

    public Id(int singerId, long albumId, long songId) {
      this.singerId = singerId;
      this.albumId = albumId;
      this.songId = songId;
    }

    public int getSingerId() {
      return singerId;
    }

    public long getAlbumId() {
      return albumId;
    }

    public long getSongId() {
      return songId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Id)) {
        return false;
      }
      Id id = (Id) o;
      return singerId == id.singerId && albumId == id.albumId && songId == id.songId;
    }

    @Override
    public int hashCode() {
      return Objects.hash(singerId, albumId, songId);
    }

    @Override
    public String toString() {
      return "Id{" +
          "singerId=" + singerId +
          ", albumId=" + albumId +
          ", songId=" + songId +
          '}';
    }
  }

  private final Id id;
  private final String songName;
  private final String songData;

  public Song(Album.Id albumId, long songId, String songName, String songData) {
    this.id = new Id(albumId.getSingerId(), albumId.getAlbumId(), songId);
    this.songName = songName;
    this.songData = songData;
  }

  public Id getId() {
    return id;
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
    return Objects.equals(id, song.id) && Objects.equals(songName, song.songName)
        && Objects.equals(songData, song.songData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, songName, songData);
  }

  @Override
  public String toString() {
    return "Song{" +
        "id=" + id +
        ", songName='" + songName + '\'' +
        ", songData='" + songData + '\'' +
        '}';
  }
}
