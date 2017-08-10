package com.twork.util;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class FileDownload {

    public static void download(HttpServletResponse response, String fileName, String filePath) {
        try {
            try {
                fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            InputStream inputStream = new FileInputStream(new File(filePath));
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}