package com.senzhikong.mongo.converter;

import com.senzhikong.basic.converter.BaseDtoConverter;
import com.senzhikong.basic.domain.BaseEntityVO;
import com.senzhikong.basic.dto.BaseDTO;
import com.senzhikong.basic.dto.BaseEntityDTO;
import com.senzhikong.mongo.entity.BaseMongoPO;

/**
 * @author shu
 */
public interface BaseMongoConverter<PO extends BaseMongoPO, VO extends BaseEntityVO, DTO extends BaseEntityDTO, AddRequest extends BaseDTO, UpdateRequest extends BaseDTO> extends BaseMongoPoConverter<PO, VO>, BaseDtoConverter<VO, DTO, AddRequest, UpdateRequest> {

}
