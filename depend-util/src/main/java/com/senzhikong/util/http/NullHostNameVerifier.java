package com.senzhikong.util.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author shu.zhou
 * @date 2018年5月17日 下午3:55:58
 */
public class NullHostNameVerifier implements HostnameVerifier {
    /*
     * (non-Javadoc)
     *
     * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String,
     * javax.net.ssl.SSLSession)
     */
    @Override
    public boolean verify(String arg0, SSLSession arg1) {
        return true;
    }
}

