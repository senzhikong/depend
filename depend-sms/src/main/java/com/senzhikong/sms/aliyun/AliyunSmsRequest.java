package com.senzhikong.sms.aliyun;

import com.alibaba.fastjson.JSON;

/**
 * @author Shu.Zhou
 */
public class AliyunSmsRequest {

    private String accessKeyId;         // 阿里云账号id
    private String accessKeySecret;     // 阿里云账号密钥
    private String smsUpExtendCode;     // 选填-上行短信扩展码(无特殊需求用户请忽略此字段)
    private String signName;            // 必填:短信签名-可在短信控制台中找到
    private String phoneNumbers;        // 必填:待发送手机号
    private String outId;               // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
    private String templateCode;        // 必填:短信模板-可在短信控制台中找到
    private String templateParam;       // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
    private String resourceOwnerAccount;
    private Long resourceOwnerId;

    public static AliyunSmsRequest create() {
        return new AliyunSmsRequest();
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public AliyunSmsRequest setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        return this;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public AliyunSmsRequest setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
        return this;
    }

    public String getSmsUpExtendCode() {
        return smsUpExtendCode;
    }

    public AliyunSmsRequest setSmsUpExtendCode(String smsUpExtendCode) {
        this.smsUpExtendCode = smsUpExtendCode;
        return this;
    }

    public Long getResourceOwnerId() {
        return resourceOwnerId;
    }

    public AliyunSmsRequest setResourceOwnerId(Long resourceOwnerId) {
        this.resourceOwnerId = resourceOwnerId;
        return this;
    }

    public String getSignName() {
        return signName;
    }

    public AliyunSmsRequest setSignName(String signName) {
        this.signName = signName;
        return this;
    }

    public String getResourceOwnerAccount() {
        return resourceOwnerAccount;
    }

    public AliyunSmsRequest setResourceOwnerAccount(String resourceOwnerAccount) {
        this.resourceOwnerAccount = resourceOwnerAccount;
        return this;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public AliyunSmsRequest setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        return this;
    }

    public String getOutId() {
        return outId;
    }

    public AliyunSmsRequest setOutId(String outId) {
        this.outId = outId;
        return this;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public AliyunSmsRequest setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
        return this;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public AliyunSmsRequest setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
