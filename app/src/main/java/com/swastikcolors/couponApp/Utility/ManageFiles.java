package com.swastikcolors.couponApp.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ManageFiles {
    public static void copyFile(File originalFile, File newFile) {
        try {
            FileInputStream var2 = new FileInputStream(originalFile);
            FileOutputStream var3 = new FileOutputStream(newFile);
            byte[] var4 = new byte[1024];
            int var5;
            while ((var5 = var2.read(var4)) > 0) {
                var3.write(var4, 0, var5);
            }
            var2.close();
            var3.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
