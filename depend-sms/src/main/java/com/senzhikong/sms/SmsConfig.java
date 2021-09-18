package com.senzhikong.sms;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class SmsConfig {
    protected boolean debug;
    protected SmsProvider smsProvider;
    protected JSONObject aliyunConfig;
}
