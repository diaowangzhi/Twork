package com.twork.dao;

import com.twork.pojo.Group;

import java.util.List;

public interface GroupMapper {
    int deleteByPrimaryKey(Integer gid);

    int insert(Group record);

    Group selectByPrimaryKey(Integer gid);

    List<Group> selectAll();

    int updateByPrimaryKey(Group record);

    List<Group> listGroup(Integer uid);

    int getLastGid();
}