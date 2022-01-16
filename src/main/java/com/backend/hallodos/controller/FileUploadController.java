package com.backend.hallodos.controller;

import java.io.IOException;
import java.util.List;

import com.backend.hallodos.model.FileDb;
import com.backend.hallodos.services.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("file")
public class FileUploadController{
	@Autowired
	FileUploadService fileUploadService;
	
//	@PostMapping
//	public void UploadFile(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
//		fileUploadService.uploadFile(file);
//	}
	@PostMapping
	public FileDb uploadFile(@RequestParam("file")MultipartFile file)throws IOException{
		return fileUploadService.store(file);
	}
	@GetMapping("/id")
	public FileDb getFile(@PathVariable String Id){
		return fileUploadService.getFileById(Id);
	}
	@GetMapping("/{id}")
	public List<FileDb> getFileList(){
		return fileUploadService.getFileList();
	}
}
	