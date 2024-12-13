package com.senzhikong.util.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author shu
 */
@Slf4j
public class HttpUtil {

    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String HTTPS = "https";
    private static final String BOUNDARY = "ZS--------HV2ymHFg03ehbqgZCaKO6jyH";
    private static final Integer CODE_200 = 200;

    public static String getStringByUrl(String url) {
        String result;
        try {
            InputStream in = new URL(url).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String httpByPost(String url, String param) {
        return http(url, param, POST);
    }

    public static String httpByGet(String url, String param) {
        return http(url, param, GET);
    }

    public static String http(String targetUrl, String param, String type) {
        String result;
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            if (targetUrl.startsWith(HTTPS)) {
                useHttps(httpConnection);
            }
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod(type);

            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpConnection.setUseCaches(false);

            httpConnection.connect();
            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(param.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

            if (httpConnection.getResponseCode() != CODE_200) {
                throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
            }

            BufferedReader responseBuffer = new BufferedReader(
                    new InputStreamReader((httpConnection.getInputStream()), StandardCharsets.UTF_8));

            String output;
            StringBuilder sb = new StringBuilder();
            while ((output = responseBuffer.readLine()) != null) {
                sb.append(output);
            }
            httpConnection.disconnect();
            result = sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String http(RequestEntity entity) {
        String result;
        try {
            URL targetUrl = new URL(entity.getUrl());

            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();

            if (entity.getUrl().startsWith(HTTPS)) {
                useHttps(httpConnection);
            }
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setRequestMethod(entity.getMethod());

            Map<String, String> headers = entity.getHeaders();
            headers.putIfAbsent("Connection", "Keep-Alive");
            headers.putIfAbsent("CharSet", "UTF-8");
            headers.putIfAbsent("Content-Type", "application/x-www-form-urlencoded");
            String charset = headers.get("Charset");
            for (Entry<String, String> header : headers.entrySet()) {
                httpConnection.setRequestProperty(header.getKey(), header.getValue());
            }

            OutputStream outputStream = httpConnection.getOutputStream();
            if (entity.getParam() != null) {
                outputStream.write(entity.getParam()
                        .getBytes(charset));
            }
            outputStream.flush();

            if (httpConnection.getResponseCode() != CODE_200) {
                throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
            }

            BufferedReader responseBuffer =
                    new BufferedReader(new InputStreamReader((httpConnection.getInputStream()), charset));

            String output;
            StringBuilder sb = new StringBuilder();
            while ((output = responseBuffer.readLine()) != null) {
                sb.append(output);
            }
            httpConnection.disconnect();
            result = sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static ResponseEntity postForm(RequestEntity entity) throws Exception {
        return postForm(entity.getUrl(), entity);
    }


    /**
     * post请求
     */
    public static ResponseEntity postForm(String urlStr, RequestEntity entity) throws Exception {
        OutputStream out = null;
        InputStream in = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod(POST);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            Map<String, String> headers = entity.getHeaders();
            headers.putIfAbsent("Connection", "Keep-Alive");
            headers.putIfAbsent("CharSet", "UTF-8");
            headers.remove("Content-Type");
            String charset = headers.get("CharSet");
            for (Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
            out = connection.getOutputStream();
            // 1. 处理普通表单域(即形如key = value对)的POST请求
            StringBuilder contentBody = new StringBuilder();
            Map<String, Object> params = entity.getFormFields();
            for (Entry<String, Object> param : params.entrySet()) {
                contentBody.append("\r\n--" + BOUNDARY).append("\r\n")
                        .append("Content-Disposition: form-data; name=\"").append(param.getKey()).append("\"")
                        .append("\r\n\r\n").append(param.getValue());
            }
            out.write(contentBody.toString().getBytes(charset));
            // 2. 处理文件上传
            appendFormFiles(entity, out, charset);
            // 3. 写结尾
            String endBoundary = "\r\n--" + BOUNDARY + "--\r\n";
            out.write(endBoundary.getBytes(charset));
            out.flush();
            in = connection.getInputStream();
            return new ResponseEntity(in);
        } catch (Exception e) {
            throw new Exception("上传失败：" + urlStr, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void appendFormFiles(RequestEntity entity, OutputStream out, String charset) throws IOException {
        for (UploadFile file : entity.getFiles()) {
            byte[] bufferOut = null;
            int bytes = 0;
            if (file.getFileBuffer() != null) {
                bufferOut = file.getFileBuffer();
                bytes = bufferOut.length;
            } else if (file.getFileStream() != null) {
                InputStream stream = file.getFileStream();
                bufferOut = new byte[stream.available()];
                bytes = stream.read(bufferOut);
                stream.close();
            } else if (file.getFile() != null) {
                File f = file.getFile();
                DataInputStream dis = new DataInputStream(new FileInputStream(f));
                bufferOut = new byte[(int) f.length()];
                bytes = dis.read(bufferOut);
                dis.close();
            }
            String boundaryMessage2 = "--" + BOUNDARY + "\r\n" +
                    "Content-Disposition:form-data;name=\"" + file.getFieldName() + "\";" +
                    "filename=\"" + file.getFileName() + "\";" +
                    "filelength=\"" + bytes + "\"" +
                    "\r\n" + "Content-Type:" + ContentType.get(file) +
                    "\r\n\r\n";
            out.write(boundaryMessage2.getBytes(charset));
            // 开始真正向服务器写文件
            assert bufferOut != null;
            out.write(bufferOut, 0, bytes);
            out.write(("\r\n").getBytes(charset));
        }
    }

    public static void useHttps(HttpURLConnection conn) throws Exception {

        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] tm = {new MyX509TrustManager()};
        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
        // 初始化
        sslContext.init(null, tm, null);
        // 获取SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        // 设置当前实例使用的SSLSoctetFactory
        HttpsURLConnection.setDefaultSSLSocketFactory(ssf);
        ((HttpsURLConnection) conn).setSSLSocketFactory(ssf);
    }

    /**
     * 发起https请求并获取结果
     *
     * @param url 请求地址
     * @return json String字符串
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
            if (response.getStatusLine().getStatusCode() != CODE_200) {
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
            log.error(e.getMessage(),e);
            return null;
        } finally {
            // 释放资源
            try {
                if (null != entity) {
                    EntityUtils.consume(entity);
                }
            } catch (IOException ignored) {
            }
            // 释放链接
            try {
                httpPost.releaseConnection();
            } catch (Exception ignored) {
            }
        }
    }

}
