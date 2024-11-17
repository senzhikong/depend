package com.senzhikong.util.http;

import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * @author shu.zhou
 * @date 2018年5月17日 下午3:33:55
 */
public class MyX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}
