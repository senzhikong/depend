package com.senzhikong.util.http;

/**
 * 文件下载工具类
 *
 * @author zs614
 */
public class UploadUtil {

    /**
     * http协议下载文件
     *
     * @param url
     * @param out
     * @return
     * @throws Exception
     */
    public static ResponseEntity uploadFile(String urlStr, RequestEntity entity) throws Exception {
        return HttpUtil.postForm(urlStr, entity);
    }

    public static ResponseEntity uploadFile(String urlStr, UploadFile file) throws Exception {
        RequestEntity ue = new RequestEntity();
        ue.addFile(file);
        ResponseEntity r = UploadUtil.uploadFile(urlStr, ue);
        return r;
    }


}
