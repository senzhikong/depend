package com.senzhikong.email;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReceiveEmail {
    private String messageId;
    private String receiveEmail;
    private String fromEmail;
    private String fromName;
    private String subject;
    private boolean isRead;
    private boolean replySign;
    private String textMsg;
    private String htmlMsg;
    private List<String> toList;
    private String priority;
    private String priorityDesc;
    private Integer size;
    private Date sendTime;
    private JSONArray attachmentFiles;
}
