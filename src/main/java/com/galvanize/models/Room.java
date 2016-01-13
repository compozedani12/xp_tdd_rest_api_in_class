package com.galvanize.models;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

public class Room {
  @Id
  private String id;

  @NotNull
  @Length(min = 1, message = "Room name must be more than 1 character")
  private String name;

  private String campusName;
  private int capacity;
  private boolean vc;

  public Room() {}

  public Room(String name, String campusName, int capacity, boolean vc) {
    this.name = name;
    this.campusName = campusName;
    this.capacity = capacity;
    this.vc = vc;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCampusName() {
    return campusName;
  }

  public int getCapacity() {
    return capacity;
  }

  public boolean isVc() {
    return vc;
  }
}
