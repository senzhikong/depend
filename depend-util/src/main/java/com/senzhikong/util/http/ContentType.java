package com.senzhikong.util.http;

import javax.ws.rs.core.MediaType;
import java.io.File;

public class ContentType {

    public static final String HEAD_CONTENTYPE = "Content-Type";
    public static final String APPLICATION_JSON_UTF_8 =
            MediaType.APPLICATION_JSON + "; " + MediaType.CHARSET_PARAMETER + "=UTF-8";
    public static final String APPLICATION_JSON_ENCODING_UTF_8 = "application/json; encoding=utf-8";
    public static final String TEXT_XML_UTF_8 = MediaType.TEXT_XML + "; " + MediaType.CHARSET_PARAMETER + "=UTF-8";
    public static final String TEXT_PLAIN_UTF_8 = MediaType.TEXT_PLAIN + "; " + MediaType.CHARSET_PARAMETER + "=UTF-8";
    public static final String TEXT_PLAIN = MediaType.TEXT_PLAIN;
    public static final String IMAGE_JPEG = "image/jpeg";

    public static String get(String fileName) {
        String end = fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("html".equalsIgnoreCase(end)) {
            return "text/html";
        } else if ("ico".equalsIgnoreCase(end)) {
            return "image/x-icon";
        } else if ("jpg".equalsIgnoreCase(end) || "jpeg".equalsIgnoreCase(end) || "jpe".equalsIgnoreCase(end)) {
            return "image/jpeg";
        } else if ("mp3".equalsIgnoreCase(end)) {
            return "audio/mp3";
        } else if ("wav".equalsIgnoreCase(end)) {
            return "audio/wav";
        } else if ("wma".equalsIgnoreCase(end)) {
            return "audio/x-ms-wma";
        } else if ("mp4".equalsIgnoreCase(end)) {
            return "video/mpeg4";
        }

        return "application/octet-stream";
    }

    public static String get(File file) {
        return get(file.getName());
    }

    public static String get(UploadFile file) {
        return get(file.getFileName());
    }
}
