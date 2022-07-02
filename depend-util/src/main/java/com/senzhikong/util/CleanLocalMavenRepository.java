package com.senzhikong.util;

import java.io.File;

/**
 * @author Shu.zhou
 * @date 2019年3月1日下午2:38:55
 */
public class CleanLocalMavenRepository {

    /**
     * 仓库根目录
     */
    static String root = "D:\\Develop\\maven-jar";

    public static void main(String[] args) {

        File file = new File(root);

        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                validate(f);
            }
        }

    }

    public static boolean validate(File file) {
        boolean isHaveJar = false;
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            // 判断是否有*jar 是否是有文件夹
            for (File f : files) {
                if (f.getName()
                        .endsWith(".jar")) {
                    isHaveJar = true;
                }
                if (f.getName()
                        .endsWith(".pom")) {
                    isHaveJar = true;
                }
                if (f.getName()
                        .endsWith(".war")) {
                    isHaveJar = true;
                }
                if (f.isDirectory()) {
                    boolean isNextHaveJar = validate(f);
                    if (isNextHaveJar) {
                        isHaveJar = true;
                    }
                }
            }
        }
        if (!isHaveJar) {
            delete(file);
        }
        return isHaveJar;
    }

    public static void delete(File file) {
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isDirectory()) {
                    delete(f);
                }
                f.delete();
            }
        } else {
            file.delete();
        }
    }
}
