package com.senzhikong.util.string;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shu.Zhou
 */
public class UrlUtil {
    private static final String URL_PARAM_SPLIT = "?";
    private static final String PARAM_CONTACT = "&";
    private static final String PARAM_VALUE_CONTACT = "=";

    /**
     * 在指定url后追加参数
     *
     * @param url
     * @param data 参数集合 key = value
     * @return
     */
    public static String generateUrl(String url, Map<String, Object> data) {
        StringBuilder resUrl = new StringBuilder();
        StringBuilder param = new StringBuilder();
        for (String key : data.keySet()) {
            param.append(key).append(PARAM_VALUE_CONTACT).append(data.get(key).toString()).append(PARAM_CONTACT);
        }
        String paramStr = param.toString();
        paramStr = paramStr.substring(0, paramStr.length() - 1);
        if (resUrl.indexOf(URL_PARAM_SPLIT) > -1) {
            resUrl.append(PARAM_CONTACT).append(paramStr);
        } else {
            resUrl.append(URL_PARAM_SPLIT + paramStr);
        }
        return resUrl.toString();
    }

    public static String appendParam(String url, String key, Object value) {
        Map<String, Object> data = getParams(url);
        data.put(key, value);
        String[] urlParts = url.split("\\?");
        url = urlParts[0];
        return generateUrl(url, data);
    }

    public static Map<String, Object> getParams(String url) {
        Map<String, Object> data = new HashMap<>(8);
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
        String[] params = paramStr.split(PARAM_CONTACT);
        for (String param : params) {
            String[] keyValue = param.split(PARAM_VALUE_CONTACT);
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
        url += PARAM_CONTACT;
        String pattern = "(\\?|&)#?" + name + "=[a-zA-Z\\d]*(&)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if (m.find()) {
            return m.group(0)
                    .split(PARAM_VALUE_CONTACT)[1].replace(PARAM_CONTACT, "");
        } else {
            return null;
        }
    }
}
