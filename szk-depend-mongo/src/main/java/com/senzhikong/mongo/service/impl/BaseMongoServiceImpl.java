package com.senzhikong.mongo.service.impl;

import com.senzhikong.basic.domain.BaseEntityVO;
import com.senzhikong.basic.dto.PagerParam;
import com.senzhikong.basic.dto.PagerResp;
import com.senzhikong.basic.enums.CommonStatus;
import com.senzhikong.basic.util.CommonUtil;
import com.senzhikong.mongo.converter.BaseMongoPoConverter;
import com.senzhikong.mongo.entity.BaseMongoPO;
import com.senzhikong.mongo.service.IBaseMongoService;
import com.senzhikong.spring.SpringContextHolder;
import jakarta.annotation.Resource;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author shu.zhou
 */
public abstract class BaseMongoServiceImpl<PO extends BaseMongoPO, VO extends BaseEntityVO> implements IBaseMongoService<PO, VO> {
    protected MongoRepository<PO, String> baseMongoRepository;
    protected Class<VO> outClz;
    protected Class<PO> entityClz;
    protected BaseMongoPoConverter<PO, VO> poConverter;
    @Resource
    private MongoTemplate mongoTemplate;


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

    public MongoRepository<PO, String> getRepository() {
        if (baseMongoRepository != null) {
            return baseMongoRepository;
        }
        initClz();
        String clzName = outClz.getSimpleName();
        String mapperName = clzName.substring(0, 1).toLowerCase() + clzName.substring(1, clzName.length() - 2) + "MongoRepository";
        mapperName = mapperName.substring(0, 1).toLowerCase() + mapperName.substring(1);
        return SpringContextHolder.getBean(mapperName);
    }

    @SuppressWarnings("unchecked")
    public BaseMongoPoConverter<PO, VO> getPoConverter() {
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
            poConverter = (BaseMongoPoConverter<PO, VO>) Mappers.getMapper(Class.forName(converterName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return poConverter;
    }

    @Override
    public VO findById(String id) {
        return getPoConverter().po2Vo(getRepository().findById(id).orElse(null));
    }

    @Override
    public List<VO> findAll() {
        return getPoConverter().poList2VoList(getRepository().findAll());
    }

    @Override
    public VO findOne(VO vo) {
        List<VO> list = findList(vo);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public VO findOne(VO vo, String keyword) {
        List<VO> list = findList(vo, keyword);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<VO> findList(List<String> ids) {
        return getPoConverter().poList2VoList(getRepository().findAllById(ids));
    }

    @Override
    public List<VO> findList(String[] ids) {
        return findList(Arrays.asList(ids));
    }

    @Override
    public List<VO> findList(VO vo) {
        return findList(vo, null);
    }

    @Override
    public List<VO> findList(VO vo, String keyword) {
        initClz();
        Query query = this.generateQuery(vo, null);
        List<PO> list = mongoTemplate.find(query, entityClz);
        return getPoConverter().poList2VoList(list);
    }

    @Override
    public VO save(VO vo, String createBy) {
        try {
            PO data = getPoConverter().vo2Po(vo);
            data.initialize(createBy);
            data = getRepository().insert(data);
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
            }
            dataList = getRepository().saveAll(dataList);
            return getPoConverter().poList2VoList(dataList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public VO update(VO vo, String updateBy) {
        try {
            PO data = getRepository().findById(vo.getId()).orElse(null);
            assert data != null;
            getPoConverter().vo2PoUpdate(vo, data);
            data.setUpdateBy(updateBy);
            data.setUpdateTime(new Date());
            data = getRepository().save(data);
            return getPoConverter().po2Vo(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public VO updateIgnoreNull(VO vo, String updateBy) {
        try {
            PO data = getRepository().findById(vo.getId()).orElse(null);
            assert data != null;
            getPoConverter().vo2PoUpdateIgnoreNull(vo, data);
            data.setUpdateBy(updateBy);
            data.setUpdateTime(new Date());
            data = getRepository().save(data);
            return getPoConverter().po2Vo(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateList(List<VO> list, String updateBy) {
        try {
            for (VO vo : list) {
                PO data = getRepository().findById(vo.getId()).orElse(null);
                assert data != null;
                getPoConverter().vo2PoUpdate(vo, data);
                data.setUpdateBy(updateBy);
                data.setUpdateTime(new Date());
                getRepository().save(data);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateListIgnoreNull(List<VO> list, String updateBy) {
        try {
            for (VO vo : list) {
                PO data = getRepository().findById(vo.getId()).orElse(null);
                assert data != null;
                getPoConverter().vo2PoUpdateIgnoreNull(vo, data);
                data.setUpdateBy(updateBy);
                data.setUpdateTime(new Date());
                getRepository().save(data);
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
        updateStatus(ids, CommonStatus.DELETE.code(), updateBy);
    }


    @Override
    public void deleteByStatus(String id, String updateBy) {
        updateStatus(id, CommonStatus.DELETE.code(), updateBy);
    }


    @Override
    public void updateStatus(String id, String status, String updateBy) {
        PO data = getRepository().findById(id).orElse(null);
        if (data != null) {
            data.setStatus(status);
            data.setUpdateTime(new Date());
            data.setUpdateBy(updateBy);
            getRepository().save(data);
        }
    }

    @Override
    public void updateStatus(List<String> ids, String status, String updateBy) {
        List<PO> list = getRepository().findAllById(ids);
        for (PO data : list) {
            data.setStatus(status);
            data.setUpdateTime(new Date());
            data.setUpdateBy(updateBy);
            getRepository().save(data);
        }
    }

    @Override
    public void updateStatus(String[] ids, String status, String updateBy) {
        updateStatus(Arrays.asList(ids), status, updateBy);
    }

    @Override
    public PagerResp<VO> findByPage(PagerParam pager) {
        return findByPage(pager, null);
    }

    @Override
    public PagerResp<VO> findByPage(PagerParam pager, VO vo) {
        initClz();
        PagerResp<VO> response = new PagerResp<>();
        List<PO> dataList;
        Query query = this.generateQuery(vo, null);
        if (pager.getPageable() != null && pager.getPageable()) {
            // 分页
            PageRequest page = PageRequest.of(pager.getPageNumber() - 1, pager.getPageSize());
            page.withSort(Sort.by(Sort.Direction.DESC, BaseMongoPO.Fields.createTime));
            mongoTemplate.find(query, entityClz);
            long count = mongoTemplate.count(query, entityClz);
            List<PO> list = mongoTemplate.find(query, entityClz);
            Page<PO> pageResult = new PageImpl<>(list, page, count);
            dataList = pageResult.getContent();
            response.setTotal(pageResult.getTotalElements());
            response.setPageNumber(pager.getPageNumber().longValue());
            response.setPageSize(pager.getPageSize().longValue());
            response.setTotalPage((long) pageResult.getTotalPages());
        } else {
            dataList = mongoTemplate.find(query, entityClz);
        }
        response.setDataList(getPoConverter().poList2VoList(dataList));
        return response;
    }
}
