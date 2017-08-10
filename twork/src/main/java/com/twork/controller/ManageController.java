package com.twork.controller;

import com.twork.pojo.User;
import com.twork.service.ManageService;
import com.twork.util.Check;
import com.twork.util.Code;
import com.twork.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("user")
@RequestMapping("/api/manager")
public class ManageController {

    @Autowired
    private ManageService manageService;

    @RequestMapping(value = "listuser", method = RequestMethod.POST)
    public @ResponseBody
    Result listuser(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : manageService.listuser(user);
    }

    @RequestMapping(value = "listnoncheckeduser", method = RequestMethod.POST)
    @ResponseBody
    public Result listnoncheckeduser(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : manageService.listnoncheckeduser(user);
    }

    @RequestMapping(value = "changeuserinfo", method = RequestMethod.POST)
    @ResponseBody
    public Result changeuserinfo(@SessionAttribute(value = "user", required = false) User user,
                                 @RequestParam(value = "uid", required = false) Integer uid,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "email", required = false) String email) {
        Result result = Check.checkParameter(user, uid, name, email);
        return result.getCode() != Code.SUCCESS ? result : manageService.changeuserinfo(user, uid, name, email);
    }

    @RequestMapping(value = "changelevel", method = RequestMethod.POST)
    @ResponseBody
    public Result changelevel(@SessionAttribute(value = "user", required = false) User user,
                              @RequestParam(value = "uid", required = false) Integer uid,
                              @RequestParam(value = "level", required = false) int level) {
        Result result = Check.checkParameter(user, uid, level);
        return result.getCode() != Code.SUCCESS ? result : manageService.changelevel(user, uid, level);
    }

    @RequestMapping(value = "delUser", method = RequestMethod.POST)
    @ResponseBody
    public Result delUser(@SessionAttribute(value = "user", required = false) User user,
                          @RequestParam(value = "uid", required = false) Integer uid) {
        Result result = Check.checkParameter(user, uid);
        return result.getCode() != Code.SUCCESS ? result : manageService.delUser(user, uid);
    }

    @RequestMapping(value = "listsign", method = RequestMethod.POST)
    @ResponseBody
    public Result listsign(@SessionAttribute(value = "user", required = false) User user,
                           @RequestParam(value = "uid", required = false) Integer uid,
                           @RequestParam(value = "year", required = false) Integer year,
                           @RequestParam(value = "month", required = false) Integer month) {
        Result result = Check.checkParameter(user, uid, year, month);
        return result.getCode() != Code.SUCCESS ? result : manageService.listsign(user, uid, year, month);
    }

    @RequestMapping(value = "countsign", method = RequestMethod.POST)
    @ResponseBody
    public Result countsign(@SessionAttribute(value = "user", required = false) User user,
                            @RequestParam(value = "year", required = false) Integer year,
                            @RequestParam(value = "month", required = false) Integer month) {
        Result result = Check.checkParameter(user, year, month);
        return result.getCode() != Code.SUCCESS ? result : manageService.countsign(user, year, month);
    }
}
