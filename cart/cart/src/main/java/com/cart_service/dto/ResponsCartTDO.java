package com.cart_service.dto;

import com.cart_service.common.dto.InfoDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponsCartTDO extends InfoDTO {
    private CartDTO cartDTO;

    public ResponsCartTDO() {}
    public ResponsCartTDO(Integer clientId, String tracingId, String errorCode, String errorDesc, CartDTO cartDTO) {
        super(clientId, tracingId, errorCode, errorDesc);
        this.cartDTO = cartDTO;
    }

    public ResponsCartTDO(Integer clientId, String tracingId, String errorCode, String errorDesc, String token, CartDTO cartDTO) {
        super(clientId, tracingId, errorCode, errorDesc, token);
        this.cartDTO = cartDTO;
    }
}
