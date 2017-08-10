package com.twork.websocket.vo;

public class GroupMessage {
    private Sender mine;
    private ToGroup to;

    public Sender getMine() {
        return mine;
    }

    public void setMine(Sender mine) {
        this.mine = mine;
    }

    public ToGroup getTo() {
        return to;
    }

    public void setTo(ToGroup to) {
        this.to = to;
    }

}
