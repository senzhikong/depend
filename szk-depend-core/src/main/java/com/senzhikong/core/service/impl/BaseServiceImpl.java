package com.senzhikong.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senzhikong.basic.domain.BaseEntityVO;
import com.senzhikong.basic.dto.PagerParam;
import com.senzhikong.basic.dto.PagerResp;
import com.senzhikong.basic.enums.CommonStatus;
import com.senzhikong.basic.util.CommonUtil;
import com.senzhikong.core.converter.BasePoConverter;
import com.senzhikong.core.entity.BaseEntityPO;
import com.senzhikong.core.service.IBaseService;
import com.senzhikong.spring.SpringContextHolder;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author shu.zhou
 */
public abstract class BaseServiceImpl<PO extends BaseEntityPO, VO extends BaseEntityVO> implements IBaseService<PO, VO> {
    protected BaseMapper<PO> baseMapper;
    protected Class<VO> outClz;
    protected Class<PO> entityClz;
    protected BasePoConverter<PO, VO> poConverter;

    @SuppressWarnings("unchecked")
    public void initClz() {
        if (entityClz == null || outClz == null) {
            Type type = getClass().getGenericSuperclass();
            Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
            entityClz = (Class<PO>) trueType;
            Type trueTypeK = ((ParameterizedType) type).getActualTypeArguments()[1];
            outClz = (Class<VO>) trueTypeK;
        }
    }

    public BaseMapper<PO> getMapper() {
        if (baseMapper != null) {
            return baseMapper;
        }
        initClz();
        String clzName = outClz.getSimpleName();
        String mapperName = clzName.substring(0, 1).toLowerCase() + clzName.substring(1,
                clzName.length() - 2) + "Mapper";
        mapperName = mapperName.substring(0, 1).toLowerCase() + mapperName.substring(1);
        return SpringContextHolder.getBean(mapperName);
    }

    @SuppressWarnings("unchecked")
    public BasePoConverter<PO, VO> getPoConverter() {
        if (poConverter != null) {
            return poConverter;
        }
        initClz();
        String clzName = outClz.getSimpleName();
        String packageName = outClz.getPackage().getName();
        packageName = packageName.substring(0, packageName.lastIndexOf(".")) + ".converter.";
        clzName = CommonUtil.camelToUnderline(clzName);
        clzName = CommonUtil.underlineToCamel(clzName.substring(clzName.indexOf("_", 1)));
        String converterName = packageName + clzName.substring(0, clzName.length() - 2) + "Converter";
        try {
            poConverter = (BasePoConverter<PO, VO>) Mappers.getMapper(Class.forName(converterName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return poConverter;
    }

    @Override
    public VO findById(String id) {
        return getPoConverter().po2Vo(getMapper().selectById(id));
    }

    @Override
    public List<VO> findAll() {
        return this.findList(new QueryWrapper<>());
    }

    @Override
    public List<VO> findList(QueryWrapper<PO> wrapper) {
        return getPoConverter().poList2VoList(getMapper().selectList(wrapper));
    }

    @Override
    public List<VO> findList(List<String> ids) {
        return getPoConverter().poList2VoList(getMapper().selectBatchIds(ids));
    }

    @Override
    public List<VO> findList(String[] ids) {
        return findList(Arrays.asList(ids));
    }

    @Override
    public VO save(VO vo, String createBy) {
        try {
            PO data = getPoConverter().vo2Po(vo);
            data.initialize(createBy);
            getMapper().insert(data);
            return getPoConverter().po2Vo(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<VO> saveList(List<VO> list, String createBy) {
        try {
            List<PO> dataList = getPoConverter().voList2PoList(list);
            for (PO data : dataList) {
                data.initialize(createBy);
                getMapper().insert(data);
            }
            return getPoConverter().poList2VoList(dataList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public VO update(VO vo, String updateBy) {
        try {
            PO data = getMapper().selectById(vo.getId());
            getPoConverter().vo2PoUpdate(vo, data);
            data.setUpdateBy(updateBy);
            data.setUpdateTime(new Date());
            getMapper().updateById(data);
            return getPoConverter().po2Vo(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateList(List<VO> list, String updateBy) {
        try {
            for (VO vo : list) {
                PO data = getMapper().selectById(vo.getId());
                getPoConverter().vo2PoUpdate(vo, data);
                data.setUpdateBy(updateBy);
                data.setUpdateTime(new Date());
                getMapper().updateById(data);
                getPoConverter().po2Vo(data, vo);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByStatus(String[] ids, String updateBy) {
        deleteByStatus(Arrays.asList(ids), updateBy);
    }

    @Override
    public void deleteByStatus(List<String> ids, String updateBy) {
        List<PO> list = getMapper().selectBatchIds(ids);
        for (PO data : list) {
            data.setStatus(CommonStatus.DELETE.code());
            data.setUpdateTime(new Date());
            data.setUpdateBy(updateBy);
            getMapper().updateById(data);
        }
    }


    @Override
    public void deleteByStatus(String id, String updateBy) {
        PO data = getMapper().selectById(id);
        if (data != null) {
            data.setStatus(CommonStatus.DELETE.code());
            data.setUpdateTime(new Date());
            data.setUpdateBy(updateBy);
            getMapper().updateById(data);
        }
    }

    @Override
    public PagerResp<VO> findByPage(PagerParam pager) {
        QueryWrapper<PO> queryWrapper = this.generateWrapper(null, pager.getKeyword());
        return findByPage(pager, queryWrapper);
    }

    @Override
    public PagerResp<VO> findByPage(PagerParam pager, VO vo) {
        QueryWrapper<PO> queryWrapper = this.generateWrapper(vo, pager.getKeyword());
        return findByPage(pager, queryWrapper);
    }

    @Override
    public PagerResp<VO> findByPage(PagerParam pager, QueryWrapper<PO> queryWrapper) {
        PagerResp<VO> response = new PagerResp<>();
        List<PO> dataList;
        if (pager.getPageable() != null && pager.getPageable()) {
            Page<PO> page = Page.of(pager.getPageNumber(), pager.getPageSize());
            page = getMapper().selectPage(page, queryWrapper);
            dataList = page.getRecords();
            response.setTotal(page.getTotal());
            response.setPageNumber(page.getCurrent());
            response.setPageSize(page.getSize());
            response.setTotalPage(page.getPages());
        } else {
            dataList = getMapper().selectList(queryWrapper);
        }
        response.setDataList(getPoConverter().poList2VoList(dataList));
        return response;
    }
}
