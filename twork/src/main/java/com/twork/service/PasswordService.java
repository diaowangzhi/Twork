package com.twork.service;

import com.twork.pojo.User;
import com.twork.vo.Result;

public interface PasswordService {
    Result changepassword(User user, String oldPassword, String newPassword);
}
