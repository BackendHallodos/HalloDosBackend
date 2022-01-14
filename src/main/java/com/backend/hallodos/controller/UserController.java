package com.backend.hallodos.controller;

import java.security.Principal;

// import java.io.IOException;
// import java.util.List;

import com.backend.hallodos.model.repository.MahasiswaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
;



@Controller
public class UserController {
	
	@Autowired
	MahasiswaRepository mahasiswaRepo;

	// untuk dasboard
	//dan autentication
	@GetMapping("/dasboard")
	public String getdasboard(Model model,Principal principal) {
	return "index";
	}
	@GetMapping("/mahasiswa")
	public String getMahasiswa(Model model) {
		
		return "mahasiswa";
	}
	@GetMapping("/dosen")
	public String getDosen(Model model) {
	
		return "dosen";
	}



	
	@GetMapping("/loginUser")
	public String getIndex(Model model) {
		// model.addAttribute("object", new Mahasiswa());
		return "login";
	}

	
	
//	//get untuk memunculkan profil mahasiswa
//	@GetMapping("/profilMahasiswa")
//	public String Mahasiswa(Model model) {
//		        model.addAttribute("fotoUser",new Hallodos.model.entities.Mahasiswa().getPhotos());
//	List<Hallodos.model.entities.Mahasiswa> profil = mahasiswaRepo.findAll();
//	model.addAttribute("data",profil);
//	return("profilmahasiswa");
//	}
//	 @PostMapping("/mahasiswa/save")
//	    public RedirectView saveUser(Hallodos.model.entities.Mahasiswa mahasiswa,
//	        @RequestParam(value="id") MultipartFile multipartFile) throws IOException {
//	        
//	        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//	        
//	        mahasiswa.setPhotos(fileName);
//	        
//	        Hallodos.model.entities.Mahasiswa saveMaha = mahasiswaRepo.save(mahasiswa);
//	        
//	        String uploadDir = "user-photos/" + saveMaha.getId();
//	        
//	        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//	        
//	        return new RedirectView("/profilMahasiswa", true);
//	    }
}
	

