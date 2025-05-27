package com.senzhikong.basic.converter;


import com.senzhikong.basic.domain.BaseEntityVO;
import com.senzhikong.basic.dto.BaseDTO;
import com.senzhikong.basic.dto.BaseEntityDTO;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author shu
 */
@MapperConfig
public interface BaseDtoConverter<VO extends BaseEntityVO, DTO extends BaseEntityDTO, AddRequest extends BaseDTO, UpdateRequest extends BaseDTO> {

    /**
     * 传输对象转业务对象
     *
     * @param dto 传输对象
     * @return 业务对象
     */
    VO dto2Vo(DTO dto);


    /**
     * 传输对象列表转业务对象列表
     *
     * @param dtoList 传输对象列表
     * @return 业务对象列表
     */
    List<VO> dtoList2VoList(List<DTO> dtoList);

    /**
     * 传输对象转业务对象
     *
     * @param vo 业务对象
     * @return 传输对象
     */
    DTO vo2Dto(VO vo);

    /**
     * 业务对象列表转传输对象列表
     *
     * @param voList 业务对象列表
     * @return 传输对象列表
     */
    List<DTO> voList2DtoList(List<VO> voList);

    /**
     * 将添加请求对象转换为业务对象
     *
     * @param request 添加请求对象
     * @return 业务对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    VO addRequest2Vo(AddRequest request);

    /**
     * 将更新请求对象转换为业务对象
     *
     * @param request 更新请求对象
     * @return 业务对象
     */
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    VO updateRequest2Vo(UpdateRequest request);
}
