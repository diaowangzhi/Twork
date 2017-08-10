package com.twork.pojo;

import java.util.Date;

public class GroupFile {
    private Integer gid;

    private Integer uid;

    private String hash;

    private String name;

    private Date datetime;

    private Date availabledatetime;

    private Short count;

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Date getAvailabledatetime() {
        return availabledatetime;
    }

    public void setAvailabledatetime(Date availabledatetime) {
        this.availabledatetime = availabledatetime;
    }

    public Short getCount() {
        return count;
    }

    public void setCount(Short count) {
        this.count = count;
    }
}