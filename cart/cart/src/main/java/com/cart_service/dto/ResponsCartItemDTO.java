package com.cart_service.dto;

import com.cart_service.common.dto.InfoDTO;
import lombok.Data;

@Data
public class ResponsCartItemDTO extends InfoDTO {

    public ResponsCartItemDTO() {
    }
    public ResponsCartItemDTO(Integer clientId, String tracingId, String errorCode, String errorDesc) {
        super(clientId, tracingId, errorCode, errorDesc);
    }
}
