package com.twork.dao;

import com.twork.pojo.GroupChatRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface GroupChatRecordMapper {
    int deleteByPrimaryKey(@Param("gid") Integer gid, @Param("uid") Integer uid, @Param("datetime") Date datetime);

    int insert(GroupChatRecord record);

    GroupChatRecord selectByPrimaryKey(@Param("gid") Integer gid, @Param("uid") Integer uid, @Param("datetime") Date datetime);

    List<GroupChatRecord> selectAll();

    List<GroupChatRecord> selectByGid(Integer gid);

    List<GroupChatRecord> offLineRecord(@Param("gid") Integer gid, @Param("lastonlinedatetime") Date lastonlinedatetime);

    int updateByPrimaryKey(GroupChatRecord record);
}