package com.twork.service;

import com.twork.pojo.User;
import com.twork.vo.Result;

public interface FriendGroupService {
    Result addgroup(User user, String name, boolean register);

    Result delgroup(User user, String name);

    Result changegroup(User user, String oldname, String newname);

    Result listgroup(User user);

    Result movefriend(User user, Integer fid, String newname);

    Result listgroupfriend(User user, String name);

    Result listgroupAndfriend(User user);

    Result test();
}
