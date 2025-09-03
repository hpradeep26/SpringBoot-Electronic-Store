package com.lcwd.electronic.store.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.AddItemToCartDto;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepositary;
import com.lcwd.electronic.store.repositories.CartRepositary;
import com.lcwd.electronic.store.repositories.ProductRepositary;
import com.lcwd.electronic.store.repositories.UserRepositary;
import com.lcwd.electronic.store.services.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	UserRepositary userRepositary;
	
	@Autowired
	ProductRepositary productRepositary;
	
	@Autowired
	CartRepositary cartRepositary;
	
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	CartItemRepositary cartItemRepositary;
	
	@Override
	public CartDto addItemToCart(String userId, AddItemToCartDto addItemToCartDto) {
		User user = userRepositary.findById(userId).orElseThrow(() ->new ResourceNotFoundException("User Not found for"+userId));
		
		Product product = productRepositary.findById(addItemToCartDto.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product Not Found for"+addItemToCartDto.getProductId()));
		String productId = product.getProductId();
		Optional<Cart> optionalCart = cartRepositary.findByUser(user);
		int quantity = addItemToCartDto.getQuantity();
		Cart cart = null;
		if(!optionalCart.isPresent()) {
			cart = new Cart();
			cart.setCreatedDate(new Date());
			cart.setUser(user);
		}else {
			cart = optionalCart.get();
		}
		List<CartItem> cartItems = cart.getCartItems();
		 AtomicReference<Boolean> updated = new AtomicReference<>(false);
		 cartItems = cartItems.stream().map(item -> {

	            if (item.getProduct().getProductId().equals(productId)) {
	                //item already present in cart
	                item.setQuantity(quantity);
	                item.setTotalPrice(quantity * product.getPrice());
	                updated.set(true);
	            }
	            return item;
	        }).collect(Collectors.toList());
		
		if(!updated.get()) {
			CartItem cartItem = new CartItem();
			cartItem.setQuantity(quantity);
			cartItem.setTotalPrice(quantity * product.getPrice());
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cart.getCartItems().add(cartItem);
		}
		
		Cart savedCart = cartRepositary.save(cart);
		return mapper.map(savedCart, CartDto.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItemId) {
		userRepositary.findById(userId).orElseThrow(() ->new ResourceNotFoundException("User Not found for"+userId));
		CartItem cartItem = cartItemRepositary.findById(cartItemId).orElseThrow(() ->new ResourceNotFoundException("Cart Item Not found for"+cartItemId));
		cartItemRepositary.delete(cartItem);
	}

	@Override
	public void clearCart(String userId) {
		User user = userRepositary.findById(userId).orElseThrow(() ->new ResourceNotFoundException("User Not found for"+userId));
		Cart cart = cartRepositary.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
		cart.getCartItems().clear();
		cartRepositary.save(cart);
	}

	@Override
	public CartDto getCartByUser(String userId) {
		User user = userRepositary.findById(userId).orElseThrow(() ->new ResourceNotFoundException("User Not found for"+userId));
		Cart cart = cartRepositary.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
		return mapper.map(cart, CartDto.class);
	}

}
