package com.lcwd.electronic.store.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.services.FileUploadService;

@Service
public class FileUploadServiceImpl implements FileUploadService{
	
	Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class); 
	
	@Override
	public InputStream getResource(String path,String name) throws FileNotFoundException {
		String fullPath = path + File.separator + name;
		InputStream inputStream = new FileInputStream(fullPath);
		return inputStream;
	}
	

	@Override
	public String uploadImage(MultipartFile file, String path) throws IOException {
		String originalFilename = file.getOriginalFilename();
		logger.info("filename - {}",originalFilename);
		String fileName = UUID.randomUUID().toString();
		String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String fileNameWithExtension = fileName + fileExtension;
		String fullPathWithFileName  = path + File.separator + fileNameWithExtension;
		
		if(fileExtension.equalsIgnoreCase(".png")||fileExtension.equalsIgnoreCase(".jpg")||fileExtension.equalsIgnoreCase(".jpeg")) {
			// save
			File folder = new File(path);
			if(!folder.exists()) {
				folder.mkdirs();
			}
			
			//upload
			Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName),StandardCopyOption.REPLACE_EXISTING);
			return fileNameWithExtension;
			
		}else {
			throw new BadApiRequestException("File Type "+fileExtension+" is not supported");
		}
	}
	
	@Override
	public List<String> fileList(String path){
		File dir = new File(path);
		File[] listFiles = dir.listFiles();
		return listFiles != null ? Arrays.stream(listFiles).map(file -> file.getName()).collect(Collectors.toList()) : null;
	}
}
