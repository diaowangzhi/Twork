package com.twork.pojo;

import java.util.Date;

public class U2uFile {
    private Integer sid;

    private Integer rid;

    private String hash;

    private String name;

    private Date datetime;

    private Date availabledatetime;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
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
}