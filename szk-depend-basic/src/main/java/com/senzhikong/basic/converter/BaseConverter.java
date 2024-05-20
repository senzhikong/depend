package com.senzhikong.basic.converter;

import com.senzhikong.basic.dto.BaseEntityDTO;
import com.senzhikong.basic.entity.BaseEntityPO;
import com.senzhikong.basic.vo.BaseEntityVO;

/**
 * @author shu
 */
public interface BaseConverter<PO extends BaseEntityPO, VO extends BaseEntityVO, DTO extends BaseEntityDTO> extends BasePoConverter<PO, VO>, BaseDtoConverter<VO, DTO> {

}
