package com.product_service.dto;

import com.product_service.common.dto.InfoDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RequestupdateQuantity extends InfoDTO {
    private List<QuantityDTO> quantityDTOList;
}
