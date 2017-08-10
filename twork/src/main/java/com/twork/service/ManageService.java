package com.twork.service;

import com.twork.pojo.User;
import com.twork.vo.Result;

public interface ManageService {
    Result listuser(User user);

    Result listnoncheckeduser(User user);

    Result changeuserinfo(User user, Integer uid, String name, String email);

    Result changelevel(User user, Integer uid, Integer level);

    Result delUser(User user, Integer uid);

    Result listsign(User user, Integer uid, Integer year, Integer month);

    Result countsign(User user, Integer year, Integer month);

    Result test();

    Result testSession(User user, String name, Integer uid);
}
