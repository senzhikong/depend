package com.senzhikong.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * https工具类
 *
 * @author Shu.zhou
 */
public class HttpsUtils {

    // private static Log logger = LogFactory.getLog(HttpsUtils.class);

    /**
     * 发起https请求并获取结果
     *
     * @param url 请求地址
     * @throws Exception
     */
    public static String get(String url) {
        String body = "";
        HttpEntity entity = null;
        CloseableHttpClient httpClient = HttpConnectionManager.getHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                httpGet.abort();
                return null;
            }
            entity = response.getEntity();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 释放链接
            try {
                httpGet.releaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发起https请求并获取结果
     *
     * @param url 请求地址
     * @throws Exception
     */
    public static String post(String url) {
        String body = "";
        HttpEntity entity = null;
        CloseableHttpClient httpClient = HttpConnectionManager.getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            // 接收数据
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                httpPost.abort();
                return null;
            }
            entity = response.getEntity();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 释放链接
            try {
                httpPost.releaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 发起https请求并获取结果
     *
     * @param url 请求地址
     * @throws Exception
     */
    public static String delete(String url) {
        String body = "";
        HttpEntity entity = null;
        CloseableHttpClient httpClient = HttpConnectionManager.getHttpClient();
        HttpDelete httpDelete = new HttpDelete(url);
        try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                httpDelete.abort();
                return null;
            }
            entity = response.getEntity();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 释放链接
            try {
                httpDelete.releaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发起https请求并获取结果
     *
     * @param url 请求地址
     * @return json String字符串
     * @throws Exception
     */
    public static String postWithJson(String url, Object jsonObject) {
        String body = "";
        HttpEntity entity = null;
        CloseableHttpClient httpClient = HttpConnectionManager.getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求参数
        httpPost.setHeader(ContentType.HEAD_CONTENTYPE, ContentType.APPLICATION_JSON_UTF_8);
        StringEntity params = new StringEntity(jsonObject.toString(), "UTF-8");
        httpPost.setEntity(params);
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                httpPost.abort();
                return null;
            }
            entity = response.getEntity();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 释放链接
            try {
                httpPost.releaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发起https请求并获取结果
     *
     * @param url 请求地址
     * @return json String字符串
     * @throws Exception
     */
    public static String postWithJson(String url, Object jsonObject, String charset) {
        String body = "";
        HttpEntity entity = null;
        CloseableHttpClient httpClient = HttpConnectionManager.getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求参数
        httpPost.setHeader(ContentType.HEAD_CONTENTYPE, ContentType.APPLICATION_JSON_UTF_8);
        StringEntity params = new StringEntity(jsonObject.toString(), charset);
        httpPost.setEntity(params);
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                httpPost.abort();
                return null;
            }
            entity = response.getEntity();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 释放链接
            try {
                httpPost.releaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发起https请求并获取结果
     *
     * @param url 请求地址
     * @return json String字符串
     * @throws Exception
     */
    public static String putWithJson(String url, Object jsonObject) {
        String body = "";
        HttpEntity entity = null;
        CloseableHttpClient httpClient = HttpConnectionManager.getHttpClient();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader(ContentType.HEAD_CONTENTYPE, ContentType.APPLICATION_JSON_UTF_8);
        StringEntity params = new StringEntity(jsonObject.toString(), "utf-8");
        httpPut.setEntity(params);
        try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                httpPut.abort();
                return null;
            }
            entity = response.getEntity();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 释放链接
            try {
                httpPut.releaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发起https请求并获取结果
     *
     * @param url 请求地址
     * @return builder
     * @throws Exception
     */
    public static String postWithForm(String url, MultipartEntityBuilder builder) {
        String body = "";
        HttpEntity entity = null;
        CloseableHttpClient httpClient = HttpConnectionManager.getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求参数
        httpPost.setHeader("Connection", "Keep-Alive");
        httpPost.setHeader("Cache-Control", "no-cache");
        httpPost.addHeader("Connection", "keep-alive");
        httpPost.addHeader("Accept", "*/*");
        httpPost.addHeader("Content-Type", "multipart/form-data;boundary=------------------------bb611e0a6f2b3caf");
        httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        httpPost.setEntity(builder.build());
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine()
                    .getStatusCode() != 200) {
                httpPost.abort();
                return null;
            }
            entity = response.getEntity();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 释放链接
            try {
                httpPost.releaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
