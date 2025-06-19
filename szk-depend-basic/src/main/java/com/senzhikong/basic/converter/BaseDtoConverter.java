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
public interface BaseDtoConverter<VO extends BaseEntityVO, DTO extends BaseEntityDTO, AddRequest extends BaseDTO, UpdateRequest extends BaseDTO, QueryRequest extends BaseDTO> {

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
     * 将添加请求对象列表转换为业务对象列表
     * 批量转换AddRequest为VO对象，适用于批量添加场景
     *
     * @param request 添加请求对象列表
     * @return 转换后的业务对象列表
     */
    List<VO> addRequestList2VoList(List<AddRequest> request);

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

    /**
     * 将更新请求对象列表转换为业务对象列表
     * 批量转换UpdateRequest为VO对象，适用于批量更新场景
     * 会自动忽略createTime、createBy、updateTime、updateBy字段
     *
     * @param request 更新请求对象列表
     * @return 转换后的业务对象列表
     */
    List<VO> updateRequestList2VoList(List<UpdateRequest> request);


    /**
     * 将查询请求对象转换为业务对象
     *
     * @param request 更新请求对象
     * @return 业务对象
     */
    VO queryRequest2Vo(QueryRequest request);
}
