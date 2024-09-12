package com.order_service.dto;

import com.order_service.common.dto.InfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class RequestupdateQuantity extends InfoDTO {
    private List<QuantityDTO> quantityDTOList;
}
