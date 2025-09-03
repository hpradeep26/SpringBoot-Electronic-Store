package com.lcwd.electronic.store.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.services.FileUploadService;
import com.lcwd.electronic.store.services.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/products")
@Validated
@Tag(name = "Product", description = "Product API")
public class ProductController {
	
	private final ProductService productService;
	
	private final FileUploadService fileUploadService;
	
	@Value("${product.image.path}")
	private String productImagePath;

	
	public ProductController(ProductService productService,FileUploadService fileUploadService) {
		this.productService = productService;
		this.fileUploadService = fileUploadService;
	}
	
	@PostMapping
	public ResponseEntity<ProductDto> save(@Valid @RequestBody ProductDto productDto){
		ProductDto savedProductDto = productService.save(productDto);
		return new ResponseEntity<>(savedProductDto,HttpStatus.CREATED);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") String productId){
		ProductDto productDto = productService.findById(productId);
		return new ResponseEntity<>(productDto,HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAll(
			@RequestParam(name = "pageNumber",defaultValue = "0", required = false)  int pageNo,
			@RequestParam(name = "PageSize", defaultValue = "10", required = false) int pagesize){
		 PageableResponse<ProductDto> pageableResponse = productService.findAll(pageNo, pagesize);
		 return ResponseEntity.ok(pageableResponse);
	}
	
	@PatchMapping("/{productId}")
	public ResponseEntity<ProductDto> update(@Valid @RequestBody ProductDto productDto,
			@PathVariable("productId") String productId){
		ProductDto savedProductDto = productService.update(productDto, productId);
		return new ResponseEntity<>(savedProductDto,HttpStatus.OK);
	}
	
	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> delete(@PathVariable String productId){
		productService.delete(productId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping("/image/{productId}")
	public ResponseEntity<ApiResponseMessage> uploadImage(@RequestParam("image") MultipartFile image,
			@PathVariable("productId") String productId) throws IOException{
		ProductDto productDto = productService.findById(productId);
		String imageName = fileUploadService.uploadImage(image, productImagePath);
		productDto.setProductImageName(imageName);
		productService.update(productDto, productId);
		ApiResponseMessage message = new ApiResponseMessage();
		message.setHttpStatus(HttpStatus.CREATED);
		message.setMessage("Image uploaded Successfully");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	@GetMapping("/image/{productId}")
	public void serverImage(@PathVariable("productId") String productId,
			HttpServletResponse response) throws IOException {
		ProductDto productDto = productService.findById(productId);
		InputStream inputStream = fileUploadService.getResource(productImagePath, productDto.getProductImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(inputStream,response.getOutputStream());
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<ProductDto>> searchByProductName(@RequestParam("name") String name){
		 List<ProductDto> list = productService.findByProductName(name);
		 return new ResponseEntity<>(list,HttpStatus.OK);
	}
	
	
	
}
