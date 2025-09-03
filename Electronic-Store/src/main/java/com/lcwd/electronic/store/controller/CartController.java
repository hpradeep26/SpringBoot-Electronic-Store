package com.lcwd.electronic.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.AddItemToCartDto;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.services.CartService;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
	
	@Autowired
	CartService cartService;
	
	@PostMapping("/{userId}")
	public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartDto addItemToCartDto){
		if (addItemToCartDto.getQuantity() <= 0) {
            throw new BadApiRequestException("Requested quantity is not valid !!");
        }

		CartDto cartDto = cartService.addItemToCart(userId, addItemToCartDto);
		return new ResponseEntity<>(cartDto,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{userId}/items/{itemId}")
	public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId,@PathVariable int itemId){
		cartService.removeItemFromCart(userId, itemId);
		ApiResponseMessage message = new ApiResponseMessage();
		message.setHttpStatus(HttpStatus.OK);
		message.setMessage("Item Removed from Cart");
		return new ResponseEntity<>(message,HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){
		ApiResponseMessage message = new ApiResponseMessage();
		CartDto cartDto = cartService.getCartByUser(userId);
		if(cartDto==null) {
			message.setMessage("Cart is empty nothing to clear");
		}else {
			cartService.clearCart(userId);
			message.setMessage("Cart Items are Cleared");
		}
		message.setHttpStatus(HttpStatus.OK);
		return new ResponseEntity<>(message,HttpStatus.OK);
	}
	@GetMapping("/{userId}")
	public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId){
		CartDto cartDto = cartService.getCartByUser(userId);
		return new ResponseEntity<>(cartDto,HttpStatus.OK);
	}
}
