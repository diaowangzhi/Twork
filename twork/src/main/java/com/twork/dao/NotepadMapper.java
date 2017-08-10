package com.twork.dao;

import com.twork.pojo.Notepad;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NotepadMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Notepad record);

    Notepad selectByPrimaryKey(@Param("uid") Integer uid,@Param("id")  Integer id);

    List<Notepad> selectAll();

    List<Notepad> selectByUid(Integer uid);

    int updateByPrimaryKey(Notepad record);

    int getLastId(Integer uid);
}