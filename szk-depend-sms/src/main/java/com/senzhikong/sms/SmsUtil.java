package com.senzhikong.sms;

import com.alibaba.fastjson.JSONObject;
import com.senzhikong.sms.aliyun.AliyunSms;
import com.senzhikong.sms.aliyun.AliyunSmsRequest;
import com.senzhikong.sms.aliyun.AliyunSmsResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shu
 */
@Slf4j
public class SmsUtil {
    public static String sendSms(SmsConfig config, JSONObject smsParam, String mobilePhone) {
        if (config.getSmsProvider() == SmsProvider.ALIYUN) {
            return sendAli(config, smsParam, mobilePhone);
        }
        return "不支持该服务商";
    }

    public static String sendAli(SmsConfig config, JSONObject smsParam, String mobilePhone) {
        JSONObject msgConfig = config.getConfig();
        AliyunSmsRequest request = new AliyunSmsRequest();
        request.setAccessKeyId(msgConfig.getString("accessKeyId"));
        request.setAccessKeySecret(msgConfig.getString("accessKeySecret"));
        request.setPhoneNumbers(mobilePhone);
        request.setSignName(msgConfig.getString("signName"));
        request.setTemplateCode(msgConfig.getString("templateCode"));
        request.setTemplateParam(smsParam.toJSONString());
        try {
            log.info(request.toString());
            AliyunSmsResponse response = AliyunSms.sendSms(request);
            log.info(response.toString());
            if (response.isSuccess()) {
                return null;
            } else {
                return response.getMessage();
            }
        } catch (Exception e) {
            return "短信发送失败";
        }
    }

}
