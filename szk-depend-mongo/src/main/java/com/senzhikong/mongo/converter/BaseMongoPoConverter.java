package com.senzhikong.mongo.converter;

import com.senzhikong.basic.domain.BaseEntityVO;
import com.senzhikong.mongo.entity.BaseMongoPO;
import org.mapstruct.BeanMapping;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * @author shu
 */
@MapperConfig
public interface BaseMongoPoConverter<PO extends BaseMongoPO, VO extends BaseEntityVO> {


    /**
     * 持久对象转业务对象
     *
     * @param po 持久对象
     * @return 业务对象
     */
    VO po2Vo(PO po);


    /**
     * 持久对象列表转业务对象列表
     *
     * @param poList 持久对象列表
     * @return 业务对象列表
     */
    List<VO> poList2VoList(List<PO> poList);

    /**
     * 业务对象转持久对象
     *
     * @param vo 业务对象
     * @return 持久对象
     */
    PO vo2Po(VO vo);

    /**
     * 业务对象列表转持久对象列表
     *
     * @param voList 业务对象列表
     * @return 持久对象列表
     */
    List<PO> voList2PoList(List<VO> voList);


    /**
     * 业务对象转持久对象
     *
     * @param vo 业务对象
     * @param po 持久对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    void vo2PoUpdate(VO vo, @MappingTarget PO po);

    /**
     * 业务对象转持久对象
     *
     * @param vo 业务对象
     * @param po 持久对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void vo2PoUpdateIgnoreNull(VO vo, @MappingTarget PO po);

    /**
     * 业务对象转持久对象
     *
     * @param vo 业务对象
     * @param po 持久对象
     */
    void vo2Po(VO vo, @MappingTarget PO po);

    /**
     * 业务对象转持久对象
     *
     * @param vo 业务对象
     * @param po 持久对象
     */
    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void vo2PoIgnoreNull(VO vo, @MappingTarget PO po);

    /**
     * 持久对象转业务对象
     *
     * @param po 持久对象
     * @param vo 业务对象
     */
    void po2Vo(PO po, @MappingTarget VO vo);


}
