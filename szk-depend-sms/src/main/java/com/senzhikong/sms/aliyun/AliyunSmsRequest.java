package com.senzhikong.sms.aliyun;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Shu.Zhou
 */
@Getter
@Setter
public class AliyunSmsRequest {
    /**
     * 阿里云账号id
     */
    private String accessKeyId;
    /**
     * 阿里云账号密钥
     */
    private String accessKeySecret;
    /**
     * 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
     */
    private String smsUpExtendCode;
    /**
     * 必填:短信签名-可在短信控制台中找到
     */
    private String signName;
    /**
     * 必填:待发送手机号
     */
    private String phoneNumbers;
    /**
     * 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
     */
    private String outId;
    /**
     * 必填:短信模板-可在短信控制台中找到
     */
    private String templateCode;
    /**
     * 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
     */
    private String templateParam;
    private String resourceOwnerAccount;
    private Long resourceOwnerId;

    public static AliyunSmsRequest create() {
        return new AliyunSmsRequest();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
