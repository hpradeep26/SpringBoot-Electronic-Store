package com.lcwd.electronic.store.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileUploadService;
import com.lcwd.electronic.store.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
@Validated
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	public ProductService productService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Value("${catalog.cover.image.path}")
	private String catalogImagepath;
	
	@PostMapping
	public ResponseEntity<CategoryDto> save(@Valid @RequestBody CategoryDto categoryDto){
		CategoryDto saveCategoryDto = categoryService.save(categoryDto);
		return new ResponseEntity<CategoryDto>(saveCategoryDto, HttpStatus.CREATED);
		
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAll(
			@RequestParam(name = "pageNo",defaultValue = "0", required = false)  int pageNo,
			@RequestParam(name = "pagesize",defaultValue = "10", required = false) int pageSize,
			@RequestParam(name = "sortBy", required = false, defaultValue = "categoryId") String sortBy,
			@RequestParam(name = "sortDir",required = false, defaultValue = "asc") String sortDir){
		PageableResponse<CategoryDto> pageableResponse = categoryService.findAll(pageNo, pageSize, sortBy, sortDir);
		return ResponseEntity.ok(pageableResponse);
	}
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> findById(@PathVariable String categoryId){
		CategoryDto categoryDto = categoryService.findById(categoryId);
		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
	}
	
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> update(@Valid @RequestBody CategoryDto categoryDto,@PathVariable String categoryId){
		CategoryDto updateCategoryDto = categoryService.update(categoryDto, categoryId);
		return new ResponseEntity<CategoryDto>(updateCategoryDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponseMessage> delete(@PathVariable String categoryId){
		categoryService.delete(categoryId);
		ApiResponseMessage message = new ApiResponseMessage();
		message.setHttpStatus(HttpStatus.NO_CONTENT);
		message.setMessage("Resouce deleted Succesfully");
		return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
	}
	
	
	@GetMapping("/search")
	public ResponseEntity<CategoryDto> serachCategoryBytitle(@RequestParam String name) {
		CategoryDto categoryDto = categoryService.findByCategoryName(name);
		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);
	}
	
	@PostMapping("/image/{categoryId}")
	public ResponseEntity<ApiResponseMessage> uploadImage(@RequestParam("image") MultipartFile image,@PathVariable String categoryId) throws IOException{
		CategoryDto categoryDto = categoryService.findById(categoryId);
		String imageName = fileUploadService.uploadImage(image, catalogImagepath);
		categoryDto.setCoverImage(imageName);
		categoryService.update(categoryDto, categoryId);
		ApiResponseMessage message = new ApiResponseMessage();
		message.setHttpStatus(HttpStatus.CREATED);
		message.setMessage("Image uploaded Successfully");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	@PostMapping("/{categoryId}/products")
	public ResponseEntity<ProductDto> save(@Valid @RequestBody ProductDto productDto,
			@PathVariable("categoryId") String categoryId){
		ProductDto savedProductDto = productService.saveProductWithCategories(productDto, categoryId);
		return new ResponseEntity<>(savedProductDto,HttpStatus.CREATED);
	} 
	
	@PutMapping("/{categoryId}/products/{productId}")
	public ResponseEntity<ProductDto> save(@PathVariable("productId") String productId,
			@PathVariable("categoryId") String categoryId){
		ProductDto savedProductDto = productService.updateProductWithCategories(productId, categoryId);
		return new ResponseEntity<>(savedProductDto,HttpStatus.OK);
	} 
	
	@GetMapping("/{categoryId}/products")
	public ResponseEntity<List<ProductDto>> findProductsByCategory(@PathVariable("categoryId") String categoryId){
		List<ProductDto> productDtos  = productService.getProductsByCategory(categoryId);
		return new ResponseEntity<>(productDtos,HttpStatus.OK);
	}
	
	
	
}
