package com.twork.dao;

import com.twork.pojo.File;
import java.util.List;

public interface FileMapper {
    int deleteByPrimaryKey(String hash);

    int insert(File record);

    File selectByPrimaryKey(String hash);

    List<File> selectAll();

    int updateByPrimaryKey(File record);
}