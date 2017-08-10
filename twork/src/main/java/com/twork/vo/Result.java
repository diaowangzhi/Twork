package com.twork.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class Result {
    @JSONField(ordinal = 1)
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
