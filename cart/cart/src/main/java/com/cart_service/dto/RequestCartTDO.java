package com.cart_service.dto;

import com.cart_service.common.dto.InfoDTO;
import lombok.Data;

@Data
public class RequestCartTDO extends InfoDTO {
    private CartDTO cartDTO;
}
