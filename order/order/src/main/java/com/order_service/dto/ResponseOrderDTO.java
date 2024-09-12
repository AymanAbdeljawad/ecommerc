package com.order_service.dto;

import com.order_service.common.dto.InfoDTO;
import lombok.Data;

@Data
public class ResponseOrderDTO extends InfoDTO {
    private OrderDTO orderDTO;
    public ResponseOrderDTO(){}
    public ResponseOrderDTO(Integer clientId, String tracingId, String errorCode, String errorDesc, OrderDTO orderDTO) {
        super(clientId, tracingId, errorCode, errorDesc);
        this.orderDTO = orderDTO;
    }

    public ResponseOrderDTO(Integer clientId, String tracingId, String errorCode, String errorDesc) {
        super(clientId, tracingId, errorCode, errorDesc);
    }
}
