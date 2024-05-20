package com.senzhikong.entity;

import com.alibaba.fastjson.JSON;
import com.senzhikong.enums.CommonStatus;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.domain.Example;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shu
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    protected static final long serialVersionUID = 1L;

    public void updateStatus(CommonStatus status) {
        this.setStatus(status.code());
        this.setStatusDesc(status.desc());

    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }


    public <T> Example<T> example() {
        return (Example<T>) Example.of(this);
    }


    public void initialize() {
        initialize(null);
    }

    public void initialize(Long accountId) {
        this.setId(null);
        this.setStatus(CommonStatus.NORMAL.code());
        this.setStatusDesc(CommonStatus.NORMAL.desc());
        this.setCreateBy(accountId);
        this.setCreateTime(new Date());
        this.setUpdateBy(accountId);
        this.setUpdateTime(new Date());
    }

    public void setUpdateInfo(Long accountId) {
        this.setUpdateBy(accountId);
        this.setUpdateTime(new Date());
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public abstract void setId(Long id);

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public abstract void setStatus(String status);

    /**
     * 设置状态描述
     *
     * @param statusDesc 状态描述
     */
    public abstract void setStatusDesc(String statusDesc);

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public abstract void setCreateTime(Date createTime);

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public abstract void setCreateBy(Long createBy);

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public abstract void setUpdateTime(Date updateTime);

    /**
     * 设置更新人
     *
     * @param updateBy 更新人
     */
    public abstract void setUpdateBy(Long updateBy);

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public abstract void setRemark(String remark);
}
