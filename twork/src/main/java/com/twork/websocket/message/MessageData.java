package com.twork.websocket.message;

import com.twork.vo.UserModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageData {
    private int id;
    private int uid;
    private Integer from;
    private int from_group;
    private int type;
    private String content;
    private String remark;
    private String href;
    private int read;
    private Date time;
    private UserModel user;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public int getFrom_group() {
        return from_group;
    }

    public void setFrom_group(int from_group) {
        this.from_group = from_group;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read ? 1 : 0;
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
