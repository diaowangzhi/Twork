package com.twork.vo;

import com.twork.pojo.FriendGroup;
import com.twork.pojo.User;

import java.util.LinkedList;
import java.util.List;

public class FriendModel {
    private String groupname;
    private int id;
    private List<UserModel> list;

    public FriendModel(FriendGroup friendGroup, List<User> userList) {
        list = new LinkedList<>();

        setGroupname(friendGroup.getName());
        setId(friendGroup.getId());

        for (User user : userList) {
            list.add(new UserModel(user));
        }
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

    public List<UserModel> getList() {
        return list;
    }

    public void setList(List<UserModel> list) {
        this.list = list;
    }
}
