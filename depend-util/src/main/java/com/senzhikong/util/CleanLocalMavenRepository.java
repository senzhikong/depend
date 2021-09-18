package com.senzhikong.util;

import java.io.File;

/**
 * @author Shu.zhou
 * @date 2019年3月1日下午2:38:55
 */
public class CleanLocalMavenRepository {

    // 仓库根目录
    static String root = "D:\\Develop\\maven-jar";

    public static void main(String[] args) {

        File file = new File(root);

        File[] _files = file.listFiles();
        if (_files != null && _files.length > 0) {
            for (File _file : _files) {
                validate(_file);
            }
        }

    }

    public static boolean validate(File file) {
        boolean isHaveJar = false;
        File[] _files = file.listFiles();
        if (_files != null && _files.length > 0) {
            // 判断是否有*jar 是否是有文件夹
            for (File _file : _files) {
                if (_file.getName()
                        .endsWith(".jar")) {
                    isHaveJar = true;
                }
                if (_file.getName()
                        .endsWith(".pom")) {
                    isHaveJar = true;
                }
                if (_file.getName()
                        .endsWith(".war")) {
                    isHaveJar = true;
                }
                if (_file.isDirectory()) {
                    boolean isNextHaveJar = validate(_file);
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
        File[] _files = file.listFiles();
        if (_files != null && _files.length > 0) {
            for (File _file : _files) {
                if (_file.isDirectory()) {
                    delete(_file);
                }
                _file.delete();
            }
        } else {
            file.delete();
        }
    }
}
