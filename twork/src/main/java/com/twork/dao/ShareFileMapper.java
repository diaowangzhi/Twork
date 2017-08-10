package com.twork.dao;

import com.twork.pojo.ShareFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShareFileMapper {
    int deleteByPrimaryKey(@Param("uid") Integer uid, @Param("hash") String hash);

    int insert(ShareFile record);

    ShareFile selectByPrimaryKey(@Param("uid") Integer uid, @Param("hash") String hash);

    List<ShareFile> selectAll();

    List<ShareFile> listchecked();

    List<ShareFile> listnochecked();

    int updateByPrimaryKey(ShareFile record);
}