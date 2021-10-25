package com.senzhikong.sql;

import com.senzhikong.db.entity.BaseEntity;
import com.senzhikong.db.enums.CommonStatus;
import com.senzhikong.db.sql.BaseJpaRepository;
import com.senzhikong.db.sql.wrapper.WrapperService;
import com.senzhikong.spring.SpringContextHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author Shu.zhou
 * @date 2018年9月27日下午3:37:13
 */
@Getter
@Setter
@Slf4j
public class BaseService extends WrapperService implements IBaseService {

    public <T extends BaseEntity> void updateStatus(Class<T> clz, Long[] ids, CommonStatus status) {
        updateStatus(clz, ids, status.code(), status.description());
    }

    @Override
    public <T extends BaseEntity> void updateStatus(Class<T> clz, Long[] ids, String status, String statusDesc) {
        BaseJpaRepository<T, Long> repository = getBaseJpaRepository(clz);
        List<T> list = repository.findByIdIn(ids);
        for (T item : list) {
            item.setStatus(status);
            item.setStatusDesc(statusDesc);
        }
        repository.saveAll(list);
    }

    @Override
    public <T extends BaseEntity,R> R entityToDTO(T t, Class<R> clz) {
        try {
            R dto = clz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(t, clz);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("转化类型失败");
        }
    }

    <T extends BaseEntity> BaseJpaRepository<T, Long> getBaseJpaRepository(Class<T> clz) {
        String clzName = clz.getPackage().getName();
        clzName = clzName.substring(0, clzName.length() - 6);
        clzName = clzName + "repository." + clz.getSimpleName() + "Repository";
        return SpringContextHolder.getBeanByClassName(clzName);
    }


}
