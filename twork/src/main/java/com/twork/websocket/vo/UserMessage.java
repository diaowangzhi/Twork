package com.twork.websocket.vo;

public class UserMessage {

    private Sender mine;
    private ToUser to;

    public Sender getMine() {
        return mine;
    }

    public void setMine(Sender mine) {
        this.mine = mine;
    }

    public ToUser getTo() {
        return to;
    }

    public void setTo(ToUser to) {
        this.to = to;
    }
}
