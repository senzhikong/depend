package com.senzhikong.sql;

import com.senzhikong.db.sql.wrapper.IWrapperService;
import com.senzhikong.entity.BaseEntity;
import com.senzhikong.enums.CommonStatus;

/**
 * @author Shu.Zhou
 */
public interface IBaseService extends IWrapperService {
    /**
     * 更新状态
     *
     * @param clz    类
     * @param ids    主键
     * @param status 状态
     * @param <T>    泛型
     */
    <T extends BaseEntity> void updateStatus(Class<T> clz, Long[] ids, CommonStatus status);

    /**
     * 更新状态
     *
     * @param clz        类
     * @param ids        主键
     * @param status     状态
     * @param statusDesc 状态描述
     * @param <T>        泛型
     */
    <T extends BaseEntity> void updateStatus(Class<T> clz, Long[] ids, String status, String statusDesc);

    /**
     * 实体转DTO
     *
     * @param t   实体
     * @param clz DTO类目
     * @param <T> 实体类泛型
     * @param <R> DTO泛型
     * @return DTO
     */
    <T extends BaseEntity, R> R entityToDTO(T t, Class<R> clz);
}
