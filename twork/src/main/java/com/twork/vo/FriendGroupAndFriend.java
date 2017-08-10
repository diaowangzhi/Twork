package com.twork.vo;

import com.twork.pojo.FriendGroup;
import com.twork.pojo.User;

import java.util.LinkedList;
import java.util.List;

public class FriendGroupAndFriend {
    private String text;
    private List<Friend> nodes;

    public FriendGroupAndFriend(FriendGroup friendGroup, List<User> userList) {
        this.nodes = new LinkedList<>();
        this.text = friendGroup.getName();

        for (User user : userList) {
            nodes.add(new Friend(user));
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Friend> getNodes() {
        return nodes;
    }

    public void setNodes(List<Friend> nodes) {
        this.nodes = nodes;
    }

    class Friend {
        private Integer fid;
        private String text;

        public Friend(User user) {
            this.fid = user.getUid();
            this.text = user.getName();
        }

        public Integer getFid() {
            return fid;
        }

        public void setFid(Integer fid) {
            this.fid = fid;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}