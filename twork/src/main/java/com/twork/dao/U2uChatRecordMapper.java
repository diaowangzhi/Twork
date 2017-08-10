package com.twork.dao;

import com.twork.pojo.U2uChatRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface U2uChatRecordMapper {
    int deleteByPrimaryKey(@Param("sid") Integer sid, @Param("rid") Integer rid, @Param("datetime") Date datetime);

    int insert(U2uChatRecord record);

    U2uChatRecord selectByPrimaryKey(@Param("sid") Integer sid, @Param("rid") Integer rid, @Param("datetime") Date datetime);

    List<U2uChatRecord> selectAll();

    List<U2uChatRecord> selectByUid(@Param("sid") Integer sid, @Param("rid") Integer rid);

    List<U2uChatRecord> offLineRecord(@Param("rid") Integer rid, @Param("lastonlinedatetime") Date lastonlinedatetime);

    int updateByPrimaryKey(U2uChatRecord record);
}