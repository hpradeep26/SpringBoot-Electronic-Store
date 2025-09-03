package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

public interface CategoryService {
	
	CategoryDto save(CategoryDto categoryDto);
	
	PageableResponse<CategoryDto> findAll(int pageNo,int pageSize,String sortBy, String sortDir);
	
	CategoryDto findById(String categoryId);
	
	void delete(String dategoryId);
	
	CategoryDto update(CategoryDto categoryDto, String categoryId);

	CategoryDto findByCategoryName(String categoryName);
	

}
