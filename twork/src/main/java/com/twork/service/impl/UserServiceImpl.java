package com.twork.service.impl;

import com.twork.dao.*;
import com.twork.pojo.*;
import com.twork.service.FriendGroupService;
import com.twork.service.UserService;
import com.twork.util.Code;
import com.twork.util.UserLevel;
import com.twork.vo.*;
import com.twork.websocket.SpringWebSocketHandler;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SessionAttributes("user")
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordMapper passwordMapper;
    @Autowired
    private FriendGroupMapper friendGroupMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private U2uChatRecordMapper u2uChatRecordMapper;

    @Autowired
    private FriendGroupService friendGroupService;

    @Autowired
    private SpringWebSocketHandler springWebSocketHandler;

    @Override
    public synchronized Result register(String name, String password, String email) {
        ResultData resultData = new ResultData();

        User user = new User();

        int uid = getNewUid();

        user.setUid(uid);
        user.setName(name);
        user.setEmail(email);
        if (uid == 10000) {
            user.setLevel(UserLevel.MANAGER_USER);
        }

        int row = userMapper.insert(user);
        if (row != 1) {
            row = userMapper.insert(user);
            if (row != 1) {
                resultData.setCode(Code.REGIREGIST_FAILED);
                return resultData;
            }
        }

        user = userMapper.selectByPrimaryKey(uid);

        Password passwd = new Password();
        passwd.setUid(user.getUid());
        passwd.setPassword(password);
        row = passwordMapper.insert(passwd);
        if (row != 1) {
            row = passwordMapper.insert(passwd);
            if (row != 1) {
                userMapper.deleteByPrimaryKey(user.getUid());
                resultData.setCode(Code.REGIREGIST_FAILED);
                return resultData;
            }
        }

        Result result = friendGroupService.addgroup(user, "我的好友", true);
        if (result.getCode() != Code.SUCCESS) {
            friendGroupService.addgroup(user, "我的好友", true);
            if (result.getCode() != Code.SUCCESS) {
                userMapper.deleteByPrimaryKey(user.getUid());
                passwordMapper.deleteByPrimaryKey(user.getUid());
                resultData.setCode(Code.REGIREGIST_FAILED);
                return resultData;
            }
        }

        UserView userView = new UserView(user);
        resultData.setCode(Code.SUCCESS);
        resultData.setData(userView);
        return resultData;
    }

    @Override
    public Result login(Model model, Integer uid, String password) {
        ResultData resultData = new ResultData();

        User user = userMapper.selectByPrimaryKey(uid);
        if (user == null) {
            resultData.setCode(Code.USERID_OR_PASSWORD_INCORRECT);
            return resultData;
        }

        if (user.getLevel().equals(UserLevel.UNAVAILABLE_USER)) {
            resultData.setCode(Code.USER_NOT_AVALABLE);
            return resultData;
        }

        if (user.getStatus()) {
            resultData.setCode(Code.ALREADY_LOGIN);
            return resultData;
        }

        if (!password.equals(passwordMapper.selectByPrimaryKey(uid).getPassword())) {
            resultData.setCode(Code.USERID_OR_PASSWORD_INCORRECT);
            return resultData;
        }

        user.setStatus(true);
        userMapper.updateByPrimaryKey(user);
        model.addAttribute("user", user);
        resultData.setData(user);
        resultData.setCode(Code.SUCCESS);
        return resultData;
    }

    @Override
    public Result logout(User user, SessionStatus sessionStatus) {
        Result result = new Result();

        user.setStatus(false);
        user.setLastonlinedatetime(new Date());
        int row = userMapper.updateByPrimaryKey(user);
        if (row != 1) {
            row = userMapper.updateByPrimaryKey(user);
            if (row != 1) {
                row = userMapper.updateByPrimaryKey(user);
                result.setCode(Code.LOGOUT_FAILED);
                return result;
            }
        }

        sessionStatus.setComplete();
        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result init(User user) {
        ResultMsg resultMsg = new ResultMsg();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultMsg.setCode(Code.PERMISSION_DENIED);
            return resultMsg;
        }

        UserInfo userInfo = new UserInfo();
        UserModel mine = new UserModel(user);

        List<FriendModel> friend = new LinkedList<>();
        List<GroupModel> group = new LinkedList<>();

        List<FriendGroup> friendGroupList = friendGroupMapper.selectById(user.getUid());
        for (FriendGroup friendGroup : friendGroupList) {
            List<User> userList = userMapper.listFriendGroupUser(user.getUid(), friendGroup.getName());
            friend.add(new FriendModel(friendGroup, userList));
        }

        List<Group> groupList = groupMapper.listGroup(user.getUid());
        for (Group g : groupList) {
            group.add(new GroupModel(g));
        }

        userInfo.setMine(mine);
        userInfo.setFriend(friend);
        userInfo.setGroup(group);

        resultMsg.setCode(0);
        resultMsg.setData(userInfo);

        return resultMsg;
    }

    @Override
    public Result listInfo(User user, Integer uid) {
        ResultData resultData = new ResultData();

        if (user.getLevel() < UserLevel.ORDINARY_USER && !uid.equals(user.getUid())) {
            resultData.setCode(Code.PERMISSION_DENIED);
            return resultData;
        }

        User u = userMapper.selectByPrimaryKey(uid);
        if (u == null) {
            resultData.setCode(Code.USER_NOT_EXIST);
            return resultData;
        }

        UserView userView = new UserView(u);
        resultData.setCode(Code.SUCCESS);
        resultData.setData(userView);
        return resultData;
    }

    @Override
    public Result changeinfo(User user, String name, String email) {
        Result result = new Result();

        user.setName(name);
        user.setEmail(email);

        int row = userMapper.updateByPrimaryKey(user);
        if (row != 1) {
            result.setCode(Code.CHANGE_USER_INFO_FAILED);
            return result;
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result exitgroup(User user, Integer gid) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Member member = memberMapper.selectByPrimaryKey(gid, user.getUid());
        if (member == null) {
            result.setCode(Code.USER_NOT_IN_GROUP);
            return result;
        }

        int row = memberMapper.deleteByPrimaryKey(gid, user.getUid());
        if (row != 1) {
            row = memberMapper.deleteByPrimaryKey(gid, user.getUid());
            if (row != 1) {
                result.setCode(Code.EXIT_GROUP_FAILED);
                return result;
            }
        }

        springWebSocketHandler.remove(user.getUid(), gid, "group");

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result shieldgroup(User user, Integer gid) {
        Result result = new Result();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            result.setCode(Code.PERMISSION_DENIED);
            return result;
        }

        Member member = memberMapper.selectByPrimaryKey(gid, user.getUid());
        if (member == null) {
            result.setCode(Code.USER_NOT_IN_GROUP);
            return result;
        }

        member.setShield(true);
        int row = memberMapper.updateByPrimaryKey(member);
        if (row != 1) {
            row = memberMapper.updateByPrimaryKey(member);
            if (row != 1) {
                result.setCode(Code.SHIELD_GROUP_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result listrecord(User user, Integer uid) {
        ResultMsg resultMsg = new ResultMsg();

        if (user.getLevel() < UserLevel.ORDINARY_USER) {
            resultMsg.setCode(Code.PERMISSION_DENIED);
            return resultMsg;
        }

        List<U2uChatRecord> chatRecords = u2uChatRecordMapper.selectByUid(user.getUid(), uid);

        List<Record> recordList = new LinkedList<>();

        for (U2uChatRecord chatRecord : chatRecords) {
            Record record = new Record();
            record.setId(chatRecord.getSid());
            record.setUsername(userMapper.selectByPrimaryKey(chatRecord.getSid()).getName());
            record.setContent(chatRecord.getContent());
            record.setAvatar("/layui/css/modules/layim/skin/logo.jpg");

            recordList.add(record);
        }

        resultMsg.setData(recordList);
        resultMsg.setCode(0);
        return resultMsg;
    }

    private Integer getNewUid() {
        int uid = 0;
        try {
            uid = userMapper.getLastUid();
        } catch (BindingException e) {
            uid = 9999;
        } finally {
            uid += 1;
        }
        return uid;
    }
}
