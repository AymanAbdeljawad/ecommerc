package com.cart_service.controller;

import com.cart_service.dto.*;
import com.cart_service.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ResponsCartItemDTO> addItem(@Valid @RequestBody RequestCartItemDTO requestCartItemDTO) {
        ResponsCartItemDTO responsCartItemDTO = cartService.addItemToCart(requestCartItemDTO);
        return ResponseEntity.ok(responsCartItemDTO);
    }

    @PostMapping("/view")
    public ResponseEntity<ResponsCartTDO> viewCart(@Valid @RequestBody RequestCartItemDTO requestCartItemDTO) {
        ResponsCartTDO cartFromSession = cartService.getCartFromSession(requestCartItemDTO);
        return ResponseEntity.ok(cartFromSession);
    }

    @PostMapping("/remove")
    public ResponseEntity<ResponsCartItemDTO> removeItem(@Valid@RequestBody RequestCartItemIdDTO requestCartItemIdDTO, HttpSession session) {
        ResponsCartItemDTO responsCartItemDTO = cartService.removeItemFromCart(requestCartItemIdDTO);
        return ResponseEntity.ok(responsCartItemDTO);
    }
}




//package com.cart_service.controller;
//
//import com.cart_service.dto.*;
//import com.cart_service.service.CartService;
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/cart")
//public class CartController {
//
//    @Autowired
//    private CartService cartService;
//
//    @PostMapping("/add")
//    public ResponseEntity<ResponsCartItemDTO> addItem(@Valid @RequestBody RequestCartItemDTO requestCartItemDTO, HttpSession session) {
//        ResponsCartItemDTO responsCartItemDTO = cartService.addItemToCart(requestCartItemDTO, session);
//        return ResponseEntity.ok(responsCartItemDTO);
//    }
//
//    @PostMapping("/view")
//    public ResponseEntity<ResponsCartTDO> viewCart(@Valid @RequestBody RequestCartItemDTO requestCartItemDTO, HttpSession session) {
//        ResponsCartTDO cartFromSession = cartService.getCartFromSession(requestCartItemDTO, session.getId());
//        return ResponseEntity.ok(cartFromSession);
//    }
//
//    @PostMapping("/remove")
//    public ResponseEntity<ResponsCartItemDTO> removeItem(@Valid@RequestBody RequestCartItemIdDTO requestCartItemIdDTO, HttpSession session) {
//        ResponsCartItemDTO responsCartItemDTO = cartService.removeItemFromCart(requestCartItemIdDTO, session);
//        return ResponseEntity.ok(responsCartItemDTO);
//    }
//}
