package com.lcwd.electronic.store.services.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UpdateOrderRequest;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Order;
import com.lcwd.electronic.store.entities.OrderItem;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartRepositary;
import com.lcwd.electronic.store.repositories.OrderRepositary;
import com.lcwd.electronic.store.repositories.UserRepositary;
import com.lcwd.electronic.store.services.OrderService;
import com.lcwd.electronic.store.util.Helper;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	OrderRepositary orderRepositary;
	
	@Autowired
	UserRepositary userRepositary;
	
	@Autowired
	CartRepositary cartRepositary;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	public OrderDto createOrder(CreateOrderRequest createOrderRequest) {
		String userId = createOrderRequest.getUserId();
		
		User user = userRepositary.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found for"+userId));
		Cart cart = user.getCart();
		
		Order order = new Order();
		order.setBillingAddress(createOrderRequest.getBillingAddress());
		order.setBillingName(createOrderRequest.getBillingName());
		order.setPhoneNumber(createOrderRequest.getBillingPhone());
		order.setOrderedDate(new Date());
		order.setPaymentStatus(createOrderRequest.getPaymentStatus());
		order.setOrderStatus(createOrderRequest.getOrderStatus());
		order.setUser(user);
		
		List<CartItem> cartItems = cart.getCartItems();
		AtomicReference<Double> totalAmount = new AtomicReference<>(0.00);
		List<OrderItem> orderItems = cartItems.stream()
		.map(cartItem -> {
			OrderItem orderItem = new OrderItem();
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setTotalPrice(cartItem.getTotalPrice());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(order);
			totalAmount.set(totalAmount.get() + cartItem.getTotalPrice());
			return orderItem;
		}).collect(Collectors.toList());
		
		
		order.setOrderItems(orderItems);
		order.setOrderAmount(totalAmount.get());
		
		cart.getCartItems().clear();
		cartRepositary.save(cart);
		Order savedOrder = orderRepositary.save(order);
		return modelMapper.map(savedOrder, OrderDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		orderRepositary.deleteById(orderId);
	}

	@Override
	public List<OrderDto> getOrderByUser(String userId) {
		User user = userRepositary.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found for"+userId));
		List<Order> orderList = orderRepositary.findByUser(user);
		List<OrderDto> orderDtoList = orderList.stream()
		 .map(o -> modelMapper.map(o, OrderDto.class))
		 .collect(Collectors.toList());
		return orderDtoList;
	}

	@Override
	public PageableResponse<OrderDto> getAll(int pageNo, int pageSize, String sortBy,String sortDir) {
		Sort by = Sort.by(sortBy);
		Sort sort = sortDir.equalsIgnoreCase("desc") ? by.descending() : by.ascending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Order> pageOrders = orderRepositary.findAll(pageable);
		PageableResponse<OrderDto> pageableResponse = Helper.getPageableResponse(pageOrders, OrderDto.class);
		return pageableResponse;
	}

	@Override
	public OrderDto updateOrder(UpdateOrderRequest updateOrderRequest,String orderId) {
		Order order = orderRepositary.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order Not found for"+orderId));
		order.setBillingAddress(updateOrderRequest.getBillingAddress());
		order.setBillingName(updateOrderRequest.getBillingName());
		order.setPhoneNumber(updateOrderRequest.getBillingPhone());
		order.setPaymentStatus(updateOrderRequest.getPaymentStatus());
		order.setOrderStatus(updateOrderRequest.getOrderStatus());
		order.setDeliveryDate(updateOrderRequest.getDeliveryDate());
		Order updatedOrder = orderRepositary.save(order);
		return modelMapper.map(updatedOrder, OrderDto.class);
	}

}
