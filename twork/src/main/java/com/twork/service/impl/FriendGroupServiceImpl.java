package com.twork.service.impl;

import com.twork.dao.FriendGroupMapper;
import com.twork.dao.FriendMapper;
import com.twork.dao.UserMapper;
import com.twork.pojo.Friend;
import com.twork.pojo.FriendGroup;
import com.twork.pojo.User;
import com.twork.service.FriendGroupService;
import com.twork.util.Code;
import com.twork.util.UserLevel;
import com.twork.vo.*;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class FriendGroupServiceImpl implements FriendGroupService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    FriendMapper friendMapper;
    @Autowired
    FriendGroupMapper friendGroupMapper;

    @Override
    public Result addgroup(User user, String name, boolean register) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER && !register) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        FriendGroup friendGroup = friendGroupMapper.selectByName(user.getUid(), name);

        if (friendGroup != null) {
            result.setCode(Code.FRIEND_GROUP_ALREADY_EXIST);
            return result;
        }

        friendGroup = new FriendGroup();
        friendGroup.setUid(user.getUid());
        friendGroup.setId(getNewFriendGroupId(user.getUid()));
        friendGroup.setName(name);

        int row = friendGroupMapper.insert(friendGroup);
        if (row != 1) {
            result.setCode(Code.ADD_FRIEND_GROUP_FAILED);
            return result;
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result delgroup(User user, String name) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        FriendGroup friendGroup = friendGroupMapper.selectByName(user.getUid(), name);

        if (friendGroup == null) {
            result.setCode(Code.FRIEND_GROUP_NOT_EXIST);
            return result;
        }

        if (friendGroup.getId() == 1) {
            result.setCode(Code.FRIEND_GROUP_NOT_ALLOW_DELETE);
            return result;
        }

        short id = friendGroup.getId();
        short newId = 1;

        friendMapper.changeFriendGroup(user.getUid(), id, newId);
        int row = friendGroupMapper.deleteByPrimaryKey(user.getUid(), id);
        if (row != 1) {
            row = friendGroupMapper.deleteByPrimaryKey(user.getUid(), id);
            if (row != 1) {
                result.setCode(Code.DELETE_FRIEND_GROUP_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result changegroup(User user, String oldname, String newname) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        FriendGroup friendGroup = friendGroupMapper.selectByName(user.getUid(), newname);
        if (friendGroup != null) {
            result.setCode(Code.FRIEND_GROUP_ALREADY_EXIST);
            return result;
        }

        friendGroup = friendGroupMapper.selectByName(user.getUid(), oldname);

        if (friendGroup == null) {
            result.setCode(Code.FRIEND_GROUP_NOT_EXIST);
            return result;
        }

        friendGroup.setName(newname);
        int row = friendGroupMapper.updateByPrimaryKey(friendGroup);
        if (row != 1) {
            row = friendGroupMapper.updateByPrimaryKey(friendGroup);
            if (row != 1) {
                result.setCode(Code.FRIEND_GROUP_CHANGE_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result listgroup(User user) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        List<FriendGroup> friendGroupList = friendGroupMapper.selectById(user.getUid());

        List<FriendGroupView> friendGroupViewList = new LinkedList<>();

        for (FriendGroup friendGroup : friendGroupList) {
            friendGroupViewList.add(new FriendGroupView(friendGroup));
        }

        resultData.setCode(Code.SUCCESS);
        resultData.setData(friendGroupViewList);
        return resultData;
    }

    @Override
    public Result movefriend(User user, Integer fid, String newname) {
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

        FriendGroup friendGroup = friendGroupMapper.selectByName(user.getUid(), newname);
        if (friendGroup == null) {
            result.setCode(Code.FRIEND_GROUP_NOT_EXIST);
            return result;
        }

        short id = friendGroup.getId();
        if (friend.getId().equals(id)) {
            result.setCode(Code.USER_ALREADY_IN_FRIEND_GROUP);
            return result;
        }

        friend.setId(id);
        int row = friendMapper.updateByPrimaryKey(friend);
        if (row != 1) {
            row = friendMapper.updateByPrimaryKey(friend);
            if (row != 1) {
                result.setCode(Code.MOVE_FRIEND_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result listgroupfriend(User user, String name) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        List<UserView> userViewList = new LinkedList<>();

        List<User> userList = userMapper.listFriendGroupUser(user.getUid(), name);

        for (User u : userList) {
            userViewList.add(new UserView(u));
        }

        resultData.setCode(Code.SUCCESS);
        resultData.setData(userViewList);
        return resultData;
    }

    @Override
    public Result listgroupAndfriend(User user) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        List<FriendGroupAndFriend> friendGroupAndFriendList = new LinkedList<>();

        List<FriendGroup> friendGroupList = friendGroupMapper.selectById(user.getUid());
        for (FriendGroup friendGroup : friendGroupList) {
            List<User> userList = userMapper.listFriendGroupUser(user.getUid(), friendGroup.getName());
            friendGroupAndFriendList.add(new FriendGroupAndFriend(friendGroup, userList));
        }

        resultData.setCode(Code.SUCCESS);
        resultData.setData(friendGroupAndFriendList);
        return resultData;
    }

    @Override
    public Result test() {
        return null;
    }

    private short getNewFriendGroupId(Integer uid) {
        short gid = 0;
        try {
            gid = friendGroupMapper.getLastFriendGroupId(uid);
        } catch (BindingException e) {
            gid = 0;
        } finally {
            gid += 1;
        }
        return gid;
    }
}
