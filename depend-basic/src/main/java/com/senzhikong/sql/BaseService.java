package com.senzhikong.sql;

import com.senzhikong.db.repository.BaseJpaRepository;
import com.senzhikong.db.sql.wrapper.OrderByType;
import com.senzhikong.db.sql.wrapper.PagerQueryWrapper;
import com.senzhikong.db.sql.wrapper.WrapperService;
import com.senzhikong.db.sql.wrapper.WrapperValue;
import com.senzhikong.dto.PagerRequestDTO;
import com.senzhikong.entity.BaseEntity;
import com.senzhikong.enums.CommonStatus;
import com.senzhikong.exception.DataException;
import com.senzhikong.spring.SpringContextHolder;
import com.senzhikong.util.string.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    @Override
    public <T extends BaseEntity> void updateStatus(Class<T> clz, Long[] ids, CommonStatus status) {
        updateStatus(clz, ids, status.getCode(), status.getDescription());
    }

    @Override
    public <T extends BaseEntity> void updateStatus(Class<T> clz, Long[] ids, String status, String statusDesc) {
        if (ids == null || ids.length == 0) {
            return;
        }
        BaseJpaRepository<T, Long> repository = getBaseJpaRepository(clz);
        List<T> list = repository.findByIdIn(ids);
        if (list == null || list.isEmpty()) {
            return;
        }
        for (T item : list) {
            item.setStatus(status);
            item.setStatusDesc(statusDesc);
        }
        repository.saveAll(list);
    }

    @Override
    public <T extends BaseEntity, R> R entityToDTO(T t, Class<R> clz) {
        try {
            R dto = clz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(t, dto);
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

    public void checkStringNull(String obj, String err) {
        if (StringUtils.isBlank(obj)) {
            throw new DataException(err);
        }
    }

    public void checkNull(Object obj, String err) {
        if (obj == null) {
            throw new DataException(err);
        }
    }

    public void throwError(String err) {
        throw new DataException(err);
    }

    public void checkNull(Object obj, RuntimeException err) {
        if (obj == null) {
            throw err;
        }
    }

    public void setPageInfo(PagerQueryWrapper<?> wrapper, PagerRequestDTO requestDTO) {
        wrapper.setPageNumber(requestDTO.getPageNumber());
        wrapper.setPageSize(requestDTO.getPageSize());
        wrapper.setPage(requestDTO.getPage());
        if (StringUtils.isNotEmpty(requestDTO.getOrderBy())) {
            if (StringUtil.equalsIgnoreCase(OrderByType.DESC.toString(), requestDTO.getOrderType())) {
                wrapper.orderBy(WrapperValue.col(requestDTO.getOrderBy()), OrderByType.DESC);
            } else {
                wrapper.orderBy(WrapperValue.col(requestDTO.getOrderBy()), OrderByType.ASC);
            }
        }
    }
}
