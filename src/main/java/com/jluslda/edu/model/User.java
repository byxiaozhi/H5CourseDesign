package com.jluslda.edu.model;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class User {

    private Integer id;

    @NotNull(message = "昵称不能为空")
    @Length(min = 2, max = 64, message = "昵称长度需要在2到64之间")
    private String nickname;

    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    @NotNull(message = "密码不能为空")
    @Length(min = 6, max = 64, message = "密码长度需要在6到64之间")
    private String password;

    private Timestamp createTime;
    private Timestamp updateTime;
    private String signature;
    private String status;
    private String extra;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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
