package com.senzhikong.util.http;

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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {

    public static final String POST = "POST";
    public static final String GET = "GET";
    private static final String BOUNDARY = "ZS--------HV2ymHFg03ehbqgZCaKO6jyH";

    public static String getStringByUrl(String url) {
        String result = "fail";
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

    public static String http(String targetURL, String param, String type) {
        String result = "fail";
        try {
            URL targetUrl = new URL(targetURL);
            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            if (targetURL.startsWith("https://")) {
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

            if (httpConnection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
            }

            BufferedReader responseBuffer = new BufferedReader(
                    new InputStreamReader((httpConnection.getInputStream()), StandardCharsets.UTF_8));

            String output;
            StringBuffer sb = new StringBuffer();
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
        String result = "fail";
        try {
            URL targetUrl = new URL(entity.getUrl());

            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();

            if (entity.getUrl()
                    .startsWith("https://")) {
                useHttps(httpConnection);
            }
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setRequestMethod(entity.getMethod());

            Map<String, String> headers = entity.getHeaders();
            if (headers.get("Connection") == null)
                headers.put("Connection", "Keep-Alive");
            if (headers.get("Charset") == null)
                headers.put("Charset", "UTF-8");
            if (headers.get("Content-Type") == null)
                headers.put("Content-Type", "application/x-www-form-urlencoded");
            String charset = headers.get("Charset");
            Iterator<Entry<String, String>> itHeader = headers.entrySet()
                    .iterator();
            while (itHeader.hasNext()) {
                Entry<String, String> header = itHeader.next();
                httpConnection.setRequestProperty(header.getKey(), header.getValue());
            }

            OutputStream outputStream = httpConnection.getOutputStream();
            if (entity.getParam() != null)
                outputStream.write(entity.getParam()
                        .getBytes(charset));
            outputStream.flush();

            if (httpConnection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
            }

            BufferedReader responseBuffer =
                    new BufferedReader(new InputStreamReader((httpConnection.getInputStream()), charset));

            String output;
            StringBuffer sb = new StringBuffer();
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
     *
     * @param urlStr
     * @param entity
     * @return
     * @throws Exception
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
            if (headers.get("Connection") == null)
                headers.put("Connection", "Keep-Alive");
            if (headers.get("CharSet") == null)
                headers.put("CharSet", "UTF-8");
            headers.remove("Content-Type");
            String charset = headers.get("CharSet");
            Iterator<Entry<String, String>> itHeader = headers.entrySet()
                    .iterator();
            while (itHeader.hasNext()) {
                Entry<String, String> header = itHeader.next();
                connection.setRequestProperty(header.getKey(), header.getValue());
            }

            out = connection.getOutputStream();

            // 1. 处理普通表单域(即形如key = value对)的POST请求
            StringBuffer contentBody = new StringBuffer();
            Map<String, Object> params = entity.getFormFields();
            Iterator<Entry<String, Object>> it = params.entrySet()
                    .iterator();
            while (it.hasNext()) {
                Entry<String, Object> param = it.next();
                contentBody
                        .append("\r\n--" + BOUNDARY)
                        .append("\r\n")
                        .append("Content-Disposition: form-data; name=\"")
                        .append(param.getKey() + "\"")
                        .append("\r\n\r\n")
                        .append(param.getValue())
                ;
            }
            out.write(contentBody.toString()
                    .getBytes(charset));
            // 2. 处理文件上传
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
                contentBody = new StringBuffer();
                contentBody.append("--" + BOUNDARY)
                        .append("\r\n")
                        .append("Content-Disposition:form-data;name=\"")
                        .append(file.getFieldName() + "\";") // form中field的名称
                        .append("filename=\"")
                        .append(file.getFileName() + "\";") // 上传文件的文件名
                        .append("filelength=\"")
                        .append(bytes + "\"") // 上传文件的文件名
                        .append("\r\n")
                        .append("Content-Type:" + ContentType.get(file))
                        .append("\r\n\r\n");
                String boundaryMessage2 = contentBody.toString();
                out.write(boundaryMessage2.getBytes(charset));
                // 开始真正向服务器写文件

                out.write(bufferOut, 0, bytes);
                out.write(("\r\n").getBytes(charset));
            }
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
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public static void main(String[] args) {
        RequestEntity re = new RequestEntity();
        // re.setMethod(GET);
        re.setUrl("https://www.sojson.com/open/api/lunar/json.shtml?date=2019-01-25");
        re.setParam("");
        re.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        re.addHeader("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3610.2 Safari/537.36");
        re.addHeader("Content-Type", "application/json;charset=UTF-8");
        // re.addHeader("", "");
        // re.addHeader("", "");
        // re.addHeader("", "");
        // re.addHeader("", "");
        // re.addHeader("", "");
//        String res = HttpUtil.http(re);
        // String res = HttpUtil.httpByGet("http://www.sojson.com/open/api/lunar/json.shtml?", "");
    }
}
