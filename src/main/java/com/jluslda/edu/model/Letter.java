package com.jluslda.edu.model;

import java.sql.Timestamp;

public class Letter {

  private Integer id;
  private Integer userId;
  private Integer fromUserId;
  private Integer toUserId;
  private String content;
  private Timestamp datetime = new Timestamp(new java.util.Date().getTime());;
  private String status = "unread";


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


  public Integer getFromUserId() {
    return fromUserId;
  }

  public void setFromUserId(Integer fromUserId) {
    this.fromUserId = fromUserId;
  }


  public Integer getToUserId() {
    return toUserId;
  }

  public void setToUserId(Integer toUserId) {
    this.toUserId = toUserId;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public java.sql.Timestamp getDatetime() {
    return datetime;
  }

  public void setDatetime(java.sql.Timestamp datetime) {
    this.datetime = datetime;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
