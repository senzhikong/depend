package com.senzhikong.core.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.senzhikong.basic.enums.CommonStatus;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shu
 */
@Data
@FieldNameConstants
public abstract class BaseEntityPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public void initialize() {
        initialize(null);
    }

    public void initialize(String userId) {
        this.setId(null);
        this.setStatus(CommonStatus.NORMAL.code());
        this.setCreateBy(userId);
        this.setCreateTime(new Date());
        this.setUpdateBy(userId);
        this.setUpdateTime(new Date());
    }

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @TableField()
    private String remark;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
