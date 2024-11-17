package com.senzhikong.express;

/**
 * @author Shu.zhou
 * @date 2018年12月12日上午9:40:42
 */
public class KuaiBaoConfig {

    public static final String API_URL = "https://kop.kuaidihelp.com/api";
    public static final String QUERY_API_NAME = "express.info.get";
    private String appid = "101618";
    private String appsecret = "27068766d205ad1963206a77a9d805f7b2cf6e3a";

    public KuaiBaoConfig(String appid, String appsecret) {
        this.appid = appid;
        this.appsecret = appsecret;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }
}
