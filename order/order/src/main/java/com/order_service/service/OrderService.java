package com.order_service.service;

import com.order_service.common.mapper.OrderMapper;
import com.order_service.dto.*;
import com.order_service.entity.Order;
import com.order_service.entity.RequeestByIDProductDTO;
import com.order_service.entity.RequestPayment;
import com.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate;
//-----------------------------------------------------------------------------


    public ResponsCartTDO getCartById(RequestCartItemDTO requestCartItemDTO) {
        ResponsCartTDO responsCartTDO = new ResponsCartTDO();
        String cartUrl = "http://localhost:8082/api/cart/view";
        ResponseEntity<ResponsCartTDO> cartResponse = restTemplate.postForEntity(cartUrl, requestCartItemDTO, ResponsCartTDO.class);

        if (cartResponse.getStatusCode() != HttpStatus.OK) {
            return new ResponsCartTDO(1,"erro","1","Failed to fetch cart with id " + requestCartItemDTO.getCartItemDTO().getProductId(),null);
        }

        ResponsCartTDO cartBody = cartResponse.getBody();
        if (cartBody == null || cartBody.getCartDTO() == null) {
            return new ResponsCartTDO(1,"erro","1","Cart data is empty or invalid",null);
        }

        Map<String, CartItemDTO> items = cartBody.getCartDTO().getItems();

        if (cartBody.getClientId().equals(0)) {
            // Process and update product quantities
            if (!updateProductQuantities(items)) {
                return new ResponsCartTDO(1,"erro","1","Failed to update product quantities",null);
            }

            // Calculate total amount and initiate payment
            double totalAmount = calculateTotalAmount(items);
            if (!initiatePayment(totalAmount, "ayman.jawad@gmail.com")) {
                return new ResponsCartTDO(1,"erro","1","Failed to initiate payment",null);
            }
        } else {
        }

        return cartBody;
    }

    private boolean updateProductQuantities(Map<String, CartItemDTO> items) {
        List<QuantityDTO> quantityDTOList = new ArrayList<>();
        for (CartItemDTO itemDTO : items.values()) {
            QuantityDTO quantityDTO = new QuantityDTO();
            quantityDTO.setProductId(itemDTO.getProductId());
            quantityDTO.setQuantity(itemDTO.getQuantity());
            quantityDTOList.add(quantityDTO);
        }

        RequestupdateQuantity requestUpdateQuantity = new RequestupdateQuantity();
        requestUpdateQuantity.setQuantityDTOList(quantityDTOList);

        String updateUrl = "http://localhost:8081/api/products/updateQuantityList";
        ResponseEntity<RequestupdateQuantity> updateResponse = restTemplate.postForEntity(updateUrl, requestUpdateQuantity, RequestupdateQuantity.class);

        return updateResponse.getStatusCode() == HttpStatus.OK && updateResponse.getBody() != null && updateResponse.getBody().getClientId().equals(0);
    }

    private double calculateTotalAmount(Map<String, CartItemDTO> items) {
        return items.values().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }

    private boolean initiatePayment(double amount, String email) {
        RequestPayment requestPayment = new RequestPayment();
        requestPayment.setAmount(amount);
        requestPayment.setEmail(email);

        String paymentUrl = "http://localhost:8084/cr-payment-intent";
        ResponseEntity<RequestPayment> paymentResponse = restTemplate.postForEntity(paymentUrl, requestPayment, RequestPayment.class);

        return paymentResponse.getStatusCode() == HttpStatus.OK && paymentResponse.getBody() != null;
    }





//    public ResponsCartTDO getCartById(RequestCartItemDTO requestCartItemDTO) {
//        String url = "http://localhost:8082/api/cart/view";
//        ResponseEntity<ResponsCartTDO> response = restTemplate.postForEntity(url, requestCartItemDTO, ResponsCartTDO.class);
//        ResponsCartTDO body = response.getBody();
//        Map<String, CartItemDTO> items = body.getCartDTO().getItems();
//
//        if (body.getClientId().equals(0)) {
//            List<QuantityDTO> quantityDTOList = new ArrayList<>();
//            for (CartItemDTO itemDTO : items.values()) {
//                QuantityDTO quantityDTO = new QuantityDTO();
//                quantityDTO.setProductId(itemDTO.getProductId());
//                quantityDTO.setQuantity(itemDTO.getQuantity());
//                quantityDTOList.add(quantityDTO);
//            }
//            RequestupdateQuantity requestupdateQuantity = new RequestupdateQuantity();
//            requestupdateQuantity.setQuantityDTOList(quantityDTOList);
//
//            String url2 = "http://localhost:8081/api/products/updateQuantityList";
//            ResponseEntity<RequestupdateQuantity> requestupdateQuantityResponseEntity = restTemplate.postForEntity(url2, requestupdateQuantity, RequestupdateQuantity.class);
//            RequestupdateQuantity body1 = requestupdateQuantityResponseEntity.getBody();
//
//            if (body1.getClientId().equals(0)) {
//                double tatalAmount = 0;
//                for (CartItemDTO itemDTO : items.values()) {
//                    tatalAmount += (itemDTO.getUnitPrice() * itemDTO.getQuantity());
//                }
//                RequestPayment requestPayment = new RequestPayment();
//                requestPayment.setAmount(tatalAmount);
//                requestPayment.setEmail("ayman.jawad@gmail.com");
//                String url1 = "http://localhost:8084/cr-payment-intent";
//                ResponseEntity<RequestPayment> requestPaymentResponseEntity
//                        = restTemplate.postForEntity(url1, requestPayment, RequestPayment.class);
//            }
//
//        } else {
//
//        }
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return body;
//        } else {
//            throw new RuntimeException("Failed to fetch cart with id ");
//        }
//    }



//    public ResponsCartTDO getCartById(RequestCartItemDTO requestCartItemDTO) {
//        String url = "http://localhost:8082/api/cart/view";
//        ResponseEntity<ResponsCartTDO> response = restTemplate.postForEntity(url, requestCartItemDTO, ResponsCartTDO.class);
//        ResponsCartTDO body = response.getBody();
//        Map<String, CartItemDTO> items = body.getCartDTO().getItems();
//        double tatalAmount = 0;
//        for (CartItemDTO itemDTO: items.values()) {
//            tatalAmount += (itemDTO.getUnitPrice() * itemDTO.getQuantity());
//        }
//        RequestPayment requestPayment = new RequestPayment();
//        requestPayment.setAmount(tatalAmount);
//        requestPayment.setEmail("ayman.jawad@gmail.com");
//
//        String url1 = "http://localhost:8084/cr-payment-intent";
//        ResponseEntity<RequestPayment> requestPaymentResponseEntity
//                = restTemplate.postForEntity(url1, requestPayment, RequestPayment.class);
//        if(true){
//            List<QuantityDTO> quantityDTOList = new ArrayList<>();
//            for (CartItemDTO itemDTO: items.values()){
//                QuantityDTO quantityDTO = new QuantityDTO();
//                quantityDTO.setProductId(itemDTO.getProductId());
//                quantityDTO.setQuantity(itemDTO.getQuantity());
//                quantityDTOList.add(quantityDTO);
//            }
//            RequestupdateQuantity requestupdateQuantity = new RequestupdateQuantity();
//            requestupdateQuantity.setQuantityDTOList(quantityDTOList);
//
//            String url2 = "http://localhost:8081/api/products/updateQuantityList";
//            ResponseEntity<RequestupdateQuantity> requestupdateQuantityResponseEntity = restTemplate.postForEntity(url2, requestupdateQuantity, RequestupdateQuantity.class);
//            RequestupdateQuantity body1 = requestupdateQuantityResponseEntity.getBody();
//        }else {
//
//        }
//        System.out.println(tatalAmount);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//
//            return body;
//        } else {
//            throw new RuntimeException("Failed to fetch cart with id ");
//        }
//    }



//    -------------------------------------------------------------------------

    public ResponseOrderDTO createOrder(RequestOrderDTO requestOrderDTO) {
        OrderDTO orderDTO = requestOrderDTO.getOrderDTO();
        try {
            Order order = OrderMapper.convertToEntity(orderDTO);
            Double totalPrice = orderDTO.getUnitPrice() * orderDTO.getQuantity();
            order.setTotalPrice(totalPrice);
            Order savedOrder = orderRepository.save(order);
            OrderDTO savedOrderDTO = OrderMapper.convertToDTO(savedOrder);
            return new ResponseOrderDTO(0, "Success", "Order created successfully.", "Order created successfully.", savedOrderDTO);
        } catch (Exception e) {
            return new ResponseOrderDTO(1, "Error", "Failed to create order. Please try again later.", "Error occurred");
        }
    }


    public ResponseAllOrderDTO getAllOrders(RequestAllOrderDTO requestAllOrderDTO) {
        try {
            List<Order> orders = orderRepository.findAll();
            if (orders == null || orders.isEmpty()) {
                return new ResponseAllOrderDTO(1, "No Orders Found", "No orders available", "No orders available", Collections.emptyList());
            }
            List<OrderDTO> orderDTOS = orders.stream()
                    .map(OrderMapper::convertToDTO)
                    .collect(Collectors.toList());
            return new ResponseAllOrderDTO(0, "Success", "Orders retrieved successfully", "Orders retrieved successfully", orderDTOS);
        } catch (Exception e) {
            return new ResponseAllOrderDTO(1, "Error", "Failed to retrieve orders. Please try again later.", "Error occurred", Collections.emptyList());
        }
    }


    public ResponseOrderDTO getOrderById(RequeestByIdOrderDTO requeestByIdOrderDTO) {
        Long orderId = requeestByIdOrderDTO.getOrdertId();
        try {
            Optional<Order> orderOptional = orderRepository.findById(orderId);
            if (orderOptional.isPresent()) {
                OrderDTO orderDTO = OrderMapper.convertToDTO(orderOptional.get());
                return new ResponseOrderDTO(0, "Success", "Order found successfully", "Order found successfully", orderDTO);
            } else {
                return new ResponseOrderDTO(1, "Not Found", "Order not found with ID " + orderId, "Order not found");
            }
        } catch (Exception e) {
            return new ResponseOrderDTO(1, "Error", "Failed to retrieve order. Please try again later.", "Error occurred");
        }
    }


    public ResponseOrderDTO updateOrder(RequestOrderDTO requestOrderDTO) {
        OrderDTO orderDTO = requestOrderDTO.getOrderDTO();
        try {
            if (!orderRepository.existsById(orderDTO.getOrderId())) {
                return new ResponseOrderDTO(1, "Order Not Found", "Order with ID " + orderDTO.getOrderId() + " does not exist.", "Order not found");
            }
            Order order = OrderMapper.convertToEntity(orderDTO);
            order.setTotalPrice(orderDTO.getUnitPrice() * orderDTO.getQuantity());
            Order updatedOrder = orderRepository.save(order);
            OrderDTO updatedOrderDTO = OrderMapper.convertToDTO(updatedOrder);
            return new ResponseOrderDTO(0, "Update Successful", "Order updated successfully.", "Order updated successfully.", updatedOrderDTO);
        } catch (Exception e) {
            return new ResponseOrderDTO(1, "Error", "Failed to update order. Please try again later.", "Error occurred");
        }
    }


    public ResponseOrderDTO deleteOrder(RequeestByIdOrderDTO requeestByIdOrderDTO) {
        Long orderId = requeestByIdOrderDTO.getOrdertId();
        try {
            if (orderRepository.existsById(orderId)) {
                orderRepository.deleteById(orderId);
                return new ResponseOrderDTO(0, "Success", "Order deleted successfully", "Order deleted successfully");
            } else {
                return new ResponseOrderDTO(1, "Not Found", "Order with ID " + orderId + " not found", "Order not found");
            }
        } catch (Exception e) {
            return new ResponseOrderDTO(1, "Error", "Failed to delete order. Please try again later.", "Error occurred");
        }
    }


}

