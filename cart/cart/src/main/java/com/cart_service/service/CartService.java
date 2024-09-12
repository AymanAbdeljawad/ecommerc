//------------------------------------------------------------------------------
package com.cart_service.service;

import com.cart_service.dto.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

//    private static final Map<String, CartDTO> sessionCartMap = new HashMap<>();
//
//    public ResponsCartItemDTO addItemToCart(RequestCartItemDTO requestCartItemDTO) {
//        ResponsCartItemDTO responsCartItemDTO;
//        CartItemDTO cartItemDTO = requestCartItemDTO.getCartItemDTO();
//        ResponsCartTDO cartFromSession = getCartFromSession(requestCartItemDTO);
//        CartDTO cartDTO = cartFromSession.getCartDTO();
//        CartItemDTO existingItem = cartDTO.getItem(cartItemDTO.getProductId());
//        if (existingItem != null) {
//            existingItem.setQuantity(cartItemDTO.getQuantity());
//            cartDTO.addItem(cartItemDTO);
//            responsCartItemDTO = new ResponsCartItemDTO(1, "trcinf", "no error", "disc");
//        } else {
//            cartDTO.addItem(cartItemDTO);
//            responsCartItemDTO = new ResponsCartItemDTO(1, "trcinf addItem", "no error addItem", "disc addItem");
//        }
//        saveCartToSession(requestCartItemDTO.getToken(), cartDTO);
//        return responsCartItemDTO;
//    }

    private static final Map<String, CartDTO> sessionCartMap = new HashMap<>();

    public ResponsCartItemDTO addItemToCart(RequestCartItemDTO requestCartItemDTO) {
        // Get cart item DTO and cart from session
        ResponsCartItemDTO responsCartItemDTO;
        CartItemDTO newCartItem = requestCartItemDTO.getCartItemDTO();
        ResponsCartTDO cartFromSession = getCartFromSession(requestCartItemDTO);
        CartDTO cartDTO = cartFromSession.getCartDTO();

        // Check if item already exists in cart
        CartItemDTO existingItem = cartDTO.getItem(newCartItem.getProductId());

        if (existingItem != null) {
            // Update existing item quantity
            existingItem.setQuantity(newCartItem.getQuantity());
            // Response when item is updated
            responsCartItemDTO = new ResponsCartItemDTO(1, "Item updated in cart", "Item quantity updated successfully", "disc");
        } else {
            // Add new item to cart
            cartDTO.addItem(newCartItem);
            // Response when new item is added
            responsCartItemDTO = new ResponsCartItemDTO(1, "Item added to cart", "Item added successfully", "disc");
        }

        // Save updated cart to session
        saveCartToSession(requestCartItemDTO.getToken(), cartDTO);
        return responsCartItemDTO;
    }


//    public ResponsCartTDO getCartFromSession(RequestCartItemDTO requestCartItemDTO) {
//        ResponsCartTDO responsCartTDO;
//        CartDTO cartDTO = sessionCartMap.computeIfAbsent(requestCartItemDTO.getToken(), k -> new CartDTO());
//        responsCartTDO = new ResponsCartTDO(1, "tracingId", "err", "desc", cartDTO);
//        return responsCartTDO;
//    }

    public ResponsCartTDO getCartFromSession(RequestCartItemDTO requestCartItemDTO) {
        // Get the token from the request to identify the session
        String sessionToken = requestCartItemDTO.getToken();

        // Retrieve the cart from the session or create a new one if it does not exist
        CartDTO cartDTO = sessionCartMap.computeIfAbsent(sessionToken, k -> new CartDTO());

        // Create and return the response object with the cart information
        return new ResponsCartTDO(0, "tracingId", "Success", "Cart retrieved successfully",sessionToken, cartDTO);
    }


//    public ResponsCartTDO getCartFromSession(RequestCartItemIdDTO requestCartItemIdDTO) {
//        ResponsCartTDO responsCartTDO;
//        CartDTO cartDTO = sessionCartMap.computeIfAbsent(requestCartItemIdDTO.getToken(), k -> new CartDTO());
//        responsCartTDO = new ResponsCartTDO(1, "tracingId", "err", "desc", cartDTO);
//        return responsCartTDO;
//    }

    public ResponsCartTDO getCartFromSession(RequestCartItemIdDTO requestCartItemIdDTO) {
        // الحصول على رمز الجلسة من طلب العميل
        String sessionToken = requestCartItemIdDTO.getToken();

        // استرجاع سلة التسوق من الجلسة أو إنشاء واحدة جديدة إذا لم تكن موجودة
        CartDTO cartDTO = sessionCartMap.computeIfAbsent(sessionToken, k -> new CartDTO());

        // إنشاء كائن الاستجابة مع معلومات سلة التسوق
        return new ResponsCartTDO(1, "tracingId", "Success", "Cart retrieved successfully", cartDTO);
    }


//    public ResponsCartItemDTO removeItemFromCart(RequestCartItemIdDTO requestCartItemIdDTO) {
//        ResponsCartItemDTO responsCartItemDTO;
//        CartDTO cartDTO = getCartFromSession(requestCartItemIdDTO).getCartDTO();
//        cartDTO.removeItem(requestCartItemIdDTO.getProductId());
//        saveCartToSession(requestCartItemIdDTO.getToken(), cartDTO);
//        responsCartItemDTO =new ResponsCartItemDTO(1,"re","re","re");
//        return responsCartItemDTO;
//    }

    public ResponsCartItemDTO removeItemFromCart(RequestCartItemIdDTO requestCartItemIdDTO) {
        // الحصول على رمز الجلسة من الطلب
        String sessionToken = requestCartItemIdDTO.getToken();
        String productId = requestCartItemIdDTO.getProductId();

        // استرجاع سلة التسوق من الجلسة
        CartDTO cartDTO = getCartFromSession(requestCartItemIdDTO).getCartDTO();

        // إزالة العنصر من السلة
        boolean itemRemoved = cartDTO.deleteItem(productId);

        // تحديث الجلسة مع السلة المعدلة
        saveCartToSession(sessionToken, cartDTO);

        // تحديد رسالة الاستجابة بناءً على نتيجة العملية
        String statusMessage = itemRemoved ? "Item removed successfully" : "Item not found in cart";
        return new ResponsCartItemDTO(1, "tracingId", statusMessage, itemRemoved ? "Success" : "Failure");
    }


    private void saveCartToSession(String sessionId, CartDTO cart) {
        sessionCartMap.put(sessionId, cart);
    }

}

//------------------------------------------------------------------------------

//package com.cart_service.service;
//
//import com.cart_service.dto.*;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class CartService {
//
//    private static final Map<String, CartDTO> sessionCartMap = new HashMap<>();
//
//    public ResponsCartItemDTO addItemToCart(RequestCartItemDTO requestCartItemDTO) {
//        ResponsCartItemDTO responsCartItemDTO;
//        CartItemDTO cartItemDTO = requestCartItemDTO.getCartItemDTO();
//        String sessionId = requestCartItemDTO.getToken();
//        ResponsCartTDO cartFromSession = getCartFromSession(sessionId);
//        CartDTO cartDTO = cartFromSession.getCartDTO();
//        CartItemDTO existingItem = cartDTO.getItem(cartItemDTO.getProductId());
//        if (existingItem != null) {
//            existingItem.setQuantity(cartItemDTO.getQuantity());
//            cartDTO.addItem(cartItemDTO);
//            responsCartItemDTO = new ResponsCartItemDTO(1, "trcinf", "no error", "disc");
//        } else {
//            cartDTO.addItem(cartItemDTO);
//            responsCartItemDTO = new ResponsCartItemDTO(1, "trcinf addItem", "no error addItem", "disc addItem");
//        }
//        saveCartToSession(sessionId, cartDTO);
//        return responsCartItemDTO;
//    }
//
//    public ResponsCartTDO getCartFromSession(String sessionsId) {
//        ResponsCartTDO responsCartTDO;
//        String sessionId = sessionsId;
//        CartDTO cartDTO = sessionCartMap.computeIfAbsent(sessionId, k -> new CartDTO());
//        responsCartTDO = new ResponsCartTDO(1, "aa", "aa", "aa", cartDTO);
//        return responsCartTDO;
//    }
//
//
//    public ResponsCartItemDTO removeItemFromCart(RequestCartItemIdDTO requestCartItemIdDTO) {
//        ResponsCartItemDTO responsCartItemDTO;
//        String sessionId = requestCartItemIdDTO.getToken();
//        CartDTO cartDTO = getCartFromSession(null).getCartDTO();
//        cartDTO.removeItem(requestCartItemIdDTO.getProductId());
//        saveCartToSession(sessionId, cartDTO);
//        responsCartItemDTO =new ResponsCartItemDTO(1,"re","re","re");
//        return responsCartItemDTO;
//    }
//
//    private void saveCartToSession(String sessionId, CartDTO cart) {
//        sessionCartMap.put(sessionId, cart);
//    }
//
//}
//-----------------------------------------------------------------------------------
//package com.cart_service.service;
//
//import com.cart_service.dto.*;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class CartService {
//
//    private static final Map<String, CartDTO> sessionCartMap = new HashMap<>();
//
//    public ResponsCartItemDTO addItemToCart(RequestCartItemDTO requestCartItemDTO, HttpSession session) {
//        ResponsCartItemDTO responsCartItemDTO;
//        CartItemDTO cartItemDTO = requestCartItemDTO.getCartItemDTO();
//        String sessionId = session.getId();
//        ResponsCartTDO cartFromSession = getCartFromSession(null, sessionId);
//        CartDTO cartDTO = cartFromSession.getCartDTO();
//        CartItemDTO existingItem = cartDTO.getItem(cartItemDTO.getProductId());
//        if (existingItem != null) {
//            existingItem.setQuantity(cartItemDTO.getQuantity());
//            cartDTO.addItem(cartItemDTO);
//            responsCartItemDTO = new ResponsCartItemDTO(1, "trcinf", "no error", "disc");
//        } else {
//            cartDTO.addItem(cartItemDTO);
//            responsCartItemDTO = new ResponsCartItemDTO(1, "trcinf addItem", "no error addItem", "disc addItem");
//        }
//        saveCartToSession(sessionId, cartDTO);
//        return responsCartItemDTO;
//    }
//
//    public ResponsCartTDO getCartFromSession(RequestCartItemDTO requestCartItemDTO, String sessionId) {
//        ResponsCartTDO responsCartTDO;
//        CartDTO cartDTO = sessionCartMap.computeIfAbsent(sessionId, k -> new CartDTO());
//        responsCartTDO = new ResponsCartTDO(1, "aa", "aa", "aa", cartDTO);
//        return responsCartTDO;
//    }
//
//
//    public ResponsCartItemDTO removeItemFromCart(RequestCartItemIdDTO requestCartItemIdDTO, HttpSession session) {
//        ResponsCartItemDTO responsCartItemDTO;
//        String sessionId = session.getId();
//        CartDTO cartDTO = getCartFromSession(null, sessionId).getCartDTO();
//        cartDTO.removeItem(requestCartItemIdDTO.getProductId());
//        saveCartToSession(sessionId, cartDTO);
//        responsCartItemDTO =new ResponsCartItemDTO(1,"re","re","re");
//        return responsCartItemDTO;
//    }
//
//    private void saveCartToSession(String sessionId, CartDTO cart) {
//        sessionCartMap.put(sessionId, cart);
//    }
//
//}
