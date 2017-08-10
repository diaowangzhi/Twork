package com.twork.dao;

import com.twork.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(User record);

    User selectByPrimaryKey(Integer uid);

    List<User> selectAll();

    List<User> listNonCheckedUser();

    List<User> listGroupUser(Integer gid);

    List<User> listFriendGroupUser(@Param("uid") Integer uid, @Param("name") String name);

    int updateByPrimaryKey(User record);

    int countUser();

    int countAvailableUser();

    int getLastUid();

    List<User> selectByUidOrName(String userinfo);
}