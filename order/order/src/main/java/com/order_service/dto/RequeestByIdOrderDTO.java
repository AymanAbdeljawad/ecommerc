package com.order_service.dto;

import com.order_service.common.dto.InfoDTO;
import lombok.Data;

@Data
public class RequeestByIdOrderDTO extends InfoDTO {
    private Long ordertId;
}
