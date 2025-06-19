package com.senzhikong.core.converter;

import com.senzhikong.basic.converter.BaseDtoConverter;
import com.senzhikong.basic.domain.BaseEntityVO;
import com.senzhikong.basic.dto.BaseDTO;
import com.senzhikong.basic.dto.BaseEntityDTO;
import com.senzhikong.core.entity.BaseEntityPO;

/**
 * @author shu
 */
public interface BaseConverter<PO extends BaseEntityPO, VO extends BaseEntityVO, DTO extends BaseEntityDTO, AddRequest extends BaseDTO, UpdateRequest extends BaseDTO, QueryRequest extends BaseDTO> extends BasePoConverter<PO, VO>, BaseDtoConverter<VO, DTO, AddRequest, UpdateRequest, QueryRequest> {

}
