package com.lcwd.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entities.Category;

public interface CategoryRepositary extends JpaRepository<Category, String>{
	
	Category findByTitle(String title);

}
