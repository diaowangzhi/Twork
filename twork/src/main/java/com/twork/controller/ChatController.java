package com.twork.controller;

import com.twork.dao.MessageMapper;
import com.twork.dao.UserMapper;
import com.twork.pojo.User;
import com.twork.service.FriendService;
import com.twork.service.GroupService;
import com.twork.service.UserService;
import com.twork.util.Check;
import com.twork.util.Code;
import com.twork.vo.Result;
import com.twork.vo.ResultMsg;
import com.twork.websocket.SpringWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/chat")
@SessionAttributes("user")
public class ChatController {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    @Autowired
    private SpringWebSocketHandler springWebSocketHandler;

    @RequestMapping(value = "listrecord", method = RequestMethod.GET)
    @ResponseBody
    public Result listrecord(@SessionAttribute(value = "user", required = false) User user,
                             @RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "type", required = false) String type) {

        Result result = Check.checkParameter(user, id, type);
        if (result.getCode() != Code.SUCCESS) {
            return result;
        }

        if ("friend".equals(type)) {
            return userService.listrecord(user, id);
        } else if ("group".equals(type)) {
            return groupService.listrecord(user, id);
        }

        result = new ResultMsg();
        result.setCode(Code.PARAMETER_NOT_MATCH);
        return result;
    }
}
