package com.twork.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.twork.pojo.User;

import java.util.LinkedList;
import java.util.List;

public class MemberModel {
    @JSONField(ordinal = 1)
    private UserModel owner;
    @JSONField(ordinal = 2)
    private Integer members;
    @JSONField(ordinal = 3)
    private List<UserModel> list;

    public MemberModel(User user, List<User> userList) {
        this.owner = new UserModel(user);
        this.members = userList.size();
        this.list = new LinkedList<>();
        for (User u : userList) {
            list.add(new UserModel(u));
        }
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public Integer getMembers() {
        return members;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    public List<UserModel> getList() {
        return list;
    }

    public void setList(List<UserModel> list) {
        this.list = list;
    }
}
