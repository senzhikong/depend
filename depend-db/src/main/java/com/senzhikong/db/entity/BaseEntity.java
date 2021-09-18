package com.senzhikong.db.entity;

import com.alibaba.fastjson.JSON;
import com.senzhikong.db.enums.CommonStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Example;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    /**
     * @Fields serialVersionUID
     */
    protected static final long serialVersionUID = 1L;

    public void updateStatus(CommonStatus status) {
        this.setStatus(status.code());
        this.setStatusDesc(status.description());

    }

    public String toJSONString() {
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
        this.setStatusDesc(CommonStatus.NORMAL.description());
        this.setCreateBy(accountId);
        this.setCreateTime(new Date());
        this.setUpdateBy(accountId);
        this.setUpdateTime(new Date());
    }

    public void setUpdateInfo(Long accountId) {
        this.setUpdateBy(accountId);
        this.setUpdateTime(new Date());
    }

    public abstract void setId(Long id);

    public abstract void setStatus(String status);

    public abstract void setStatusDesc(String statusDesc);

    public abstract void setCreateTime(Date createTime);

    public abstract void setCreateBy(Long createBy);

    public abstract void setUpdateTime(Date updateTime);

    public abstract void setUpdateBy(Long updateBy);

    public abstract void setRemark(String remark);
}
