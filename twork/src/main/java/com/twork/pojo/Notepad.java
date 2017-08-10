package com.twork.pojo;

import java.util.Date;

public class Notepad {
    private Integer id;

    private Integer uid;

    private Integer cid;

    private String content;

    private Date datetime;

    private Date reminddatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Date getReminddatetime() {
        return reminddatetime;
    }

    public void setReminddatetime(Date reminddatetime) {
        this.reminddatetime = reminddatetime;
    }
}