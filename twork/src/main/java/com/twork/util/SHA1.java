package com.twork.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SHA1 {
    public static String SHA1(File file) {
        FileInputStream fileInputStream = null;
        String sha1 = null;
        try {
            fileInputStream = new FileInputStream(file);
            sha1 = DigestUtils.sha1Hex(IOUtils.toByteArray(fileInputStream));
        } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sha1;
    }
}
