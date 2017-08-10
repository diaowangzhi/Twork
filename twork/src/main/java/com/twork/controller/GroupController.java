package com.twork.controller;

import com.twork.pojo.User;
import com.twork.service.GroupService;
import com.twork.util.Check;
import com.twork.util.Code;
import com.twork.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("user")
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(@SessionAttribute(value = "user", required = false) User user,
                      @RequestParam(value = "name", required = false) String name) {
        Result result = Check.checkParameter(user, name);
        return result.getCode() != Code.SUCCESS ? result : groupService.add(user, name);
    }

    @RequestMapping(value = "change", method = RequestMethod.POST)
    @ResponseBody
    public Result change(@SessionAttribute(value = "user", required = false) User user,
                         @RequestParam(value = "gid", required = false) Integer gid,
                         @RequestParam(value = "name", required = false) String name) {
        Result result = Check.checkParameter(user, name);
        return result.getCode() != Code.SUCCESS ? result : groupService.change(user, gid, name);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public Result list(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : groupService.list(user);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@SessionAttribute(value = "user", required = false) User user,
                         @RequestParam(value = "gid", required = false) Integer gid) {
        Result result = Check.checkParameter(user, gid);
        return result.getCode() != Code.SUCCESS ? result : groupService.delete(user, gid);
    }

    @RequestMapping(value = "adduser", method = RequestMethod.POST)
    @ResponseBody
    public Result adduser(@SessionAttribute(value = "user", required = false) User user,
                          @RequestParam(value = "gid", required = false) Integer gid,
                          @RequestParam(value = "uid", required = false) Integer uid) {
        Result result = Check.checkParameter(user, gid, uid);
        return result.getCode() != Code.SUCCESS ? result : groupService.addUser(user, gid, uid);
    }

    @RequestMapping(value = "deluser", method = RequestMethod.POST)
    @ResponseBody
    public Result deluser(@SessionAttribute(value = "user", required = false) User user,
                          @RequestParam(value = "gid", required = false) Integer gid,
                          @RequestParam(value = "uid", required = false) Integer uid) {
        Result result = Check.checkParameter(user, gid, uid);
        return result.getCode() != Code.SUCCESS ? result : groupService.delUser(user, gid, uid);
    }

    @RequestMapping(value = "listuser", method = RequestMethod.POST)
    @ResponseBody
    public Result listuser(@SessionAttribute(value = "user", required = false) User user,
                           @RequestParam(value = "gid", required = false) Integer gid) {
        Result result = Check.checkParameter(user, gid);
        return result.getCode() != Code.SUCCESS ? result : groupService.listUser(user, gid);
    }

    @RequestMapping(value = "getmembers", method = RequestMethod.GET)
    @ResponseBody
    public Result getmembers(@SessionAttribute(value = "user", required = false) User user,
                             @RequestParam(value = "id", required = false) Integer gid) {
        Result result = Check.checkParameter(user, gid);
        return result.getCode() != Code.SUCCESS ? result : groupService.getmembers(user, gid);
    }

    @RequestMapping(value = "listgroupandmember", method = RequestMethod.POST)
    @ResponseBody
    public Result listgroupandmember(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : groupService.listgroupandmember(user);
    }

    @RequestMapping(value = "listrecord", method = RequestMethod.POST)
    @ResponseBody
    public Result listrecord(@SessionAttribute(value = "user", required = false) User user,
                             @RequestParam(value = "gid", required = false) Integer gid) {
        Result result = Check.checkParameter(user, gid);
        return result.getCode() != Code.SUCCESS ? result : groupService.listrecord(user, gid);
    }
}
