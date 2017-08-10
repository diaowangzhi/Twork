package com.twork.vo;

public class FriendGroupView {
    private Short id;
    private String name;

    public FriendGroupView(com.twork.pojo.FriendGroup friendGroup) {
        this.id = friendGroup.getId();
        this.name = friendGroup.getName();
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
