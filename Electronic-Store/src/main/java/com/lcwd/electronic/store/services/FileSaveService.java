package com.lcwd.electronic.store.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;


public interface FileSaveService {
	
	public String saveFile(MultipartFile file) throws IOException;
}
