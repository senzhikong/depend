package com.senzhikong.sms.aliyun;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;

public class AliyunSms {

    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static AliyunSmsResponse sendSms(AliyunSmsRequest request) throws Exception {

        Client acsClient = createClient(request.getAccessKeyId(), request.getAccessKeySecret());
        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest re = new SendSmsRequest();
        re.setPhoneNumbers(request.getPhoneNumbers());
        re.setSignName(request.getSignName());
        re.setTemplateCode(request.getTemplateCode());
        re.setTemplateParam(request.getTemplateParam());
        re.setSmsUpExtendCode(request.getSmsUpExtendCode());
        re.setOutId(request.getOutId());
        // hint 此处可能会抛出异常，注意catch
        SendSmsResponse res = acsClient.sendSms(re);
        SendSmsResponseBody body = res.getBody();
        AliyunSmsResponse response = new AliyunSmsResponse();
        response.setBizId(body.getBizId());
        response.setCode(body.getCode());
        response.setRequestId(body.getRequestId());
        response.setMessage(body.getMessage());
        return response;
    }


}
