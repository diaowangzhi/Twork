package com.twork.service;

import com.twork.pojo.User;
import com.twork.vo.Result;

import java.util.Date;

public interface NotepadService {
    Result add(User user, Integer uid, String content, Date reminddatetime);

    Result delete(User user, Integer id);

    Result change(User user, Integer id, String content);

    Result list(User user);
}
