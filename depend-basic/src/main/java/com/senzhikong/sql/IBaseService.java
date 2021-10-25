package com.senzhikong.sql;

import com.senzhikong.db.entity.BaseEntity;
import com.senzhikong.db.enums.CommonStatus;
import com.senzhikong.db.sql.wrapper.IWrapperService;
/**
 * @author Shu.Zhou
 */
public interface IBaseService extends IWrapperService {

    <T extends BaseEntity> void updateStatus(Class<T> clz, Long[] ids, CommonStatus status);

    <T extends BaseEntity> void updateStatus(Class<T> clz, Long[] ids, String status, String statusDesc);

    <T extends BaseEntity, R> R entityToDTO(T t, Class<R> clz);
}
