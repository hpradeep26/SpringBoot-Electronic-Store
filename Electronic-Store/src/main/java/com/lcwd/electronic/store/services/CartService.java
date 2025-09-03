package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.AddItemToCartDto;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {
	
	CartDto addItemToCart(String userId,AddItemToCartDto addItemToCartDto);
	
	void removeItemFromCart(String userId,int cartItem);
	
	void clearCart(String userId);
	
	CartDto getCartByUser(String userId);
	
	

}
