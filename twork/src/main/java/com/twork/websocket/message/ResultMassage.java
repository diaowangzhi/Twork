package com.twork.websocket.message;

import com.twork.vo.Result;

import java.util.List;

public class ResultMassage extends Result {

    private int pages;
    private List<MessageData> data;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<MessageData> getData() {
        return data;
    }

    public void setData(List<MessageData> data) {
        this.data = data;
    }

}
