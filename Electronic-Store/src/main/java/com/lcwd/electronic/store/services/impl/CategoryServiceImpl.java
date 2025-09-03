package com.lcwd.electronic.store.services.impl;

import java.io.File;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CategoryRepositary;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.util.FileUtil;
import com.lcwd.electronic.store.util.Helper;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepositary categoryRepositary;

	@Autowired
	ModelMapper mapper;

	@Value("${catalog.cover.image.path}")
	private String catalogImagepath;


	@Override
	public CategoryDto save(CategoryDto categoryDto) {
		Category category = mapper.map(categoryDto, Category.class);
		Category savedCategory = categoryRepositary.save(category);
		return mapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public PageableResponse<CategoryDto> findAll(int pageNo,int pageSize,String sortBy, String sortDir) {
		Sort sortby = Sort.by(sortBy);
		Sort sort = sortDir.equalsIgnoreCase("desc") ? sortby.descending() : sortby.ascending();
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);
		Page<Category> page = categoryRepositary.findAll(pageRequest);
		PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page,CategoryDto.class);
		return pageableResponse;
	}

	@Override
	public CategoryDto findById(String categoryId) {
		Category category = categoryRepositary.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found for -"+categoryId));
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public CategoryDto findByCategoryName(String categoryName) {
		Category category = categoryRepositary.findByTitle(categoryName);
		if(category==null) {
			return null;
		}
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public void delete(String categoryId) {
		Category category = categoryRepositary.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found for -"+categoryId));
		if(category.getCoverImage() != null ) {
			String imageFilePath = catalogImagepath + File.separator +category.getCoverImage();
			FileUtil.deletefile(imageFilePath);
		}
		categoryRepositary.delete(category);
	}

	@Override
	public CategoryDto update(CategoryDto categoryDto,String categoryId) {
		Category category = categoryRepositary.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found for -"+categoryId));
		category.setTitle(categoryDto.getTitle());
		category.setDescription(categoryDto.getDescription());
		category.setCoverImage(categoryDto.getCoverImage());
		categoryRepositary.save(category);
		return mapper.map(category, CategoryDto.class);
	}



}
