package com.lcwd.electronic.store.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
	
	String uploadImage(MultipartFile file,String path) throws IOException;

	InputStream getResource(String path, String name) throws FileNotFoundException;
	
	public List<String> fileList(String path);
	

}
