package com.lcwd.electronic.store.services.impl;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CategoryRepositary;
import com.lcwd.electronic.store.repositories.ProductRepositary;
import com.lcwd.electronic.store.services.ProductService;
import com.lcwd.electronic.store.util.FileUtil;
import com.lcwd.electronic.store.util.Helper;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductRepositary productRepositary;
	
	@Autowired
	CategoryRepositary categoryRepositary;
	
	@Autowired
	ModelMapper mapper;
	
	@Value("${product.image.path}")
	private String imagePath;
	
	
	@Override
	public ProductDto save(ProductDto productDto) {
		Product product = mapper.map(productDto, Product.class);
		Product saveProduct = productRepositary.save(product);
		return mapper.map(saveProduct, ProductDto.class);
	}
	
	@Override
	public PageableResponse<ProductDto> findAll(int pageNo, int pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		Page<Product> page = productRepositary.findAll(pageRequest);
		PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
		return pageableResponse;
	}

	@Override
	@Cacheable(value = "products",key = "#productId")
	public ProductDto findById(String productId) {
		 Product product = productRepositary.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Resouce Not Found Exception for "+productId));
		 return mapper.map(product, ProductDto.class);
	}

	@Override
	@CacheEvict(value = "products",key = "#productId")
	public void delete(String productId) {
		Product product = productRepositary.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Resouce Not Found Exception for "+productId));
		if(product.getProductImageName() != null) {
			String imageFilePath = imagePath + File.separator + product.getProductImageName();
			FileUtil.deletefile(imageFilePath);
		}
		productRepositary.delete(product);
	}

	@Override
	@CachePut(value = "products",key = "#productId")
	public ProductDto update(ProductDto productDto, String productId) {
		Product product = productRepositary.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Resouce Not Found Exception for "+productId));
		product.setProductName(productDto.getProductName());
		product.setProductImageName(productDto.getProductImageName());
		product.setAvaliable(productDto.isAvaliable());
		product.setDescription(productDto.getDescription());
		Product updateProduct = productRepositary.save(product);
		return mapper.map(updateProduct, ProductDto.class);
	}

	@Override
	public List<ProductDto> findByProductName(String name) {
		List<Product> findByProductName = productRepositary.findByProductName(name);
		List<ProductDto> list = findByProductName.stream()
			.map(p -> mapper.map(p, ProductDto.class))
			.collect(Collectors.toList());
		return list;
	}
	
	@Override
	public ProductDto saveProductWithCategories(ProductDto productDto,String categoryId) {
		Category category = categoryRepositary.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Resource Not Found for"+categoryId));
		Product product = mapper.map(productDto, Product.class);
		product.setCategory(category);
		Product savedProduct = productRepositary.save(product);
		return mapper.map(savedProduct, ProductDto.class);
	}
	
	
	@Override
	public ProductDto updateProductWithCategories(String productId, String categoryId) {
		Product product = productRepositary.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Resouce Not Found Exception for "+productId));
		Category category = categoryRepositary.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Resource Not Found for"+categoryId));
		product.setCategory(category);
		Product savedProduct = productRepositary.save(product);
		return mapper.map(savedProduct, ProductDto.class);
	}
	
	@Override
	public List<ProductDto> getProductsByCategory(String categoryId){
		List<Product> findByCategory = productRepositary.findByCategoryId(categoryId);
		List<ProductDto> list = findByCategory.stream()
				.map(p -> mapper.map(p, ProductDto.class))
				.collect(Collectors.toList());
		return list;
	}
	
}
