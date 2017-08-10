package com.twork.controller;

import com.twork.pojo.User;
import com.twork.service.FriendGroupService;
import com.twork.service.FriendService;
import com.twork.util.Check;
import com.twork.util.Code;
import com.twork.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("user")
@RequestMapping("/api/friend")
public class FriendController {

    @Autowired
    FriendService friendService;
    @Autowired
    FriendGroupService friendGroupService;

    @RequestMapping(value = "search", method = RequestMethod.POST)
    @ResponseBody
    public Result search(@SessionAttribute(value = "user", required = false) User user,
                         @RequestParam(value = "userinfo", required = false) String userinfo) {
        Result result = Check.checkParameter(user, userinfo);
        return result.getCode() != Code.SUCCESS ? result : friendService.search(user, userinfo);
    }

    @RequestMapping(value = "addfriend", method = RequestMethod.POST)
    @ResponseBody
    public Result addfriend(@SessionAttribute(value = "user", required = false) User user,
                            @RequestParam(value = "fid", required = false) Integer fid) {
        Result result = Check.checkParameter(user, fid);
        return result.getCode() != Code.SUCCESS ? result : friendService.addfriendMessage(user, fid);
    }

    @RequestMapping(value = "delfriend", method = RequestMethod.POST)
    @ResponseBody
    public Result delfriend(@SessionAttribute(value = "user", required = false) User user,
                            @RequestParam(value = "fid", required = false) Integer fid) {
        Result result = Check.checkParameter(user, fid);
        return result.getCode() != Code.SUCCESS ? result : friendService.delfriend(user, fid);
    }

    @RequestMapping(value = "addgroup", method = RequestMethod.POST)
    @ResponseBody
    public Result addgroup(@SessionAttribute(value = "user", required = false) User user,
                           @RequestParam(value = "name", required = false) String name) {
        Result result = Check.checkParameter(user, name);
        return result.getCode() != Code.SUCCESS ? result : friendGroupService.addgroup(user, name, false);
    }

    @RequestMapping(value = "delgroup", method = RequestMethod.POST)
    @ResponseBody
    public Result delgroup(@SessionAttribute(value = "user", required = false) User user,
                           @RequestParam(value = "name", required = false) String name) {
        Result result = Check.checkParameter(user, name);
        return result.getCode() != Code.SUCCESS ? result : friendGroupService.delgroup(user, name);
    }

    @RequestMapping(value = "changegroup", method = RequestMethod.POST)
    @ResponseBody
    public Result changegroup(@SessionAttribute(value = "user", required = false) User user,
                              @RequestParam(value = "oldname", required = false) String oldname,
                              @RequestParam(value = "newname", required = false) String newname) {
        Result result = Check.checkParameter(user, oldname, newname);
        return result.getCode() != Code.SUCCESS ? result : friendGroupService.changegroup(user, oldname, newname);
    }

    @RequestMapping(value = "listgroup", method = RequestMethod.POST)
    @ResponseBody
    public Result listgroup(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : friendGroupService.listgroup(user);
    }

    @RequestMapping(value = "movefriend", method = RequestMethod.POST)
    @ResponseBody
    public Result movefriend(@SessionAttribute(value = "user", required = false) User user,
                             @RequestParam(value = "fid", required = false) Integer fid,
                             @RequestParam(value = "newname", required = false) String newname) {
        Result result = Check.checkParameter(user, fid, newname);
        return result.getCode() != Code.SUCCESS ? result : friendGroupService.movefriend(user, fid, newname);
    }

    @RequestMapping(value = "listgroupfriend", method = RequestMethod.POST)
    @ResponseBody
    public Result listgroupfriend(@SessionAttribute(value = "user", required = false) User user,
                                  @RequestParam(value = "name", required = false) String name) {
        Result result = Check.checkParameter(user, name);
        return result.getCode() != Code.SUCCESS ? result : friendGroupService.listgroupfriend(user, name);
    }

    @RequestMapping(value = "listgroupandfriend", method = RequestMethod.POST)
    @ResponseBody
    public Result listgroupandfriend(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : friendGroupService.listgroupAndfriend(user);
    }
}