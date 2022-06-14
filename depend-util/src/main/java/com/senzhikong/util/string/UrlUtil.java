package com.senzhikong.util.string;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shu.Zhou
 */
public class UrlUtil {

    /**
     * 在指定url后追加参数
     *
     * @param url
     * @param data 参数集合 key = value
     * @return
     */
    public static String generateUrl(String url, Map<String, Object> data) {
        String newUrl = url;
        StringBuffer param = new StringBuffer();
        for (String key : data.keySet()) {
            param.append(key + "=" + data.get(key)
                    .toString() + "&");
        }
        String paramStr = param.toString();
        paramStr = paramStr.substring(0, paramStr.length() - 1);
        if (newUrl.indexOf("?") >= 0) {
            newUrl += "&" + paramStr;
        } else {
            newUrl += "?" + paramStr;
        }
        return newUrl;
    }

    public static String appendParam(String url, String key, Object value) {
        Map<String, Object> data = getParams(url);
        data.put(key, value);
        String[] urlParts = url.split("\\?");
        url = urlParts[0];
        return generateUrl(url, data);
    }

    public static Map<String, Object> getParams(String url) {
        Map<String, Object> data = new HashMap<>();
        if (StringUtil.isEmpty(url)) {
            return data;
        }
        String[] urlParts = url.split("\\?");
        //没有参数
        if (urlParts.length == 1) {
            return data;
        }
        String paramStr = urlParts[1];
        //有参数
        String[] params = paramStr.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            data.put(keyValue[0], keyValue[1]);
        }
        return data;
    }

    /**
     * 获取指定url中的某个参数
     *
     * @param url
     * @param name
     * @return
     */
    public static String getParamByUrl(String url, String name) {
        url += "&";
        String pattern = "(\\?|&){1}#{0,1}" + name + "=[a-zA-Z0-9]*(&{1})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if (m.find()) {
            return m.group(0)
                    .split("=")[1].replace("&", "");
        } else {
            return null;
        }
    }
}
