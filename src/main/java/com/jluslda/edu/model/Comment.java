package com.jluslda.edu.model;


import com.alibaba.fastjson.JSONObject;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class Comment {

  private Integer id;
  private Integer userId;
  private Integer fatherId;
  private String object;
  private Integer objectId;

  @NotNull(message = "评论内容不能为空")
  private String content;

  private Timestamp createTime;
  private String status;
  private String extra;


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


  public Integer getFatherId() {
    return fatherId;
  }

  public void setFatherId(Integer fatherId) {
    this.fatherId = fatherId;
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


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }


  public String getStatus() { return status; }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getExtra() {
    return extra;
  }

  public void setExtra(String extra) {
    this.extra = extra;
  }

  public void setExtra(String key, Object value) {
    JSONObject jsonObject = JSONObject.parseObject(getExtra());
    jsonObject.put(key, value);
    setExtra(jsonObject.toJSONString());
  }

  public Object getExtra(String key) {
    JSONObject jsonObject = JSONObject.parseObject(getExtra());
    return jsonObject.getOrDefault(key, null);
  }

}
