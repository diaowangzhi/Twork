package com.twork.websocket.message;

public class Remove {

    private String type;
    private int id;//用户/群组ID

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
