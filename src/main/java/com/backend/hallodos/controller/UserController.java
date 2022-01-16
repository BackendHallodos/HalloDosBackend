package com.backend.hallodos.controller;

import java.security.Principal;

import com.backend.hallodos.model.Mahasiswa;
import com.backend.hallodos.repository.MahasiswaRepository;

// import java.io.IOException;
// import java.util.List;


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

	
//Register Start
@GetMapping("/register")
public String register(Model model) {
	model.addAttribute("data", new Mahasiswa());
	return "register";
}


}
	

