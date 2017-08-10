package com.twork.service.impl;

import com.twork.dao.SignMapper;
import com.twork.pojo.Sign;
import com.twork.pojo.User;
import com.twork.service.SignService;
import com.twork.util.Code;
import com.twork.util.UserLevel;
import com.twork.vo.Result;
import com.twork.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SignServiceImpl implements SignService {

    @Autowired
    private SignMapper signMapper;

    @Override
    public Result sign(User user) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Sign sign = signMapper.selectByDay(user.getUid(), new Date());
        if (sign != null) {
            result.setCode(Code.ALREADY_SIGN);
            return result;
        }

        sign = new Sign();
        sign.setUid(user.getUid());
        sign.setDatetime(new Date());

        int row = signMapper.insert(sign);
        if (row != 1) {
            row = signMapper.insert(sign);
            if (row != 1) {
                result.setCode(Code.SIGN_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result listsigns(User user, Integer year, Integer month) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        List<Sign> signList = signMapper.selectByUid(user.getUid(), year, month);
        resultData.setCode(Code.SUCCESS);
        resultData.setData(signList);
        return resultData;
    }
}
