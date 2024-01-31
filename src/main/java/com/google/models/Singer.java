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

public class Singer {

  private final long singerId;
  private final String firstName;
  private final String lastName;
  private final List<Album> albums;

  public Singer(String firstName, String lastName, List<Album> albums) {
    this(-1, firstName, lastName, albums);
  }

  public Singer(long singerId, String firstName, String lastName, List<Album> albums) {
    this.singerId = singerId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.albums = albums;
  }

  public long getSingerId() {
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
