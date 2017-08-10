package test;

import com.twork.pojo.File;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface FileMapper {
    @Delete({
        "delete from file",
        "where hash = #{hash,jdbcType=CHAR}"
    })
    int deleteByPrimaryKey(String hash);

    @Insert({
        "insert into file (hash, path, size, ",
        "datetime, count)",
        "values (#{hash,jdbcType=CHAR}, #{path,jdbcType=CHAR}, #{size,jdbcType=INTEGER}, ",
        "#{datetime,jdbcType=TIMESTAMP}, #{count,jdbcType=SMALLINT})"
    })
    int insert(File record);

    @Select({
        "select",
        "hash, path, size, datetime, count",
        "from file",
        "where hash = #{hash,jdbcType=CHAR}"
    })
    @Results({
        @Result(column="hash", property="hash", jdbcType=JdbcType.CHAR, id=true),
        @Result(column="path", property="path", jdbcType=JdbcType.CHAR),
        @Result(column="size", property="size", jdbcType=JdbcType.INTEGER),
        @Result(column="datetime", property="datetime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="count", property="count", jdbcType=JdbcType.SMALLINT)
    })
    File selectByPrimaryKey(String hash);

    @Select({
        "select",
        "hash, path, size, datetime, count",
        "from file"
    })
    @Results({
        @Result(column="hash", property="hash", jdbcType=JdbcType.CHAR, id=true),
        @Result(column="path", property="path", jdbcType=JdbcType.CHAR),
        @Result(column="size", property="size", jdbcType=JdbcType.INTEGER),
        @Result(column="datetime", property="datetime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="count", property="count", jdbcType=JdbcType.SMALLINT)
    })
    List<File> selectAll();

    @Update({
        "update file",
        "set path = #{path,jdbcType=CHAR},",
          "size = #{size,jdbcType=INTEGER},",
          "datetime = #{datetime,jdbcType=TIMESTAMP},",
          "count = #{count,jdbcType=SMALLINT}",
        "where hash = #{hash,jdbcType=CHAR}"
    })
    int updateByPrimaryKey(File record);
}