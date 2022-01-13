package com.backend.hallodos.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.backend.hallodos.config.FileUploadUtil;
import com.backend.hallodos.model.Mahasiswa;
import com.backend.hallodos.repository.MahasiswaRepository;

@Controller
public class MahasiswaController {
	
	@Autowired
	MahasiswaRepository mahasiswaRepo;
	

	//get untuk memunculkan profil mahasiswa
	@GetMapping("/profilemahasiswa")
	public String Mahasiswa(Model model) {
		        model.addAttribute("fotoUser",new Mahasiswa().getPhotos());
	List<Mahasiswa> profil = mahasiswaRepo.findByStatus("ON");
	model.addAttribute("data",profil);
	model.addAttribute("fotoUser",profil);
	return("profilmahasiswa");
	}
	 @PostMapping("/mahasiswa/save")
	    public RedirectView saveUser(Mahasiswa mahasiswa,
									 @RequestParam(value="fotoUser") MultipartFile multipartFile) throws IOException {
	        
	        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());    
	        mahasiswa.setPhotos(fileName);
	        
	        Mahasiswa saveMaha = mahasiswaRepo.save(mahasiswa);
	        
	        String uploadDir = "user-photos/" + saveMaha.getId();
	        
	        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	        
	        return new RedirectView("/profilemahasiswa", true);
	    }

	 
	@GetMapping("/loginMa")
	public String getIndex(Model model) {
		model.addAttribute("object", new Mahasiswa());
		return "login";
	}
	//from login mahasiswa
	@PostMapping("/loginM")
	public String postLoginM(@ModelAttribute Mahasiswa mahasiswa,Model model) {
		Mahasiswa data = mahasiswaRepo.getMahasiswa(mahasiswa.getEmail_mahasiswa(),mahasiswa.getPassword());
		if(data !=null) {
			data.setStatus("ON");
			mahasiswaRepo.save(data);
			model.addAttribute("user", data);
			model.addAttribute("object", new Mahasiswa());
			return "redirect:/data";
		}
		else {
			return"redirect:/loginM";
		}
	}
@GetMapping("/data")
public String getData() {
	return "index";
}
@GetMapping("/getuser")
public String getUser(Model model) {
	return "user";
	
}
}
