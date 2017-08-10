package com.twork.service.impl;

import com.twork.dao.NotepadMapper;
import com.twork.dao.UserMapper;
import com.twork.pojo.Notepad;
import com.twork.pojo.User;
import com.twork.service.NotepadService;
import com.twork.util.Code;
import com.twork.util.UserLevel;
import com.twork.vo.Result;
import com.twork.vo.ResultData;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotepadServiceImpl implements NotepadService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotepadMapper notepadMapper;

    @Override
    public synchronized Result add(User user, Integer uid, String content, Date reminddatetime) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        if (!uid.equals(user.getUid()) && user.getUid() < UserLevel.POWER_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        User u = userMapper.selectByPrimaryKey(uid);
        if (u == null) {
            result.setCode(Code.USER_NOT_EXIST);
            return result;
        }

        Notepad notepad = new Notepad();
        notepad.setUid(uid);
        notepad.setCid(user.getUid());
        notepad.setId(getNewId(uid));
        notepad.setContent(content);
        notepad.setReminddatetime(reminddatetime);

        int row = notepadMapper.insert(notepad);
        if (row != 1) {
            row = notepadMapper.insert(notepad);
            if (row != 1) {
                result.setCode(Code.ADD_NOTE_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result delete(User user, Integer id) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Notepad notepad = notepadMapper.selectByPrimaryKey(user.getUid(), id);
        if (notepad == null) {
            result.setCode(Code.NOTE_NOT_EXIST);
            return result;
        }

        int row = notepadMapper.deleteByPrimaryKey(id);
        if (row != 1) {
            row = notepadMapper.deleteByPrimaryKey(id);
            if (row != 1) {
                result.setCode(Code.DELETE_NOTE_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result change(User user, Integer id, String content) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Notepad notepad = notepadMapper.selectByPrimaryKey(user.getUid(), id);
        if (notepad == null) {
            result.setCode(Code.NOTE_NOT_EXIST);
            return result;
        }

        notepad.setContent(content);

        int row = notepadMapper.updateByPrimaryKey(notepad);
        if (row != 1) {
            row = notepadMapper.updateByPrimaryKey(notepad);
            if (row != 1) {
                result.setCode(Code.CHANGE_NOTE_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result list(User user) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        List<Notepad> notepadList = notepadMapper.selectByUid(user.getUid());

        resultData.setCode(Code.SUCCESS);
        resultData.setData(notepadList);
        return resultData;
    }

    private Integer getNewId(Integer uid) {
        int id = 0;
        try {
            id = notepadMapper.getLastId(uid);
        } catch (BindingException e) {
            id = 0;
        } finally {
            id += 1;
        }
        return id;
    }
}
