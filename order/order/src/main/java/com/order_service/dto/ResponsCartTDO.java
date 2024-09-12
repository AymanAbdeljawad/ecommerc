package com.order_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.order_service.common.dto.InfoDTO;
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

}
