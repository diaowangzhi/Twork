package com.twork.vo;

import com.twork.pojo.Group;

public class GroupModel {
    private String groupname;
    private int id;
    private String avatar;

    public GroupModel(Group group) {
        avatar = "layui/css/modules/layim/skin/logo.jpg";
        setGroupname(group.getName());
        setId(group.getGid());
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
