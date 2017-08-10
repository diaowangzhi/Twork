package com.twork.service;

import com.twork.pojo.User;
import com.twork.vo.Result;

public interface NoticeService {
    Result releasenotice(User user, String content);

    Result shownotice(User user);

    Result delnotice(User user, Integer id);

    Result test();
}
