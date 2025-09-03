package com.lcwd.electronic.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UpdateOrderRequest;
import com.lcwd.electronic.store.services.OrderService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Order", description = "Order API")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
		OrderDto createOrder = orderService.createOrder(createOrderRequest);
		return new ResponseEntity<>(createOrder,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseMessage> deleteOrder(@PathVariable String orderId){
		orderService.removeOrder(orderId);
		ApiResponseMessage message = new ApiResponseMessage();
		message.setHttpStatus(HttpStatus.OK);
		message.setMessage("Order Deleted successful");
		return new ResponseEntity<>(message,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/{userId}")
	public ResponseEntity<List<OrderDto>> getOrderByUser(@PathVariable String userId){
		List<OrderDto> orderByUser = orderService.getOrderByUser(userId);
		return new ResponseEntity<>(orderByUser,HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<PageableResponse<OrderDto>> getAllUsers(
			@RequestParam(value ="pageSize",  required = false, defaultValue = "10") Integer pageSize,
			@RequestParam(value = "pageNumber",required = false,defaultValue = "0") Integer pageNumber,
			@RequestParam(value = "sortBy", required = false, defaultValue = "orderedDate") String sortBy,
			@RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir){
		PageableResponse<OrderDto> pageableResponse = orderService.getAll(pageNumber,pageSize,sortBy,sortDir);
		return ResponseEntity.ok(pageableResponse);
	}
	
	@PatchMapping("/{orderId}")
	public ResponseEntity<OrderDto> updateOrder(@Valid @RequestBody UpdateOrderRequest updateOrderRequest,@PathVariable String orderId) {
		OrderDto updatedOrder = orderService.updateOrder(updateOrderRequest,orderId);
		return new ResponseEntity<>(updatedOrder,HttpStatus.OK);
	}
	
	
}
