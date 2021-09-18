package com.senzhikong.util.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * http工具类
 *
 * @author Shu.zhou
 */
public class HttpUtils {

    /**
     * 基于http协议get请求
     *
     * @param url请求地址
     * @return
     */
    public static String get(String url) {
        String body = "";
        HttpEntity entity = null;
        CloseableHttpClient client = HttpConnectionManager.getHttpClient();
        HttpGet get = new HttpGet(url);
        try (CloseableHttpResponse response = client.execute(get)) {
            entity = response.getEntity();
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                get.abort();
                return null;
            }
            if (entity != null) {
                // 按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, "UTF-8");
            }
            return body;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 释放资源
            try {
                if (null != entity) {
                    EntityUtils.consume(entity);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            // 释放链接
            try {
                get.releaseConnection();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 基于http协议get请求
     *
     * @param url请求地址
     * @param code指定编码
     * @return
     */
    public static String get(String url, String code) {
        String result = "";
        HttpEntity entity = null;
        CloseableHttpClient client = HttpConnectionManager.getHttpClient();
        HttpGet get = new HttpGet(url);
        try (CloseableHttpResponse response = client.execute(get)) {
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                get.abort();
                return null;
            }
            entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换结果实体为String类型
                result = EntityUtils.toString(entity, code);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 释放资源
            try {
                if (null != entity) {
                    EntityUtils.consume(entity);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            // 释放链接
            try {
                get.releaseConnection();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
     * 基于http协议post请求
     *
     * @param url请求地址
     * @return
     */
    public static String post(String url) {
        String result = "";
        HttpEntity entity = null;
        CloseableHttpClient client = HttpConnectionManager.getHttpClient();
        HttpPost post = new HttpPost(url);
        try (CloseableHttpResponse response = client.execute(post)) {
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                post.abort();
                return null;
            }
            entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换结果实体为String类型
                result = EntityUtils.toString(entity, "UTF-8");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 释放资源
            try {
                if (null != entity) {
                    EntityUtils.consume(entity);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            // 释放链接
            try {
                post.releaseConnection();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 基于http协议post请求
     *
     * @param url请求地址
     * @param code指定编码
     * @return
     */
    public static String post(String url, String code) {
        String result = "";
        HttpEntity entity = null;
        CloseableHttpClient client = HttpConnectionManager.getHttpClient();
        HttpPost post = new HttpPost(url);
        try (CloseableHttpResponse response = client.execute(post)) {
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                post.abort();
                return null;
            }
            entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换结果实体为String类型
                result = EntityUtils.toString(entity, code);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 释放资源
            try {
                if (null != entity) {
                    EntityUtils.consume(entity);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            // 释放链接
            try {
                post.releaseConnection();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 基于http协议post请求
     *
     * @param url请求地址
     * @param jsonObject请求实体
     * @return
     */
    public static String postWithJson(String url, Object jsonObject) {
        String result = "";
        HttpEntity entity = null;
        CloseableHttpClient client = HttpConnectionManager.getHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/json");
        StringEntity params = new StringEntity(jsonObject.toString(), "utf-8");
        post.setEntity(params);
        try (CloseableHttpResponse response = client.execute(post)) {
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                post.abort();
                return null;
            }
            entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换结果实体为String类型
                result = EntityUtils.toString(entity, "UTF-8");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 释放资源
            try {
                if (null != entity) {
                    EntityUtils.consume(entity);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            // 释放链接
            try {
                post.releaseConnection();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 基于http协议post请求
     *
     * @param url请求地址
     * @param jsonObject请求实体
     * @param code指定编码
     * @return
     */
    public static String postWithJson(String url, JSONObject jsonObject, String code) {
        String result = "";
        HttpEntity entity = null;
        CloseableHttpClient client = HttpConnectionManager.getHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/json");
        StringEntity params = new StringEntity(jsonObject.toString(), code);
        post.setEntity(params);
        try (CloseableHttpResponse response = client.execute(post)) {
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                post.abort();
                return null;
            }
            entity = response.getEntity();
            if (entity != null) {
                // 按指定编码转换结果实体为String类型
                result = EntityUtils.toString(entity, code);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // 释放资源
            try {
                if (null != entity) {
                    EntityUtils.consume(entity);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            // 释放链接
            try {
                post.releaseConnection();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
