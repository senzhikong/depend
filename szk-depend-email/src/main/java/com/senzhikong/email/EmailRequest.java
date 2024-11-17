package com.senzhikong.email;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
