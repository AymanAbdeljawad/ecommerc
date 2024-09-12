package com.product_service.dto;

import com.product_service.common.dto.InfoDTO;
import lombok.Data;

@Data
public class RequeestByIDProductDTO extends InfoDTO {
    private Long productId;
}
