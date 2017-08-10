package com.twork.service;

import com.twork.pojo.User;
import com.twork.vo.Result;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

public interface UserService {
    Result register(String name, String password, String email);

    Result login(Model model, Integer uid, String password);

    Result logout(User user, SessionStatus sessionStatus);

    Result init(User user);

    Result listInfo(User user, Integer uid);

    Result changeinfo(User user, String name, String email);

    Result exitgroup(User user, Integer gid);

    Result shieldgroup(User user, Integer gid);

    Result listrecord(User user, Integer uid);
}
