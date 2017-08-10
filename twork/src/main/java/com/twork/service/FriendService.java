package com.twork.service;

import com.twork.pojo.User;
import com.twork.vo.Result;

public interface FriendService {
    Result search(User user, String userinfo);

    Result addfriendMessage(User user, Integer fid);

    Result addfriend(User user, Integer fid);

    Result delfriend(User user, Integer fid);
}
