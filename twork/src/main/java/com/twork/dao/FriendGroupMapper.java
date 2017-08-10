package com.twork.dao;

import com.twork.pojo.FriendGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendGroupMapper {
    int deleteByPrimaryKey(@Param("uid") Integer uid, @Param("id") Short id);

    int insert(FriendGroup record);

    FriendGroup selectByPrimaryKey(@Param("uid") Integer uid, @Param("id") Short id);

    FriendGroup selectByName(@Param("uid") Integer uid, @Param("name") String name);

    List<FriendGroup> selectById(Integer uid);

    List<FriendGroup> selectAll();

    int updateByPrimaryKey(FriendGroup record);

    short getLastFriendGroupId(Integer uid);
}