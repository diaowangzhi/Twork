package com.twork.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.twork.pojo.Group;
import com.twork.pojo.User;

import java.util.LinkedList;
import java.util.List;

public class GroupAndMember {
    @JSONField(ordinal = 1)
    private String text;
    @JSONField(ordinal = 2)
    private Integer gid;
    @JSONField(ordinal = 3)
    private List<Member> nodes;

    public GroupAndMember(Group group, List<User> userList) {
        this.gid = group.getGid();
        this.text = group.getName();
        this.nodes = new LinkedList<>();
        for (User user : userList) {
            nodes.add(new Member(user));
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public List<Member> getNodes() {
        return nodes;
    }

    public void setNodes(List<Member> nodes) {
        this.nodes = nodes;
    }

    class Member {
        private Integer fid;
        private String text;

        Member(User user) {
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
