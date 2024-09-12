package com.order_service.common.mapper;
import com.order_service.dto.OrderDTO;
import com.order_service.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public static OrderDTO convertToDTO(Order order) {
        if (order == null) {
            return null;
        }
        return OrderDTO.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .token(order.getToken())
                .productId(order.getProductId())
                .productName(order.getProductName())
                .description(order.getDescription())
                .unitPrice(order.getTotalPrice())
                .quantity(order.getQuantity())
                .build();
    }

    public static Order convertToEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        Order order = new Order();
        order.setId(orderDTO.getOrderId());
        order.setUserId(orderDTO.getUserId());
        order.setToken(orderDTO.getToken());
        order.setProductId(orderDTO.getProductId());
        order.setProductName(orderDTO.getProductName());
        order.setDescription(orderDTO.getDescription());
        order.setTotalPrice(orderDTO.getUnitPrice());
        order.setQuantity(orderDTO.getQuantity());
        return order;
    }
}
