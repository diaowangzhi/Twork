package com.twork.controller;

import com.twork.pojo.User;
import com.twork.service.FileService;
import com.twork.util.Check;
import com.twork.util.Code;
import com.twork.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("user")
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public Result upload(@SessionAttribute(value = "user", required = false) User user,
                         @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                         @RequestParam(value = "chunk", required = false) Integer chunk,
                         @RequestParam(value = "chunks", required = false) Integer chunks,
                         @RequestParam(value = "time", required = false) String time,
                         @RequestParam(value = "name", required = false) String name) {
        Result result = Check.checkParameter(user, multipartFile, chunk, chunks, time, name);
        return result.getCode() != Code.SUCCESS ? result : fileService.upload(user, multipartFile, chunk, chunks, time, name);
    }

    @RequestMapping(value = "pictureupload", method = RequestMethod.POST)
    @ResponseBody
    public Result pictureupload(@SessionAttribute(value = "user", required = false) User user,
                                @RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        Result result = Check.checkParameter(user, multipartFile);
        return result.getCode() != Code.SUCCESS ? result : fileService.upload(user, multipartFile);
    }

    @RequestMapping(value = "download", method = {RequestMethod.POST, RequestMethod.GET})
    public void download(@SessionAttribute(value = "user", required = false) User user,
                         @RequestParam(value = "hash", required = false) String hash,
                         HttpServletResponse response) {
        Result result = Check.checkParameter(user, hash, response);
        fileService.download(user, hash, response);
    }

    @RequestMapping(value = "downloadimg", method = {RequestMethod.POST, RequestMethod.GET})
    public void downloadimg(@SessionAttribute(value = "user", required = false) User user,
                         @RequestParam(value = "hash", required = false) String hash,
                         HttpServletResponse response) {
        Result result = Check.checkParameter(user, hash, response);
        fileService.downloadimg(user, hash, response);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    public Result list(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : fileService.list(user);
    }

    @RequestMapping(value = "listnonchecked", method = RequestMethod.POST)
    @ResponseBody
    public Result listnonchecked(@SessionAttribute(value = "user", required = false) User user) {
        Result result = Check.checkParameter(user);
        return result.getCode() != Code.SUCCESS ? result : fileService.listnonchecked(user);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@SessionAttribute(value = "user", required = false) User user,
                         @RequestParam(value = "hash", required = false) String hash) {
        Result result = Check.checkParameter(user, hash);
        return result.getCode() != Code.SUCCESS ? result : fileService.delete(user, hash);
    }

    @RequestMapping(value = "check", method = RequestMethod.POST)
    @ResponseBody
    public Result check(@SessionAttribute(value = "user", required = false) User user,
                        @RequestParam(value = "hash", required = false) String hash) {
        Result result = Check.checkParameter(user, hash);
        return result.getCode() != Code.SUCCESS ? result : fileService.check(user, hash);
    }
}
