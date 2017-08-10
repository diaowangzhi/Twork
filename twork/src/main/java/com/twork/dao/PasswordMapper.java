package com.twork.dao;

import com.twork.pojo.Password;
import java.util.List;

public interface PasswordMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(Password record);

    Password selectByPrimaryKey(Integer uid);

    List<Password> selectAll();

    int updateByPrimaryKey(Password record);
}