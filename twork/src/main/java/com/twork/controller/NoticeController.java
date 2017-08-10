package com.twork.controller;

import com.twork.pojo.User;
import com.twork.service.NoticeService;
import com.twork.util.Check;
import com.twork.util.Code;
import com.twork.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/system")
@SessionAttributes("user")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @RequestMapping(value = "releasenotice", method = RequestMethod.POST)
    @ResponseBody
    public Result releasenotice(@SessionAttribute(value = "user", required = false) User user,
                                @RequestParam(value = "content", required = false) String content) {
        Result result = Check.checkParameter(user, content);
        return result.getCode() != Code.SUCCESS ? result : noticeService.releasenotice(user, content);
    }

    @RequestMapping(value = "shownotice", method = RequestMethod.POST)
    @ResponseBody
    public Result shownotice(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : noticeService.shownotice(user);
    }

    @RequestMapping(value = "delnotice", method = RequestMethod.POST)
    @ResponseBody
    public Result delnotice(@SessionAttribute(value = "user", required = false) User user,
                            @RequestParam(value = "id", required = false) Integer id) {
        Result result = Check.checkParameter(user, id);
        return result.getCode() != Code.SUCCESS ? result : noticeService.delnotice(user, id);
    }
}