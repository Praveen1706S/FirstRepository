package com.movieflix.movieApi.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.movieflix.movieApi.exceptions.FileExistException;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadFile(String path, MultipartFile file) throws IOException {
		
		// get the file name
		if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
			
			throw new FileExistException("File already exists! please enter another file name");
		}
		
		String fileName= file.getOriginalFilename();
		
		// get the file path
		
		String filePath= path + File.separator + fileName;
		
		
		// create file object
		File f = new File(path);
		
		// if the folder is not exist then to create new folder
		if(!f.exists()) {
			
			f.mkdir();
		}
		
		
		// copy the file or upload file to the path
		
		Files.copy(file.getInputStream(), Paths.get(filePath));
		
		
		return fileName;
	}

	@Override
	public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
		
		String filePath = path + File.separator + fileName;
		
		return new FileInputStream(filePath);
	}

}
