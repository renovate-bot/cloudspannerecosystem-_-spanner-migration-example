package com.google.models;

import java.util.Objects;

public class Album {
  public static class Id {

    @Override
    public String toString() {
      return "(" + singerId + ", " + albumId + ')';
    }

    private final long singerId;
    private final long albumId;

    public Id(long singerId, long albumId) {
      this.singerId = singerId;
      this.albumId = albumId;
    }

    public long getSingerId() {
      return singerId;
    }

    public long getAlbumId() {
      return albumId;
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
      return singerId == id.singerId && albumId == id.albumId;
    }

    @Override
    public int hashCode() {
      return Objects.hash(singerId, albumId);
    }
  }

  private final Id id;
  private final String albumTitle;

  public Album(long singerId, long albumId, String albumTitle) {
    this.id = new Id(singerId, albumId);
    this.albumTitle = albumTitle;
  }

  public Id getId() {
    return id;
  }

  public String getAlbumTitle() {
    return albumTitle;
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
    return Objects.equals(id, album.id) && Objects.equals(albumTitle,
        album.albumTitle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, albumTitle);
  }

  @Override
  public String toString() {
    return "Album{" +
        "id=" + id +
        ", albumTitle='" + albumTitle + '\'' +
        '}';
  }
}
