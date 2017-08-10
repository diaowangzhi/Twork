
package com.twork.service.impl;

import com.twork.dao.NoticeMapper;
import com.twork.pojo.Notice;
import com.twork.pojo.User;
import com.twork.service.NoticeService;
import com.twork.util.Code;
import com.twork.util.UserLevel;
import com.twork.vo.Result;
import com.twork.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    NoticeMapper noticeMapper;

    @Override
    public Result releasenotice(User user, String content) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.POWER_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Notice notice = new Notice();
        notice.setUid(user.getUid());
        notice.setContent(content);
        notice.setDatetime(new Date());

        int row = noticeMapper.insert(notice);

        if (row != 1) {
            row = noticeMapper.insert(notice);
            if (row != 1) {
                result.setCode(Code.RELEASE_NOTICE_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result shownotice(User user) {
        List<Notice> noticeList = noticeMapper.selectAll();

        ResultData resultdata = new ResultData();
        resultdata.setCode(Code.SUCCESS);
        resultdata.setData(noticeList);
        return resultdata;
    }

    @Override
    public Result delnotice(User user, Integer id) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.POWER_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Notice notice = noticeMapper.selectByPrimaryKey(id);
        if (notice == null) {
            result.setCode(Code.NOTICE_NOT_EXIST);
            return result;
        }

        int row = noticeMapper.deleteByPrimaryKey(id);
        if (row != 1) {
            row = noticeMapper.deleteByPrimaryKey(id);
            if (row != 1) {
                result.setCode(Code.DELETE_NOTICE_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result test() {
        return null;
    }
}
