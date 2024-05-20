package com.senzhikong.core.converter;

import com.senzhikong.basic.domain.BaseEntityVO;
import com.senzhikong.basic.dto.BaseEntityDTO;
import com.senzhikong.basic.entity.BaseEntityPO;

/**
 * @author shu
 */
public interface BaseConverter<PO extends BaseEntityPO, VO extends BaseEntityVO, DTO extends BaseEntityDTO> extends BasePoConverter<PO, VO>, BaseDtoConverter<VO, DTO> {

}
