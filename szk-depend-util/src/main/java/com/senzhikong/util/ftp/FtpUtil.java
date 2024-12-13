package com.senzhikong.util.ftp;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author shu
 */
@Slf4j
@Getter
@Setter
public class FtpUtil {
    /**
     * ftp服务器地址
     */
    public String hostname;
    /**
     * ftp服务器端口号默认为21
     */
    public Integer port;
    /**
     * ftp登录账号
     */
    public String username;
    /**
     * ftp登录密码
     */
    public String password;
    public FTPClient ftpClient = null;

    public FtpUtil(String hostname, Integer port, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * 初始化ftp服务器
     */
    public void initFtpClient() {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            // 连接ftp服务器
            ftpClient.connect(hostname, port);
            // 登录ftp服务器
            ftpClient.login(username, password);
            // 是否成功登录服务器
            int replyCode = ftpClient.getReplyCode();
            if (FTPReply.isPositiveCompletion(replyCode)) {
                log.debug("ftp connect success:{}:{}", hostname, port);
            } else {
                log.debug("ftp connect fail:{}:{}", hostname, port);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 上传文件
     *
     * @param pathname       ftp服务保存地址
     * @param fileName       上传到ftp的文件名
     * @param originFilename 待上传文件的名称（绝对地址） *
     */
    public boolean uploadFile(String pathname, String fileName, String originFilename) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(originFilename);
            return uploadFile(pathname, fileName, inputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    /**
     * 上传文件
     *
     * @param pathname    ftp服务保存地址
     * @param fileName    上传到ftp的文件名
     * @param inputStream 输入文件流
     */
    public boolean uploadFile(String pathname, String fileName, InputStream inputStream) {
        try {
            initFtpClient();
            ftpClient.enterLocalPassiveMode();
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            createDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            log.debug(
                    "ftp upload file:" + pathname + File.separator + fileName + ",size:" + inputStream.available());
            boolean isSuccess = ftpClient.storeFile(fileName, inputStream);
            ftpClient.logout();
            log.debug("ftp upload res：" + isSuccess);
            return isSuccess;
        } catch (Exception e) {

            log.error(e.getMessage(), e);
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                    log.error(e.getMessage(), e);
                }
            }
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {

                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 改变目录路径
     */
    public boolean changeWorkingDirectory(String directory) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
        } catch (IOException ioe) {
            log.error(ioe.getMessage(), ioe);
        }
        return flag;
    }

    /**
     * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
     */
    public void createDirectory(String remote) throws IOException {
        String directory = remote + File.separator;
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!File.separator.equalsIgnoreCase(directory) && !changeWorkingDirectory(directory)) {
            int start = 0;
            int end;
            if (directory.startsWith(File.separator)) {
                start = 1;
            }
            end = directory.indexOf(File.separator, start);
            StringBuilder path = new StringBuilder();
            do {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"),
                        StandardCharsets.ISO_8859_1);
                path.append(File.separator).append(subDirectory);
                if (!existFile(path.toString())) {
                    if (makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        changeWorkingDirectory(subDirectory);
                    }
                } else {
                    changeWorkingDirectory(subDirectory);
                }
                start = end + 1;
                end = directory.indexOf(File.separator, start);
                // 检查所有目录是否创建完毕
            } while (end > start);
        }
    }

    /**
     * 判断ftp服务器文件是否存在
     */
    public boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 创建目录
     *
     * @param dir 目录
     * @return 是否创建成功
     */
    public boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
        } catch (Exception e) {

            log.error(e.getMessage(), e);
        }
        return flag;
    }

    /**
     * 下载文件 *
     *
     * @param pathname  FTP服务器文件目录 *
     * @param filename  文件名称 *
     * @param localPath 下载后的文件路径 *
     */
    public boolean downloadFile(String pathname, String filename, String localPath) {
        boolean flag = false;
        OutputStream os = null;
        try {
            initFtpClient();
            // 切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    File localFile = new File(localPath + File.separator + file.getName());
                    os = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), os);
                    os.close();
                }
            }
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {

            log.error(e.getMessage(), e);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {

                    log.error(e.getMessage(), e);
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {

                    log.error(e.getMessage(), e);
                }
            }
        }
        return flag;
    }

    /**
     * 删除文件 *
     *
     * @param pathname FTP服务器保存目录 *
     * @param filename 要删除的文件名称 *
     */
    public boolean deleteFile(String pathname, String filename) {
        boolean flag = false;
        try {
            initFtpClient();
            // 切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.dele(filename);
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {

            log.error(e.getMessage(), e);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {

                    log.error(e.getMessage(), e);
                }
            }
        }
        return flag;
    }

}
