package com.twork.service.impl;

import com.twork.dao.SignMapper;
import com.twork.dao.UserMapper;
import com.twork.pojo.Sign;
import com.twork.pojo.User;
import com.twork.service.ManageService;
import com.twork.util.Code;
import com.twork.util.UserLevel;
import com.twork.vo.Result;
import com.twork.vo.ResultData;
import com.twork.vo.SignCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    SignMapper signMapper;

    @Override
    public Result listuser(User user) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.MANAGER_USER) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        List<User> listUser = userMapper.selectAll();
        resultData.setData(listUser);
        resultData.setCode(Code.SUCCESS);
        return resultData;
    }

    @Override
    public Result listnoncheckeduser(User user) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.MANAGER_USER) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        List<User> listUser = userMapper.listNonCheckedUser();
        resultData.setData(listUser);
        resultData.setCode(Code.SUCCESS);
        return resultData;
    }

    @Override
    public Result changeuserinfo(User user, Integer uid, String name, String email) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.MANAGER_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        User changeUser = userMapper.selectByPrimaryKey(uid);
        changeUser.setName(name);
        changeUser.setEmail(email);
        int row = userMapper.updateByPrimaryKey(changeUser);
        if (row != 1) {
            result.setCode(Code.CHANGE_USER_INFO_FAILED);
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result changelevel(User user, Integer uid, Integer level) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.MANAGER_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        if (!UserLevel.check(level)) {
            result.setCode(Code.PARAMETER_NOT_MATCH);
            return result;
        }

        if (uid.equals(10000)) {
            result.setCode(Code.CAN_NOT_CHANGE_ADMIN_LEVEL);
            return result;
        }

        User changeUser = userMapper.selectByPrimaryKey(uid);
        changeUser.setLevel(level);
        int row = userMapper.updateByPrimaryKey(changeUser);
        if (row != 1) {
            result.setCode(Code.CHANGE_USER_LEVEL_FAILED);
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result delUser(User user, Integer uid) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.MANAGER_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        User changeUser = userMapper.selectByPrimaryKey(uid);
        changeUser.setLevel(UserLevel.UNAVAILABLE_USER);
        int row = userMapper.updateByPrimaryKey(changeUser);
        if (row != 1) {
            result.setCode(Code.DELETE_USER_FAILED);
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result listsign(User user, Integer uid, Integer year, Integer month) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.MANAGER_USER) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        List<Sign> signList = signMapper.selectByUid(uid, year, month);

        resultData.setData(signList);
        resultData.setCode(Code.SUCCESS);
        return resultData;
    }

    @Override
    public Result countsign(User user, Integer year, Integer month) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.MANAGER_USER) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        int usernum = userMapper.countAvailableUser();
        int signnum = signMapper.count(year, month);
        double average = (double) signnum / usernum;

        SignCount signCount = new SignCount();
        signCount.setUsernum(usernum);
        signCount.setSignnum(signnum);
        signCount.setAverage(average);

        resultData.setData(signCount);
        resultData.setCode(Code.SUCCESS);
        return resultData;
    }

    public Result test() {
        User user = userMapper.selectByPrimaryKey(10000);
        return countsign(user, 2017, 7);
    }

    public Result testSession(User user, String name, Integer uid) {
        Result result = new Result();
        return result;
    }
}

