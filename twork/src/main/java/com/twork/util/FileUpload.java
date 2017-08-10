package com.twork.util;

import com.twork.vo.Result;
import com.twork.vo.ResultData;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUpload {
    private static final int BUFFER_SIZE = 2 * 1024;

    public static Result upload(MultipartFile multipartFile,
                                Integer chunk,
                                Integer chunks,
                                String fileName) {
        ResultData resultData = new ResultData();
        try {
            File tempFile = new File(FinalVariable.tempDirectory, fileName);
            File file = null;
            if (chunks > 1) {
                if (!saveUploadFile(multipartFile.getInputStream(), tempFile, chunk != 0)) {
                    throw new IOException();
                }

                if (isUploadFinish(chunk, chunks)) {
                    String sha1 = SHA1.SHA1(tempFile);
                    file = new File(FinalVariable.uploadDirectory, sha1);
                    if (!file.exists()) {
                        if (!tempFile.renameTo(file)) {
                            throw new IOException();
                        }
                    }
                    com.twork.pojo.File f = new com.twork.pojo.File();
                    f.setHash(sha1);
                    f.setPath(File.separator + sha1);
                    f.setSize((int) file.length());
                    resultData.setData(f);
                    // System.out.println(file.getCanonicalPath());
                } else {
                    resultData.setCode(Code.UPLOAD_FILE_FAILED);
                    return resultData;
                }
            } else {
                multipartFile.transferTo(tempFile);
                String sha1 = SHA1.SHA1(tempFile);
                file = new File(FinalVariable.uploadDirectory, sha1);
                if (!file.exists()) {
                    if (!tempFile.renameTo(file)) {
                        throw new IOException();
                    }
                }
                com.twork.pojo.File f = new com.twork.pojo.File();
                f.setHash(sha1);
                f.setPath(File.separator + sha1);
                f.setSize((int) file.length());
                resultData.setData(f);
                // System.out.println(file.getCanonicalPath());
            }
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
            resultData.setCode(Code.UPLOAD_FILE_FAILED);
            return resultData;
        }

        resultData.setCode(Code.SUCCESS);
        return resultData;
    }

    public static Result upload(MultipartFile multipartFile,
                                String fileName) {
        ResultData resultData = new ResultData();
        try {
            File tempFile = new File(FinalVariable.tempDirectory, fileName);
            File file = null;

            multipartFile.transferTo(tempFile);
            String sha1 = SHA1.SHA1(tempFile);
            file = new File(FinalVariable.uploadDirectory, sha1);
            if (!file.exists()) {
                if (!tempFile.renameTo(file)) {
                    throw new IOException();
                }
            }
            com.twork.pojo.File f = new com.twork.pojo.File();
            f.setHash(sha1);
            f.setPath(File.separator + sha1);
            f.setSize((int) file.length());
            resultData.setData(f);
            // System.out.println(file.getCanonicalPath());

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
            resultData.setCode(Code.UPLOAD_FILE_FAILED);
            return resultData;
        }

        resultData.setCode(Code.SUCCESS);
        return resultData;
    }

    private static boolean saveUploadFile(InputStream in, File file, boolean append) {
        OutputStream out = null;
        boolean result = true;
        try {
            if (file.exists() && append) {
                out = new BufferedOutputStream(new FileOutputStream(file, true), BUFFER_SIZE);
            } else {
                out = new BufferedOutputStream(new FileOutputStream(file), BUFFER_SIZE);
            }

            in = new BufferedInputStream(in, BUFFER_SIZE);

            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            result = false;
            System.out.println(e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean isUploadFinish(Integer chunk, Integer chunks) {
        return chunks - chunk == 1;
    }
}