package com.senzhikong.db.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseDTO implements Serializable {
    protected static final long serialVersionUID = 1L;
    private Long id;
    private String status;
    private String statusDesc;
    private Date createTime;
    private Long createBy;
    private Date updateTime;
    private Long updateBy;
}
