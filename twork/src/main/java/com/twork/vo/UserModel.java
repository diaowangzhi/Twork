package com.twork.vo;

import com.twork.pojo.User;

public class UserModel {
    private String username;
    private Integer id;
    private String status;
    private String sign;
    private String avatar;

    public UserModel() {
        avatar = "/layui/css/modules/layim/skin/logo.jpg";
    }

    public UserModel(User user) {
        avatar = "/layui/css/modules/layim/skin/logo.jpg";
        setUsername(user.getName());
        setId(user.getUid());
        setStatus(user.getStatus());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status ? "online" : "offline";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
