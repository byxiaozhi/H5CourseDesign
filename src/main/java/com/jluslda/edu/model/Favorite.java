package com.jluslda.edu.model;


public class Favorite {

  private Integer id;
  private Integer userId;
  private String object;
  private Integer objectId;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }


  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }


  public Integer getObjectId() {
    return objectId;
  }

  public void setObjectId(Integer objectId) {
    this.objectId = objectId;
  }

}
