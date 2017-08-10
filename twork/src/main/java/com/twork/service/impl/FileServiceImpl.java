package com.twork.service.impl;

import com.twork.dao.FileMapper;
import com.twork.dao.ShareFileMapper;
import com.twork.pojo.File;
import com.twork.pojo.ShareFile;
import com.twork.pojo.User;
import com.twork.service.FileService;
import com.twork.util.Code;
import com.twork.util.FileDownload;
import com.twork.util.FileUpload;
import com.twork.util.FinalVariable;
import com.twork.vo.PictureUrl;
import com.twork.vo.Result;
import com.twork.vo.ResultData;
import com.twork.vo.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private ShareFileMapper shareFileMapper;

    @Override
    public Result upload(User user, MultipartFile multipartFile, Integer chunk, Integer chunks, String time, String name) {
        String fileName = user.getUid() + "@" + time + "@" + name;
        ResultData resultData = (ResultData) FileUpload.upload(multipartFile, chunk, chunks, fileName);
        Result result = new Result();

        if (resultData.getCode() == Code.FILE_UPLOADING) {
            result.setCode(Code.FILE_UPLOADING);
            return result;
        }

        if (resultData.getCode() != Code.SUCCESS) {
            result.setCode(Code.UPLOAD_FILE_FAILED);
            return result;
        }

        File file = (File) resultData.getData();

        File f = fileMapper.selectByPrimaryKey(file.getHash());
        if (f != null) {
            f.setCount((short) (f.getCount() + 1));
            int row = fileMapper.updateByPrimaryKey(f);
            if (row != 1) {
                result.setCode(Code.UPLOAD_FILE_FAILED);
                return result;
            }
        } else {
            int row = fileMapper.insert(file);
            if (row != 1) {
                result.setCode(Code.UPLOAD_FILE_FAILED);
                return result;
            }
        }

        ShareFile shareFile = new ShareFile();
        shareFile.setUid(user.getUid());
        shareFile.setName(name);
        shareFile.setHash(file.getHash());
        int row = shareFileMapper.insert(shareFile);
        if (row != 1) {
            result.setCode(Code.UPLOAD_FILE_FAILED);
            return result;
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result upload(User user, MultipartFile multipartFile) {
        String name = multipartFile.getOriginalFilename();
        String fileName = user.getUid() + "@" + new Date().getTime() + "@" + name;
        ResultData resultData = (ResultData) FileUpload.upload(multipartFile, fileName);
        ResultMsg resultMsg = new ResultMsg();

        if (resultData.getCode() != Code.SUCCESS) {
            resultMsg.setCode(Code.UPLOAD_FILE_FAILED);
            return resultMsg;
        }

        File file = (File) resultData.getData();

        File f = fileMapper.selectByPrimaryKey(file.getHash());
        if (f == null) {
            file.setCount((short) 0);
            int row = fileMapper.insert(file);
            if (row != 1) {
                resultMsg.setCode(Code.UPLOAD_FILE_FAILED);
                return resultMsg;
            }
        } else {
            f.setCount((short) (f.getCount() + 1));
            int row = fileMapper.updateByPrimaryKey(f);
            if (row != 1) {
                resultMsg.setCode(Code.UPLOAD_FILE_FAILED);
                return resultMsg;
            }
        }

        String src = FinalVariable.ORIGIN + "/api/file/downloadimg?hash=" + file.getHash();

        resultMsg.setCode(0);
        resultMsg.setData(new PictureUrl(src));
        return resultMsg;
    }

    @Override
    public void download(User user, String hash, HttpServletResponse response) {
        ShareFile shareFile = shareFileMapper.selectByPrimaryKey(user.getUid(), hash);
        if (shareFile == null) {
            return;
        }

        String fileName = shareFile.getName();

        File file = fileMapper.selectByPrimaryKey(hash);

        String filePath = FinalVariable.uploadDirectory + java.io.File.separator + file.getPath();
        FileDownload.download(response, fileName, filePath);
    }

    @Override
    public void downloadimg(User user, String hash, HttpServletResponse response) {
        File file = fileMapper.selectByPrimaryKey(hash);

        if (file == null) {
            return;
        }

        String filePath = FinalVariable.uploadDirectory + java.io.File.separator + file.getPath();
        FileDownload.download(response, file.getHash() + ".jpg", filePath);
    }

    @Override
    public Result list(User user) {
        ResultData resultData = new ResultData();

        List<ShareFile> fileList = shareFileMapper.listchecked();
        resultData.setData(fileList);
        resultData.setCode(Code.SUCCESS);
        return resultData;
    }

    @Override
    public Result listnonchecked(User user) {
        ResultData resultData = new ResultData();

        List<ShareFile> fileList = shareFileMapper.listnochecked();
        resultData.setData(fileList);
        resultData.setCode(Code.SUCCESS);
        return resultData;
    }

    @Override
    public Result delete(User user, String hash) {
        Result result = new Result();

        ShareFile shareFile = shareFileMapper.selectByPrimaryKey(user.getUid(), hash);
        if (shareFile == null) {
            result.setCode(Code.FILE_NOT_EXIST);
            return result;
        }

        int row = shareFileMapper.deleteByPrimaryKey(user.getUid(), hash);
        if (row != 1) {
            row = shareFileMapper.deleteByPrimaryKey(user.getUid(), hash);
            if (row != 1) {
                result.setCode(Code.DELETE_FILE_FAILED);
                return result;
            }
        }

        File f = fileMapper.selectByPrimaryKey(hash);
        if (f != null) {
            short count = f.getCount();
            if (count > 1) {
                f.setCount((short) (count - 1));
                row = fileMapper.updateByPrimaryKey(f);
                if (row != 1) {
                    result.setCode(Code.DELETE_FILE_FAILED);
                    return result;
                }
            } else {
                System.out.println("需要删除文件" + hash);
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }

    @Override
    public Result check(User user, String hash) {
        Result result = new Result();

        ShareFile shareFile = shareFileMapper.selectByPrimaryKey(user.getUid(), hash);
        if (shareFile == null) {
            result.setCode(Code.FILE_NOT_EXIST);
            return result;
        }

        shareFile.setStatus(true);
        int row = shareFileMapper.updateByPrimaryKey(shareFile);
        if (row != 1) {
            row = shareFileMapper.updateByPrimaryKey(shareFile);
            if (row != 1) {
                result.setCode(Code.CHECK_FILE_FAILED);
                return result;
            }
        }

        result.setCode(Code.SUCCESS);
        return result;
    }
}
