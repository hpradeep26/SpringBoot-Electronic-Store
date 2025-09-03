package com.lcwd.electronic.store.services.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.entities.File;
import com.lcwd.electronic.store.repositories.FileRepositary;
import com.lcwd.electronic.store.services.FileSaveService;

@Service
public class FileSaveServiceImpl implements FileSaveService{
	
	@Autowired
	private FileRepositary fileRepositary;
	
	@Override
	public String saveFile(MultipartFile file) throws IOException {
		String filename = file.getOriginalFilename();
		String contentType = file.getContentType();
		byte[] bytes = file.getBytes();
		File savefile = new File(filename,contentType,bytes);
		fileRepositary.save(savefile);
	   return "File saved successfully";
	}


}
