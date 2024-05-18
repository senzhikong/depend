package com.senzhikong.util.http;

import java.io.File;

/**
 * @author shu
 */
public class ContentType {

    public static final String HEAD_CONTENTYPE = "Content-Type";
    public static final String APPLICATION_JSON_UTF_8 =
            MediaType.APPLICATION_JSON + "; " + MediaType.CHARSET_PARAMETER + "=UTF-8";
    public static final String APPLICATION_JSON_ENCODING_UTF_8 = "application/json; encoding=utf-8";
    public static final String TEXT_XML_UTF_8 = MediaType.TEXT_XML + "; " + MediaType.CHARSET_PARAMETER + "=UTF-8";
    public static final String TEXT_PLAIN_UTF_8 = MediaType.TEXT_PLAIN + "; " + MediaType.CHARSET_PARAMETER + "=UTF-8";
    public static final String TEXT_PLAIN = MediaType.TEXT_PLAIN;
    public static final String IMAGE_JPEG = "image/jpeg";

    private static final String HTML = "html";
    private static final String ICO = "ico";
    private static final String JPG = "jpg";
    private static final String JPEG = "jpeg";
    private static final String JPE = "jpe";
    private static final String MP3 = "mp3";
    private static final String WAV = "wav";
    private static final String WMA = "wma";
    private static final String MP4 = "mp4";

    public static String get(String fileName) {
        String end = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (HTML.equalsIgnoreCase(end)) {
            return "text/html";
        } else if (ICO.equalsIgnoreCase(end)) {
            return "image/x-icon";
        } else if (JPEG.equalsIgnoreCase(end) || JPG.equalsIgnoreCase(end) || JPE.equalsIgnoreCase(end)) {
            return "image/jpeg";
        } else if (MP3.equalsIgnoreCase(end)) {
            return "audio/mp3";
        } else if (WAV.equalsIgnoreCase(end)) {
            return "audio/wav";
        } else if (WMA.equalsIgnoreCase(end)) {
            return "audio/x-ms-wma";
        } else if (MP4.equalsIgnoreCase(end)) {
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
