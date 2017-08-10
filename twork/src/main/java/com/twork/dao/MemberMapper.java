package com.twork.dao;

import com.twork.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberMapper {
    int deleteByPrimaryKey(@Param("gid") Integer gid, @Param("uid") Integer uid);

    int deleteByGid(Integer gid);

    int insert(Member record);

    Member selectByPrimaryKey(@Param("gid") Integer gid, @Param("uid") Integer uid);

    List<Member> selectAll();

    int updateByPrimaryKey(Member record);

    List<Member> selectByGid(Integer gid);
}