package com.backend.hallodos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import com.backend.hallodos.repository.MahasiswaRepository;


@Controller
public class UserController {
	
	@Autowired
	MahasiswaRepository mahasiswaRepo;
	// untuk dasboard
	@GetMapping("/dasboard")
	public String getdasboard() {
	return "index";
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
	

