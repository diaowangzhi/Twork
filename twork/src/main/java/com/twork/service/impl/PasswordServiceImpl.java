package com.twork.service.impl;

import com.twork.dao.PasswordMapper;
import com.twork.pojo.Password;
import com.twork.pojo.User;
import com.twork.service.PasswordService;
import com.twork.util.Code;
import com.twork.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private PasswordMapper passwordMapper;

    @Override
    public Result changepassword(User user, String oldPassword, String newPassword) {
        Result result = new Result();

        Password password = passwordMapper.selectByPrimaryKey(user.getUid());
        if (!oldPassword.equals(password.getPassword())){
            result.setCode(Code.PASSWORD_ERROR);
            return result;
        }

        password.setPassword(newPassword);
        int row = passwordMapper.updateByPrimaryKey(password);
        if (row != 1) {
            row = passwordMapper.updateByPrimaryKey(password);
            if (row != 1) {
                result.setCode(Code.CHANGE_PASSWORD_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }
}
