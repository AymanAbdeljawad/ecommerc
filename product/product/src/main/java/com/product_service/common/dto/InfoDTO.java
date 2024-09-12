package com.product_service.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoDTO implements Serializable {
    private static long SerialVersionID = 1L;
    private Integer clientId;
    private String tracingId;
    private String errorCode;
    private String errorDesc;

}
