package com.twork.vo;

import java.util.List;

public class UserInfo {
    private UserModel mine;
    private List<FriendModel> friend;
    private List<GroupModel> group;

    public UserModel getMine() {
        return mine;
    }

    public void setMine(UserModel mine) {
        this.mine = mine;
    }

    public List<FriendModel> getFriend() {
        return friend;
    }

    public void setFriend(List<FriendModel> friend) {
        this.friend = friend;
    }

    public List<GroupModel> getGroup() {
        return group;
    }

    public void setGroup(List<GroupModel> group) {
        this.group = group;
    }
}
