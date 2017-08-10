package com.twork.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class SignCount {
    @JSONField(ordinal = 1)
    private int usernum;
    @JSONField(ordinal = 2)
    private int signnum;
    @JSONField(ordinal = 3)
    private double average;

    public int getUsernum() {
        return usernum;
    }

    public void setUsernum(int usernum) {
        this.usernum = usernum;
    }

    public int getSignnum() {
        return signnum;
    }

    public void setSignnum(int signnum) {
        this.signnum = signnum;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
