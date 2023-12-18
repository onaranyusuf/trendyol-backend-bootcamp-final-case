package com.trendyol.shoppingcart.controller;

import com.trendyol.shoppingcart.models.dto.ItemDTO;
import com.trendyol.shoppingcart.models.dto.ResponseDTO;
import com.trendyol.shoppingcart.models.dto.ShoppingCartDTO;
import com.trendyol.shoppingcart.models.dto.VasItemDTO;
import com.trendyol.shoppingcart.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping(path = "/add-item", produces = "application/json")
    private ResponseEntity<ResponseDTO<String>> addItemToShoppingCart(@RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(shoppingCartService.addItemToShoppingCart(itemDTO));
    }

    @PostMapping(path = "/add-vas-item", produces = "application/json")
    private ResponseEntity<ResponseDTO<String>> addVasItemToShoppingCart(@RequestBody VasItemDTO vasItemDTO) {
        return ResponseEntity.ok(shoppingCartService.addVasItemToShoppingCart(vasItemDTO));
    }

    @GetMapping(path = "/get-shopping-cart", produces = "application/json")
    private ResponseEntity<ResponseDTO<ShoppingCartDTO>> getShoppingCart() {
        return ResponseEntity.ok(shoppingCartService.getShoppingCart());
    }

    @PostMapping(path = "/reset-shopping-cart", produces = "application/json")
    private ResponseEntity<ResponseDTO<String>> resetShoppingCart() {
        return ResponseEntity.ok(shoppingCartService.resetShoppingCart());
    }

    @DeleteMapping(path = "/remove-item", produces = "application/json")
    private ResponseEntity<ResponseDTO<String>> removeItemFromShoppingCart(@RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(shoppingCartService.removeItemFromShoppingCart(itemDTO));
    }

    @DeleteMapping(path = "/remove-vas-item", produces = "application/json")
    private ResponseEntity<ResponseDTO<String>> removeVasItemFromShoppingCart(@RequestBody VasItemDTO vasItemDTO) {
        return ResponseEntity.ok(shoppingCartService.removeVasItemFromShoppingCart(vasItemDTO));
    }

}
