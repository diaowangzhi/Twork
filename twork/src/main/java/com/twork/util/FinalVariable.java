package com.twork.util;

import java.io.File;

public class FinalVariable {
    public static final String ORIGIN = "http://www.twork.com";
    public static File tempDirectory = new File("D:\\temp");
    public static File uploadDirectory = new File("D:\\temp\\upload");

    static {
        if (!tempDirectory.exists()) {
            tempDirectory.mkdirs();
        }
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
    }
}
