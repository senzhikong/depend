package com.senzhikong.core.converter;


import com.senzhikong.basic.domain.BaseEntityVO;
import com.senzhikong.basic.dto.BaseEntityDTO;

import java.util.List;

/**
 * @author shu
 */
public interface BaseDtoConverter<VO extends BaseEntityVO, DTO extends BaseEntityDTO> {

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
}
