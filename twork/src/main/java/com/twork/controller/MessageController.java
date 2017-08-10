package com.twork.controller;

import com.twork.dao.MessageMapper;
import com.twork.dao.UserMapper;
import com.twork.pojo.Message;
import com.twork.pojo.User;
import com.twork.service.FriendService;
import com.twork.util.Check;
import com.twork.util.Code;
import com.twork.vo.Result;
import com.twork.vo.UserModel;
import com.twork.websocket.SpringWebSocketHandler;
import com.twork.websocket.message.MessageData;
import com.twork.websocket.message.ResultMassage;
import com.twork.websocket.message.ResultNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/message")
@SessionAttributes("user")
public class MessageController {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendService friendService;

    @Autowired
    private SpringWebSocketHandler springWebSocketHandler;

    @RequestMapping(value = "number", method = RequestMethod.POST)
    @ResponseBody
    public Result number(@SessionAttribute(value = "user", required = false) User user) {

        Result result = Check.checkParameter(user);
        if (result.getCode() != Code.SUCCESS) {
            return result;
        }

        ResultNumber resultNumber = new ResultNumber();
        int number = messageMapper.count(user.getUid());
        if (number < 0) {
            resultNumber.setCode(Code.GET_MESSAGE_NUMBER_FAILED);
            return resultNumber;
        }

        resultNumber.setCode(Code.SUCCESS);
        resultNumber.setNumber(number);
        return resultNumber;
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    @ResponseBody
    public Result message(@SessionAttribute(value = "user", required = false) User user) {

        Result result = Check.checkParameter(user);
        if (result.getCode() != Code.SUCCESS) {
            return result;
        }

        ResultMassage resultMassage = new ResultMassage();
        resultMassage.setPages(1);
        List<MessageData> messageDataList = new ArrayList<>();

        List<Message> messages = messageMapper.selectByUid(user.getUid());
        int i = 1;

        for (Message message : messages) {

            MessageData messageData = new MessageData();

            UserModel mine = new UserModel();
            User applicator = userMapper.selectByPrimaryKey(message.getSid());
            mine.setId(applicator.getUid());
            mine.setUsername(applicator.getName());

            messageData.setId(i);
            messageData.setFrom(message.getSid());
            messageData.setFrom_group(0);
            messageData.setContent(message.getContent());
            messageData.setTime(message.getDatetime());
            messageData.setHref(null);
            messageData.setRead(true);
            messageData.setRemark(null);
            messageData.setType(1);
            messageData.setUid(user.getUid());
            messageData.setUser(mine);

            int index = message.getContent().indexOf("已同意添加你为好友");

            if (index != -1) {
                messageMapper.deleteByPrimaryKey(message.getSid(), message.getRid());
                messageData.setFrom(null);
            }

            messageDataList.add(messageData);
        }
        resultMassage.setData(messageDataList);
        resultMassage.setCode(0);
        return resultMassage;
    }

    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    @ResponseBody
    public Result agree(@SessionAttribute(value = "user", required = false) User user,
                        @RequestParam(value = "uid", required = false) Integer rid) {

        Result result = Check.checkParameter(user);
        if (result.getCode() != Code.SUCCESS) {
            return result;
        }

        result = new Result();

        int sid = user.getUid();
        String content = userMapper.selectByPrimaryKey(sid).getName() + "已同意添加你为好友。";
        Date datetime = new Date();
        Message message = new Message();
        message.setSid(sid);
        message.setRid(rid);
        message.setContent(content);
        message.setDatetime(datetime);
        messageMapper.insert(message);
        messageMapper.deleteByPrimaryKey(rid, sid);

        friendService.addfriend(user, rid);

        springWebSocketHandler.newMessage(rid);
        springWebSocketHandler.addFriend(rid, user.getUid(), user.getName(), "", "/layui/css/modules/layim/skin/logo.jpg", 1);

        result.setCode(0);
        return result;
    }

    @RequestMapping(value = "/disagree", method = RequestMethod.POST)
    @ResponseBody
    public Result disagree(@SessionAttribute(value = "user", required = false) User user,
                           @RequestParam(value = "uid", required = false) Integer sid) {

        Result result = Check.checkParameter(user);
        if (result.getCode() != Code.SUCCESS) {
            return result;
        }

        result = new Result();

        int rid = user.getUid();
        messageMapper.deleteByPrimaryKey(sid, rid);

        result.setCode(0);
        return result;
    }

    @RequestMapping(value = "/read", method = RequestMethod.POST)
    @ResponseBody
    public Result read(@SessionAttribute(value = "user", required = false) User user,
                       @RequestParam(value = "sid", required = false) Integer sid) {

        Result result = Check.checkParameter(user);
        if (result.getCode() != Code.SUCCESS) {
            return result;
        }

        result = new Result();

        int rid = user.getUid();
        messageMapper.deleteByPrimaryKey(sid, rid);

        result.setCode(0);
        return result;
    }
}
