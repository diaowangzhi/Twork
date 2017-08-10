package com.twork.controller;

import com.twork.dao.UserMapper;
import com.twork.pojo.User;
import com.twork.service.GroupService;
import com.twork.service.ManageService;
import com.twork.util.FileDownload;
import com.twork.util.FileUpload;
import com.twork.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Controller
@SessionAttributes("user")
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    UserMapper userMapper;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ManageService manageService;

    @RequestMapping(value = "testGroup", method = RequestMethod.GET)
    public @ResponseBody
    Result testGroup(@RequestParam("name") String name) {
        return groupService.test();
    }

    @RequestMapping(value = "testManager", method = RequestMethod.GET)
    public @ResponseBody
    Result testManager(@RequestParam("name") String name) {
        return manageService.test();
    }

    @RequestMapping(value = "testSession", method = RequestMethod.GET)
    public @ResponseBody
    Result testSession(@SessionAttribute(required = false) User user, @RequestParam("name") String name, @RequestParam("uid") int uid) {

        System.out.println("****************:" + user);

        return manageService.testSession(user, name, uid);
    }


    /*@RequestMapping(value = "testFile", method = RequestMethod.POST)
    public @ResponseBody
    Result testFile(@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        String tmppath = System.getProperty("java.io.tmpdir");
        File directory = null;
        File tmpfile = null;
        try {
            directory = new File(tmppath);
            System.out.println(directory);
            tmpfile = File.createTempFile("twork_", ".tmp", directory);

            multipartFile.transferTo(tmpfile);
            System.out.println(tmpfile.getCanonicalPath());

            FileInputStream fileInputStream = new FileInputStream(tmpfile);
            String sha1 = DigestUtils.sha1Hex(IOUtils.toByteArray(fileInputStream));
            IOUtils.closeQuietly(fileInputStream);
            System.out.println(sha1);

            File file = new File(tmppath, sha1);
            if (!tmpfile.renameTo(file)) {
                throw new IOException();
            }
            System.out.println(file.getCanonicalPath());
        } catch (IOException e) {
            tmpfile.deleteOnExit();
            System.out.println(e);
            e.printStackTrace();
        }

        return null;
    }*/

    @RequestMapping(value = "testFile", method = RequestMethod.POST)
    public @ResponseBody
    Result testFile(@SessionAttribute(value = "user", required = false) User user,
                    @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                    @RequestParam(value = "chunk", required = false) Integer chunk,
                    @RequestParam(value = "chunks", required = false) Integer chunks,
                    @RequestParam(value = "time", required = false) String time,
                    @RequestParam(value = "name", required = false) String name) {
        user = userMapper.selectByPrimaryKey(10000);

        String fileName = user.getUid() + "@" + time + "@" + name;

        Result result = FileUpload.upload(multipartFile, chunk, chunks, fileName);

        return result;
    }

    @RequestMapping(value = "testFileDownload", method = RequestMethod.POST)
    public void testFileDownload(@RequestParam("hash") String hash,
                                 HttpServletResponse response) {

        String fileName = "测试文件.zip";
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String filePath = "D:\\Downloads\\Chrome\\websocket.zip";
        FileDownload.download(response, fileName, filePath);
    }
}
