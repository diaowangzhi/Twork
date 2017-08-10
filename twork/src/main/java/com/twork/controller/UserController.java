package com.twork.controller;

import com.twork.pojo.User;
import com.twork.service.PasswordService;
import com.twork.service.SignService;
import com.twork.service.UserService;
import com.twork.util.Check;
import com.twork.util.Code;
import com.twork.vo.Result;
import com.twork.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("user")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private PasswordService passwordService;
    @Autowired
    private UserService userService;
    @Autowired
    private SignService signService;

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public Result register(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "password", required = false) String password,
                           @RequestParam(value = "email", required = false) String email) {
        if (name == null || password == null) {
            ResultData resultData = new ResultData();
            resultData.setCode(Code.PARAMETER_NOT_MATCH);
            return resultData;
        }

        return userService.register(name, password, email);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(HttpSession session,
                        Model model,
                        @RequestParam(value = "uid", required = false) Integer uid,
                        @RequestParam(value = "password", required = false) String password) {
        if (uid == null || password == null) {
            Result result = new Result();
            result.setCode(Code.PARAMETER_NOT_MATCH);
            return result;
        }
        return userService.login(model, uid, password);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public Result logout(@SessionAttribute(value = "user", required = false) User user,
                         SessionStatus sessionStatus) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : userService.logout(user, sessionStatus);
    }

    @RequestMapping(value = "init", method = RequestMethod.GET)
    @ResponseBody
    public Result init(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : userService.init(user);
    }

    @RequestMapping(value = "listinfo", method = RequestMethod.POST)
    @ResponseBody
    public Result listinfo(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : userService.listInfo(user, user.getUid());
    }


    @RequestMapping(value = "changeinfo", method = RequestMethod.POST)
    @ResponseBody
    public Result changeinfo(@SessionAttribute(value = "user", required = false) User user,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "email", required = false) String email) {
        Result result = Check.checkParameter(user, name, email);
        return result.getCode() != Code.SUCCESS ? result : userService.changeinfo(user, name, email);
    }

    @RequestMapping(value = "changepassword", method = RequestMethod.POST)
    @ResponseBody
    public Result changepassword(@SessionAttribute(value = "user", required = false) User user,
                                 @RequestParam(value = "oldpassword", required = false) String oldPassword,
                                 @RequestParam(value = "newpassword", required = false) String newPassword) {

        Result result = Check.checkParameter(user, oldPassword, newPassword);
        return result.getCode() != Code.SUCCESS ? result : passwordService.changepassword(user, oldPassword, newPassword);
    }

    @RequestMapping(value = "sign", method = RequestMethod.POST)
    @ResponseBody
    public Result sign(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : signService.sign(user);
    }

    @RequestMapping(value = "listsigns", method = RequestMethod.POST)
    @ResponseBody
    public Result listsigns(@SessionAttribute(value = "user", required = false) User user,
                            @RequestParam(value = "year", required = false) Integer year,
                            @RequestParam(value = "month", required = false) Integer month) {
        Result result = Check.checkParameter(user, year, month);
        return result.getCode() != Code.SUCCESS ? result : signService.listsigns(user, year, month);
    }

    @RequestMapping(value = "exitgroup", method = RequestMethod.POST)
    @ResponseBody
    public Result exitgroup(@SessionAttribute(value = "user", required = false) User user,
                            @RequestParam(value = "gid", required = false) Integer gid) {
        Result result = Check.checkParameter(user, gid);
        return result.getCode() != Code.SUCCESS ? result : userService.exitgroup(user, gid);
    }

    @RequestMapping(value = "shieldgroup", method = RequestMethod.POST)
    @ResponseBody
    public Result shieldgroup(@SessionAttribute(value = "user", required = false) User user,
                              @RequestParam(value = "gid", required = false) Integer gid) {
        Result result = Check.checkParameter(user, gid);
        return result.getCode() != Code.SUCCESS ? result : userService.shieldgroup(user, gid);
    }

    @RequestMapping(value = "listrecord", method = RequestMethod.POST)
    @ResponseBody
    public Result listrecord(@SessionAttribute(value = "user", required = false) User user,
                             @RequestParam(value = "uid", required = false) Integer uid) {
        Result result = Check.checkParameter(user, uid);
        return result.getCode() != Code.SUCCESS ? result : userService.listrecord(user, uid);
    }
}
