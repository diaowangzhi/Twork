package com.twork.service.impl;

import com.twork.dao.*;
import com.twork.pojo.*;
import com.twork.service.GroupService;
import com.twork.util.Code;
import com.twork.util.UserLevel;
import com.twork.vo.*;
import com.twork.websocket.SpringWebSocketHandler;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private GroupChatRecordMapper groupChatRecordMapper;

    @Autowired
    private SpringWebSocketHandler springWebSocketHandler;

    @Override
    public synchronized Result add(User user, String name) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.POWER_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Group group = new Group();
        group.setGid(getNewGid());
        group.setUid(user.getUid());
        group.setName(name);
        group.setDatetime(new Date());
        int row = groupMapper.insert(group);

        if (row != 1) {
            result.setCode(Code.CREATE_GROUP_FAILED);
            return result;
        }

        if (addUser(user, group.getGid(), user.getUid()).getCode() != Code.SUCCESS) {
            result.setCode(Code.CREATE_GROUP_FAILED);
            return result;
        }

        springWebSocketHandler.addGroup(user.getUid(), group.getGid(), "/layui/css/modules/layim/skin/logo.jpg", group.getName());

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result change(User user, Integer gid, String name) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.POWER_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Group group = groupMapper.selectByPrimaryKey(gid);

        if (!user.getUid().equals(group.getUid())) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        group.setName(name);
        int row = groupMapper.updateByPrimaryKey(group);
        if (row != 1) {
            result.setCode(Code.CHANGE_GROUP_FAILED);
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

        List<Group> listGroup = groupMapper.listGroup(user.getUid());
        resultData.setData(listGroup);
        resultData.setCode(Code.SUCCESS);
        return resultData;
    }

    @Override
    public Result delete(User user, Integer gid) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Group group = groupMapper.selectByPrimaryKey(gid);

        if (!user.getUid().equals(group.getUid())) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        int row = memberMapper.deleteByGid(gid);
        if (row < 1) {
            result.setCode(Code.DELETE_GROUP_FAILED);
            return result;
        }

        row = groupMapper.deleteByPrimaryKey(gid);
        if (row != 1) {
            result.setCode(Code.DELETE_GROUP_FAILED);
            return result;
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result addUser(User user, Integer gid, Integer uid) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Group group = groupMapper.selectByPrimaryKey(gid);

        if (!user.getUid().equals(group.getUid())) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        if (!uid.equals(user.getUid())) {
            Friend friend = friendMapper.selectByPrimaryKey(user.getUid(), uid);
            if (friend == null) {
                result.setCode(Code.NOT_FRIEND);
                return result;
            }
        }

        User u = userMapper.selectByPrimaryKey(uid);
        if (u == null) {
            result.setCode(Code.USER_NOT_EXIST);
            return result;
        }

        Member member = memberMapper.selectByPrimaryKey(gid, uid);
        if (member != null) {
            result.setCode(Code.USER_ALREADY_IN_GROUP);
            return result;
        }

        member = new Member();
        member.setUid(uid);
        member.setGid(gid);
        member.setBan(false);
        member.setShield(false);
        member.setDatetime(new Date());
        int row = memberMapper.insert(member);
        if (row != 1) {
            result.setCode(Code.ADD_USER_FAILED);
        }

        springWebSocketHandler.addGroup(uid, gid, "/layui/css/modules/layim/skin/logo.jpg", group.getName());

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result delUser(User user, Integer gid, Integer uid) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Group group = groupMapper.selectByPrimaryKey(gid);

        if (!user.getUid().equals(group.getUid())) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }
        Member member = memberMapper.selectByPrimaryKey(gid, uid);
        if (member == null) {
            result.setCode(Code.USER_NOT_IN_GROUP);
            return result;
        }

        int row = memberMapper.deleteByPrimaryKey(gid, uid);
        if (row != 1) {
            result.setCode(Code.DELETE_GROUP_USER_FAILED);
            return result;
        }

        springWebSocketHandler.remove(uid, gid, "group");

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result listUser(User user, Integer gid) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        Member member = memberMapper.selectByPrimaryKey(gid, user.getUid());
        if (member == null) {
            resultData.setCode(Code.USER_NOT_IN_GROUP);
            return resultData;
        }

        List<UserView> userViewList = new LinkedList<>();

        List<User> userList = userMapper.listGroupUser(gid);
        for (User u : userList) {
            userViewList.add(new UserView(u));
        }

        resultData.setCode(Code.SUCCESS);
        resultData.setData(userViewList);
        return resultData;
    }

    @Override
    public Result getmembers(User user, Integer gid) {
        ResultMsg resultMsg = new ResultMsg();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultMsg.setCode(Code.PERMISSION_DENIED);
            return resultMsg;
        }

        Group group = groupMapper.selectByPrimaryKey(gid);
        if (group == null) {
            resultMsg.setCode(Code.GROUP_NOT_EXIST);
            return resultMsg;
        }

        Member member = memberMapper.selectByPrimaryKey(gid, user.getUid());
        if (member == null) {
            resultMsg.setCode(Code.USER_NOT_IN_GROUP);
            return resultMsg;
        }

        User owner = userMapper.selectByPrimaryKey(group.getUid());

        List<User> userList = userMapper.listGroupUser(gid);

        MemberModel groupModel = new MemberModel(owner, userList);

        resultMsg.setCode(0);
        resultMsg.setData(groupModel);
        return resultMsg;
    }

    @Override
    public Result listgroupandmember(User user) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        List<GroupAndMember> groupAndMembersList = new LinkedList<>();

        List<Group> groupList = groupMapper.listGroup(user.getUid());

        for (Group group : groupList) {
            List<User> userList = userMapper.listGroupUser(group.getGid());
            groupAndMembersList.add(new GroupAndMember(group, userList));
        }

        resultData.setCode(Code.SUCCESS);
        resultData.setData(groupAndMembersList);
        return resultData;
    }

    @Override
    public Result listrecord(User user, Integer gid) {
        ResultMsg resultMsg = new ResultMsg();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultMsg.setCode(Code.PERMISSION_DENIED);
            return resultMsg;
        }

        List<GroupChatRecord> chatRecords = groupChatRecordMapper.selectByGid(user.getUid());

        List<Record> recordList = new LinkedList<>();

        for (GroupChatRecord chatRecord : chatRecords) {
            Record record = new Record();
            record.setId(chatRecord.getUid());
            record.setUsername(userMapper.selectByPrimaryKey(chatRecord.getUid()).getName());
            record.setContent(chatRecord.getContent());
            record.setAvatar("/layui/css/modules/layim/skin/logo.jpg");

            recordList.add(record);
        }

        resultMsg.setData(recordList);
        resultMsg.setCode(0);
        return resultMsg;
    }

    private Integer getNewGid() {
        int gid = 0;
        try {
            gid = groupMapper.getLastGid();
        } catch (BindingException e) {
            gid = 9999;
        } finally {
            gid += 1;
        }
        return gid;
    }

    @Override
    public Result test() {
        User user = userMapper.selectByPrimaryKey(10000);
        return listgroupandmember(user);
    }
}

