package com.senzhikong.email;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shu
 */
@Getter
@Setter
public class EmailRequest {

    private boolean isSsl = false;
    private List<String> toList;
    private String fromEmail;
    private String fromName;
    private String fromPassword;
    private String subject;
    private String htmlMsg;
    private String emailHost = null;
    private Integer sendPort = 25;
    private Integer sendSslPort = 465;

    public EmailRequest() {
    }

    public EmailRequest(String fromEmail, String fromName, String fromPassword, String toEmail, String subject,
                        String htmlMsg) {
        this.fromName = fromName;
        this.fromPassword = fromPassword;
        this.subject = subject;
        this.htmlMsg = htmlMsg;
        setFromEmail(fromEmail);
        addToUser(toEmail);
    }

    public void addToUser(String toEmail) {
        if (toList == null) {
            toList = new ArrayList<>();
        }
        toList.add(toEmail);
    }

    private static final String EMAIL_DOMAIN_QQ = "@exmail.qq.com";
    private static final String EMAIL_DOMAIN_SINA_COM = "@sina.com";
    private static final String EMAIL_DOMAIN_SINA_CN = "@sina.cn";
    private static final String EMAIL_DOMAIN_SINA_VIP = "@vip.sina.com";
    private static final String EMAIL_DOMAIN_YAHOO_COM = "@yahoo.com";
    private static final String EMAIL_DOMAIN_YAHOO_COM_CN = "@yahoo.com.cn";

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
        if (StringUtils.isBlank(fromEmail)) {
            return;
        }
        if (fromEmail.endsWith(EMAIL_DOMAIN_QQ)) {
            emailHost = "exmail.qq.com";
            isSsl = true;
        } else if (fromEmail.endsWith(EMAIL_DOMAIN_SINA_COM)) {
            emailHost = "sina.com.cn";
        } else if (fromEmail.endsWith(EMAIL_DOMAIN_SINA_CN)) {
            emailHost = "sina.com";
        } else if (fromEmail.endsWith(EMAIL_DOMAIN_SINA_VIP)) {
            emailHost = "vip.sina.com";
        } else if (fromEmail.endsWith(EMAIL_DOMAIN_YAHOO_COM)) {
            emailHost = "mail.yahoo.com";
        } else if (fromEmail.endsWith(EMAIL_DOMAIN_YAHOO_COM_CN)) {
            emailHost = "mail.yahoo.com.cn";
            sendPort = 587;
        } else {
            emailHost = fromEmail.substring(fromEmail.lastIndexOf("@") + 1);
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
