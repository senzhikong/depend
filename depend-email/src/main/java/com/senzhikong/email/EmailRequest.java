package com.senzhikong.email;

import com.alibaba.fastjson.JSON;
import com.senzhikong.util.string.StringUtil;
import lombok.Getter;
import lombok.Setter;

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

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
        if (StringUtil.isEmpty(fromEmail)) {
            return;
        }
        if (fromEmail.endsWith("@exmail.qq.com")) {
            emailHost = "exmail.qq.com";
            isSsl = true;
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
