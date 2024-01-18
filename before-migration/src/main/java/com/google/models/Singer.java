package com.google.models;

import java.util.List;
import java.util.Objects;

public class Singer {
  private final int singerId;
  private final String firstName;
  private final String lastName;
  private final List<Album> albums;

  public Singer(String firstName, String lastName, List<Album> albums) {
    this(-1, firstName, lastName, albums);
  }

  public Singer(int singerId, String firstName, String lastName, List<Album> albums) {
    this.singerId = singerId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.albums = albums;
  }

  public int getSingerId() {
    return singerId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public List<Album> getAlbums() {
    return albums;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Singer)) {
      return false;
    }
    Singer singer = (Singer) o;
    return singerId == singer.singerId && Objects.equals(firstName, singer.firstName)
        && Objects.equals(lastName, singer.lastName) && Objects.equals(albums,
        singer.albums);
  }

  @Override
  public int hashCode() {
    return Objects.hash(singerId, firstName, lastName, albums);
  }

  @Override
  public String toString() {
    return "Singer{" +
        "singerId=" + singerId +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", albums=" + albums +
        '}';
  }
}
