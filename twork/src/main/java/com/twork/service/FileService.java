package com.twork.service;

import com.twork.pojo.User;
import com.twork.vo.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileService {
    Result upload(User user, MultipartFile multipartFile, Integer chunk, Integer chunks, String time, String name);

    Result upload(User user, MultipartFile multipartFile);

    void download(User user, String hash, HttpServletResponse response);

    Result list(User user);

    Result listnonchecked(User user);

    Result delete(User user, String hash);

    Result check(User user, String hash);

    void downloadimg(User user, String hash, HttpServletResponse response);
}
