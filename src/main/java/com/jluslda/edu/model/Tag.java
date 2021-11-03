package com.jluslda.edu.model;


import javax.validation.constraints.NotNull;

public class Tag {

  private Integer id;
  private String object;
  private Integer objectId;

  @NotNull(message = "标签不能为空")
  private String name;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public void setObjectId(Integer object_id) {
    this.objectId = object_id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
