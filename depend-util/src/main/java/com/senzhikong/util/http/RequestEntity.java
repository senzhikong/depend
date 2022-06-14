package com.senzhikong.util.http;

import com.senzhikong.util.string.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestEntity {
    private Map<String, Object> formFields = new HashMap<String, Object>();
    private Map<String, String> headers = new HashMap<String, String>();
    private List<UploadFile> files = new ArrayList<UploadFile>();
    private String url;
    private String method = "GET";
    private String param;

    public void addFormField(String key, Object value) {
        formFields.put(key, value);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void addFile(UploadFile file) {
        files.add(file);
    }

    public void setCharSet(String charset) {
        headers.put("CharSet", charset);
    }

    public void addParam(String key, Object value) {
        if (StringUtil.isEmpty(param)) {
            param = "";
        } else {
            param += "&";
        }
        param += key + "=" + (value == null ? "" : value);

    }

    public Map<String, Object> getFormFields() {
        return formFields;
    }

    public List<UploadFile> getFiles() {
        return files;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
