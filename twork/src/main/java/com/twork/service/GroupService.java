package com.twork.service;

import com.twork.pojo.User;
import com.twork.vo.Result;

public interface GroupService {
    Result add(User user, String name);

    Result change(User user, Integer gid, String name);

    Result list(User user);

    Result delete(User user, Integer gid);

    Result addUser(User user, Integer gid, Integer uid);

    Result delUser(User user, Integer gid, Integer uid);

    Result listUser(User user, Integer gid);

    Result getmembers(User user, Integer gid);

    Result listgroupandmember(User user);

    Result listrecord(User user, Integer gid);

    public Result test();
}
