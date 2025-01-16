package com.senzhikong.mongo.entity;

import com.senzhikong.basic.enums.CommonStatus;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shu.zhou
 */
@Data
@FieldNameConstants
public class BaseMongoPO implements Serializable {
    @Id
    private String id;

    private String status;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;

    private String remark;

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
}
