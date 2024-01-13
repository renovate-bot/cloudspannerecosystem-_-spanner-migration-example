package com.google.models;

import java.util.Objects;

public class Singer {
  private final long singerId;
  private final String firstName;
  private final String lastName;

  public Singer(String firstName, String lastName) {
    this(-1, firstName, lastName);
  }

  public Singer(long singerId, String firstName, String lastName) {
    this.singerId = singerId;
    this.firstName = firstName;
    this.lastName = lastName;
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
        && Objects.equals(lastName, singer.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(singerId, firstName, lastName);
  }

  @Override
  public String toString() {
    return "Singer{" +
        "singerId=" + singerId +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        '}';
  }
}
