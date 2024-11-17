package com.senzhikong.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.senzhikong.util.EnumUtil;
import lombok.Data;

/**
 * @author shu
 */
@Data
public class SmsConfig {
    private boolean debug;
    private SmsProvider smsProvider;
    private JSONObject config;

    public SmsConfig(String initString) {
        JSONObject smsConfig = JSON.parseObject(initString);
        this.setDebug(smsConfig.getBoolean("debug"));
        this.setSmsProvider(EnumUtil.check(SmsProvider.class, smsConfig.getString("provider")));
        this.setConfig(smsConfig.getJSONObject("config"));
    }
}
