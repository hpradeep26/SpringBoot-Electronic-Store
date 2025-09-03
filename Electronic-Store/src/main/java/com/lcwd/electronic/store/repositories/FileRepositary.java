package com.lcwd.electronic.store.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entities.File;

public interface FileRepositary extends JpaRepository<File, String> {

}
