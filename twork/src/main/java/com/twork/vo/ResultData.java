package com.twork.vo;

import com.alibaba.fastjson.annotation.JSONField;

public class ResultData extends Result {
    @JSONField(ordinal = 2)
    private Object data;
    {
        data = "";
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
