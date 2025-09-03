package com.lcwd.electronic.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lcwd.electronic.store.entities.Product;

public interface ProductRepositary extends JpaRepository<Product, String> {
	
	List<Product> findByProductName(@Param("name") String name);
	
	@Query("select p from Product p where p.category.categoryId = ?1")
	List<Product> findByCategoryId(String categoryId);

}
