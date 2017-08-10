package com.twork.dao;

import com.twork.pojo.Friend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendMapper {
    int deleteByPrimaryKey(@Param("uid") Integer uid, @Param("fid") Integer fid);

    int insert(Friend record);

    Friend selectByPrimaryKey(@Param("uid") Integer uid, @Param("fid") Integer fid);

    List<Friend> selectByUid(Integer uid);

    List<Friend> selectAll();

    int changeFriendGroup(@Param("uid") Integer uid, @Param("id") Short id, @Param("newId") Short newId);

    int updateByPrimaryKey(Friend record);
}