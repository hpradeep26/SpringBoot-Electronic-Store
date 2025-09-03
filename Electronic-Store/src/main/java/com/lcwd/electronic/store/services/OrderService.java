package com.lcwd.electronic.store.services;

import java.util.List;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UpdateOrderRequest;

public interface OrderService {
	
	OrderDto createOrder(CreateOrderRequest createOrderRequest);
	
	void removeOrder(String orderId);
	
	List<OrderDto> getOrderByUser(String userId);
	
	PageableResponse<OrderDto> getAll(int pageNo, int pageSize, String sortBy,String sortDir);
	
	OrderDto updateOrder(UpdateOrderRequest updateOrderRequest,String orderId);
}
