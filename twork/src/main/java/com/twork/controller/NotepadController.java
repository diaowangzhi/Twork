package com.twork.controller;

import com.twork.pojo.User;
import com.twork.service.NotepadService;
import com.twork.util.Check;
import com.twork.util.Code;
import com.twork.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@SessionAttributes("user")
@RequestMapping("/api/note")
public class NotepadController {

    @Autowired
    private NotepadService notepadService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(@SessionAttribute(value = "user", required = false) User user,
                      @RequestParam(value = "uid", required = false) Integer uid,
                      @RequestParam(value = "content", required = false) String content,
                      @RequestParam(value = "reminddatetime", required = false) String datetime) {
        Date reminddatetime = "".equals(datetime) ? null : new Date(Long.parseLong(datetime));
        uid = uid != null ? uid : user.getUid();
        Result result = Check.checkParameter(user, uid, content);
        return result.getCode() != Code.SUCCESS ? result : notepadService.add(user, uid, content, reminddatetime);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@SessionAttribute(value = "user", required = false) User user,
                         @RequestParam(value = "id", required = false) Integer id) {
        Result result = Check.checkParameter(user, id);
        return result.getCode() != Code.SUCCESS ? result : notepadService.delete(user, id);
    }

    @RequestMapping(value = "change", method = RequestMethod.POST)
    @ResponseBody
    public Result change(@SessionAttribute(value = "user", required = false) User user,
                         @RequestParam(value = "id", required = false) Integer id,
                         @RequestParam(value = "content", required = false) String content) {
        Result result = Check.checkParameter(user, id, content);
        return result.getCode() != Code.SUCCESS ? result : notepadService.change(user, id, content);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public Result list(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : notepadService.list(user);
    }
}
