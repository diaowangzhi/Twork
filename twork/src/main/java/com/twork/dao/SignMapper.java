package com.twork.dao;

import com.twork.pojo.Sign;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SignMapper {
    int deleteByPrimaryKey(@Param("uid") Integer uid, @Param("datetime") Date datetime);

    int insert(Sign record);

    List<Sign> selectAll();

//    List<Sign> selectByUid(Integer uid);

    List<Sign> selectByUid(@Param("uid") Integer uid, @Param("year") Integer year, @Param("month") Integer month);

    Sign selectByDay(@Param("uid") Integer uid, @Param("date") Date date);

    List<Sign> selectByDatetime(@Param("year") Integer year, @Param("month") Integer month);

    int count(@Param("year") Integer year, @Param("month") Integer month);
}