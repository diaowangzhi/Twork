
package com.twork.service.impl;

import com.twork.dao.FriendMapper;
import com.twork.dao.MessageMapper;
import com.twork.dao.UserMapper;
import com.twork.pojo.Friend;
import com.twork.pojo.Message;
import com.twork.pojo.User;
import com.twork.service.FriendService;
import com.twork.util.Code;
import com.twork.util.UserLevel;
import com.twork.vo.Result;
import com.twork.vo.ResultData;
import com.twork.vo.UserView;
import com.twork.websocket.SpringWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SpringWebSocketHandler springWebSocketHandler;

    @Override
    public Result search(User user, String userinfo) {
        ResultData resultdata = new ResultData();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultdata.setCode(Code.PERMISSION_DENIED);
            return resultdata;
        }

        List<UserView> UserViews = new LinkedList<>();

        List<User> userList = userMapper.selectByUidOrName(userinfo);

        for (User u : userList) {
            UserViews.add(new UserView(u));
        }

        resultdata.setCode(Code.SUCCESS);
        resultdata.setData(UserViews);
        return resultdata;
    }

    @Override
    public Result addfriendMessage(User user, Integer fid) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        if (fid.equals(user.getUid())) {
            result.setCode(Code.NOT_ALLOW_ADD_MIME_AS_FRIEND);
            return result;
        }

        User u = userMapper.selectByPrimaryKey(fid);
        if (u == null) {
            result.setCode(Code.USER_NOT_EXIST);
            return result;
        }

        Friend friend = friendMapper.selectByPrimaryKey(user.getUid(), fid);

        if (friend != null) {
            result.setCode(Code.ALREADY_FRIEND);
            return result;
        }

        Message message = messageMapper.selectByPrimaryKey(user.getUid(), fid);
        if (message != null) {
            result.setCode(Code.ALREADY_FRIEND);
            return result;
        }

        message = new Message();
        message.setSid(user.getUid());
        message.setRid(fid);
        message.setContent(user.getUid() + "请求添加您为好友");

        int row = messageMapper.insert(message);

        if (row != 1) {
            result.setCode(Code.ADD_FRIEND_FAILED);
            return result;
        }

        springWebSocketHandler.newMessage(fid);

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result addfriend(User user, Integer fid) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        if (fid.equals(user.getUid())) {
            result.setCode(Code.NOT_ALLOW_ADD_MIME_AS_FRIEND);
            return result;
        }

        User u = userMapper.selectByPrimaryKey(fid);
        if (u == null) {
            result.setCode(Code.USER_NOT_EXIST);
            return result;
        }

        Friend friend = friendMapper.selectByPrimaryKey(user.getUid(), fid);

        if (friend != null) {
            result.setCode(Code.ALREADY_FRIEND);
            return result;
        }

        Short id = 1;
        Date date = new Date();

        friend = new Friend();
        friend.setUid(user.getUid());
        friend.setFid(fid);
        friend.setId(id);
        friend.setDatetime(date);
        int row = friendMapper.insert(friend);

        if (row != 1) {
            result.setCode(Code.ADD_FRIEND_FAILED);
            return result;
        }

        friend = new Friend();
        friend.setUid(fid);
        friend.setFid(user.getUid());
        friend.setId(id);
        friend.setDatetime(date);
        row = friendMapper.insert(friend);
        if (row != 1) {
            // try again
            if (friendMapper.insert(friend) != 1) {
                friendMapper.deleteByPrimaryKey(user.getUid(), fid);
                result.setCode(Code.ADD_FRIEND_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result delfriend(User user, Integer fid) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Friend friend = friendMapper.selectByPrimaryKey(user.getUid(), fid);

        if (friend == null) {
            result.setCode(Code.FRIEND_NOT_EXIST);
            return result;
        }

        int row = friendMapper.deleteByPrimaryKey(user.getUid(), fid);
        if (row != 1) {
            row = friendMapper.deleteByPrimaryKey(user.getUid(), fid);
            if (row != 1) {
                result.setCode(Code.DELETE_FRIEND_FAILED);
                return result;
            }
        }
        row = friendMapper.deleteByPrimaryKey(fid, user.getUid());
        if (row != 1) {
            row = friendMapper.deleteByPrimaryKey(fid, user.getUid());
            if (row != 1) {
                friendMapper.insert(friend);
                result.setCode(Code.DELETE_FRIEND_FAILED);
                return result;
            }
        }

        springWebSocketHandler.remove(fid, user.getUid(), "friend");
        springWebSocketHandler.remove(user.getUid(), fid, "friend");

        result.setCode(Code.SUCCESS);
        return result;
    }
}
