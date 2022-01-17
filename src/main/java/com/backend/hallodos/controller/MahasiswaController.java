package com.backend.hallodos.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
// import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import com.backend.hallodos.config.FileUploadUtil;
import com.backend.hallodos.dto.SignInDto;
import com.backend.hallodos.exceptions.AuthFailException;
import com.backend.hallodos.exceptions.CustomExceptoon;
import com.backend.hallodos.model.AuthToken;
import com.backend.hallodos.model.Mahasiswa;
import com.backend.hallodos.repository.MahasiswaRepository;
import com.backend.hallodos.services.AuthService;
import com.backend.hallodos.services.UserService;

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



@Controller
public class MahasiswaController {
	
	@Autowired
    AuthService authService;

	@Autowired
	MahasiswaRepository mahasiswaRepo;
	

	//get untuk memunculkan profil mahasiswa
	@GetMapping("/profilemahasiswa")
	public String Mahasiswa(Model model) {
		        // model.addAttribute("fotoUser",new Mahasiswa());
	List<Mahasiswa> profil = mahasiswaRepo.findByStatus("ON");
	model.addAttribute("data",profil);
	return("profilmahasiswa");
	}
	 @PostMapping("/mahasiswa/save")
	    public RedirectView saveUser(Mahasiswa mahasiswa,
	        @RequestParam(value="image") MultipartFile multipartFile) throws IOException {
	
	        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				
	        mahasiswa.setPhotos(fileName);
	        
	        Mahasiswa saveMaha = mahasiswaRepo.save(mahasiswa);
	        
	        String uploadDir = "user-photos/" + saveMaha.getUsername();
	        
	        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
	        
	        return new RedirectView("/profilemahasiswa", true);
	    }

	 
	@GetMapping("/login")
	public String getIndex(Model model) {
		model.addAttribute("data", new Mahasiswa());
		return "login";
	}
	@PostMapping("/masukk")
	public String masukk(@ModelAttribute("data")SignInDto signInDto, Model model) {
		Mahasiswa user = mahasiswaRepo.findByEmail_mahasiswa(signInDto.getEmail());
	
		if (Objects.isNull(user)) {
			return "profil";
		}
		//hash the pass
		try {
			if (!user.getPassword().equals(UserService.hashPassword(signInDto.getPassword()))) {
				throw new AuthFailException("wrong password!");
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		//compare pass in DB
	
		//if pass match
		AuthToken token = authService.getToken(user);
	
		//retrive token
		if (Objects.isNull(token)) {
			throw new CustomExceptoon("token is not present!");
		}
		return "index";
	}
	

	// //from login mahasiswa
	// @PostMapping("/loginM")
	// public String postLoginM(@ModelAttribute Mahasiswa mahasiswa,Model model) {
	// 	Mahasiswa data = mahasiswaRepo.getMahasiswa(mahasiswa.getEmail_mahasiswa(),mahasiswa.getPassword());
	// 	if(data !=null) {
	// 		data.setStatus("ON");
	// 		mahasiswaRepo.save(data);
	// 		model.addAttribute("user", data);
	// 		model.addAttribute("object", new Mahasiswa());
	// 		return "redirect:/data";
	// 	}
	// 	else {
	// 		return"redirect:/loginM";
	// 	}
	// }
@GetMapping("/data")
public String getData() {
	return "index";
}
@GetMapping("/getuser")
public String getUser(Model model) {
	return "user";
	
}
}
