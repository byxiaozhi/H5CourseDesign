package com.jluslda.edu.model;


import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class Content {

  private Integer id;
  private String object;
  private Integer userId;

  @NotNull(message = "请选择分类")
  private String category;

  @NotNull(message = "标题不能为空")
  @Length(min = 4, max = 64, message = "标题长度需要在4到64之间")
  private String title;

  @NotNull(message = "简介不能为空")
  @Length(min = 4, max = 64, message = "简介长度需要在4到64之间")
  private String description;

  @NotNull(message = "封面不能为空")
  private String cover;

  @NotNull(message = "内容不能为空")
  private String content;

  private Timestamp createTime = new Timestamp(new java.util.Date().getTime());
  private Timestamp updateTime = new Timestamp(new java.util.Date().getTime());
  private String status = "normal";
  private String extra = new JSONObject().toJSONString();


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


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }


  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getCover() {
    return cover;
  }

  public void setCover(String cover) {
    this.cover = cover;
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


  public Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Timestamp updateTime) {
    this.updateTime = updateTime;
  }


  public String getStatus() {
    return status;
  }

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
