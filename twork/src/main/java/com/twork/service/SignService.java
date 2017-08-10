package com.twork.service;

import com.twork.pojo.User;
import com.twork.vo.Result;

public interface SignService {
    Result sign(User user);

    Result listsigns(User user, Integer year, Integer month);
}
