package com.senzhikong.sms;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author shu
 */
@Data
public class SmsConfig {
    protected boolean debug;
    protected SmsProvider smsProvider;
    protected JSONObject aliyunConfig;
}
