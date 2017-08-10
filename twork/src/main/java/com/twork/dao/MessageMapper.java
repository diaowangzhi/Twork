package com.twork.dao;

import com.twork.pojo.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    int deleteByPrimaryKey(@Param("sid") Integer sid, @Param("rid") Integer rid);

    int insert(Message record);

    Message selectByPrimaryKey(@Param("sid") Integer sid, @Param("rid") Integer rid);

    List<Message> selectAll();

    List<Message> selectByUid(Integer uid);

    int updateByPrimaryKey(Message record);

    int count(Integer uid);
}