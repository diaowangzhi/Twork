package com.twork.pojo;

import java.util.Date;

public class User {
    private Integer uid;

    private String name;

    private String email;

    private Boolean status;

    private Integer level;

    private Date datetime;

    private Date lastonlinedatetime;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Date getLastonlinedatetime() {
        return lastonlinedatetime;
    }

    public void setLastonlinedatetime(Date lastonlinedatetime) {
        this.lastonlinedatetime = lastonlinedatetime;
    }
}