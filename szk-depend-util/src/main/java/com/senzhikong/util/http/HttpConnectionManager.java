package com.senzhikong.util.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * httpclient连接池工具类
 *
 * @author Shu.zhou
 */
public class HttpConnectionManager {

    private static Log logger = LogFactory.getLog(HttpConnectionManager.class);

    private static PoolingHttpClientConnectionManager clientConnectionManager = null;
    private static CloseableHttpClient httpClient = null;
    private static Object syncLock = new Object();

    static {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new AnyTrustStrategy()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            sslsf = new SSLConnectionSocketFactory(sslContext);
        } catch (Exception e) {
            logger.error(e);
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslsf)
                        .register("http",
                                PlainConnectionSocketFactory.getSocketFactory())
                        .build();
        clientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        clientConnectionManager.setMaxTotal(400);
        clientConnectionManager.setDefaultMaxPerRoute(200);
    }

    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
                    httpClient = HttpClients.custom()
                            .setConnectionManager(clientConnectionManager)
                            .build();
                }
            }
        }
        return httpClient;
    }

}
