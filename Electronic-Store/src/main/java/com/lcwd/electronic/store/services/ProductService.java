package com.lcwd.electronic.store.services;

import java.util.List;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

public interface ProductService {
	
	ProductDto save(ProductDto productDto);
	
	PageableResponse<ProductDto> findAll(int pageNo,int pageSize);
	
	ProductDto findById(String productId);
	
	void delete(String productId);
	
	ProductDto update(ProductDto productDto, String productId);

	List<ProductDto> findByProductName(String name);

	ProductDto saveProductWithCategories(ProductDto productDto, String categoryId);

	ProductDto updateProductWithCategories(String productId, String categoryId);

	List<ProductDto> getProductsByCategory(String categoryId);
	
}
