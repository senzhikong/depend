package com.senzhikong.email;

import com.alibaba.fastjson.JSON;
import com.senzhikong.util.string.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class EmailRequest {

    private boolean isSSL = false;
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
        if (toList == null)
            toList = new ArrayList<>();
        toList.add(toEmail);
    }

    public boolean getIsSSL() {
        return isSSL;
    }

    public void setIsSSL(boolean isSSL) {
        this.isSSL = isSSL;
    }

    public List<String> getToList() {
        return toList;
    }

    public void setToList(List<String> toList) {
        this.toList = toList;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
        if (StringUtil.isEmpty(fromEmail))
            return;
        if (fromEmail.endsWith("@exmail.qq.com")) {
            emailHost = "exmail.qq.com";
            isSSL = true;
        } else if (fromEmail.endsWith("@sina.com")) {
            emailHost = "sina.com.cn";
        } else if (fromEmail.endsWith("@sina.cn")) {
            emailHost = "sina.com";
        } else if (fromEmail.endsWith("@vip.sina.com")) {
            emailHost = "vip.sina.com";
        } else if (fromEmail.endsWith("@yahoo.com")) {
            emailHost = "mail.yahoo.com";
        } else if (fromEmail.endsWith("@yahoo.com.cn")) {
            emailHost = "mail.yahoo.com.cn";
            sendPort = 587;
        } else {
            emailHost = fromEmail.substring(fromEmail.lastIndexOf("@") + 1);
        }
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromPassword() {
        return fromPassword;
    }

    public void setFromPassword(String fromPassword) {
        this.fromPassword = fromPassword;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHtmlMsg() {
        return htmlMsg;
    }

    public void setHtmlMsg(String htmlMsg) {
        this.htmlMsg = htmlMsg;
    }

    public String getEmailHost() {
        return emailHost;
    }

    public void setSendHost(String sendHost) {
        this.emailHost = sendHost;
    }

    public Integer getSendPort() {
        return sendPort;
    }

    public void setSendPort(Integer sendPort) {
        this.sendPort = sendPort;
    }

    public Integer getSendSslPort() {
        return sendSslPort;
    }

    public void setSendSslPort(Integer sendSslPort) {
        this.sendSslPort = sendSslPort;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
