package com.backend.hallodos.controller;

import java.security.NoSuchAlgorithmException;

import java.util.Objects;

import com.backend.hallodos.dto.SignupDosenDto;
import com.backend.hallodos.dto.SignupDto;

import com.backend.hallodos.exceptions.CustomExceptoon;
import com.backend.hallodos.model.AuthToken;
import com.backend.hallodos.model.Mahasiswa;
import com.backend.hallodos.model.Dosen;
import com.backend.hallodos.repository.DosenRepository;
import com.backend.hallodos.repository.MahasiswaRepository;
import com.backend.hallodos.services.AuthService;
import com.backend.hallodos.services.UserService;

// import java.io.IOException;

// import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;;

@Controller
public class UserController {

	@Autowired
	MahasiswaRepository mahasiswaRepo;

	@Autowired
	DosenRepository dosenRepo;

	@Autowired
	AuthService authService;

	// untuk dasboard
	// dan autentication

	@GetMapping("/mahasiswa")
	public String getMahasiswa(Model model) {

		return "mahasiswa";
	}

	@GetMapping("/dosen")
	public String getDosen(Model model) {
		return "dosen";
	}

	// Register Start
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("data", new Mahasiswa());
		return "register";
	}

	@GetMapping("/registerdosen")
	public String registerDosen(Model model) {
		model.addAttribute("data_dosen", new Dosen());
		return "registerdosen";
	}

	@PostMapping("/daftar")
	public String daftar(@ModelAttribute("data") SignupDto signupDto, Mahasiswa maha, Model model) {
		// check if user is already
		if (Objects.nonNull(mahasiswaRepo.findByEmail_mahasiswa(signupDto.getEmail()))) {
			throw new CustomExceptoon("User Already Present");
		}
		// hash the password
		String encryptedpassword = signupDto.getPassword();

		try {
			encryptedpassword = UserService.hashPassword(signupDto.getPassword());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		Mahasiswa user = new Mahasiswa(
				signupDto.getUsername(),
				encryptedpassword, null,
				maha.getSequrity_question(),
				maha.getSequrity_answer(), null, null,
				maha.getEmail_mahasiswa(),
				null, null, null, null, null);

		mahasiswaRepo.save(user);

		// create token
		final AuthToken authToken = new AuthToken(user);
		authService.saveConfirmationToken(authToken);

		model.addAttribute("logindata", new Mahasiswa());
		return "login";
	}

	@PostMapping("/daftardosen")
	public String daftarDosen(@ModelAttribute("data_dosen") SignupDosenDto signupDosenDto, Dosen dosen, Model model) {
		// check if user is already
		if (Objects.nonNull(dosenRepo.findByEmail_dosen(signupDosenDto.getEmail()))) {
			throw new CustomExceptoon("User Already Present");
		}
		// hash the password
		String encryptedpassword = signupDosenDto.getPassword();

		try {
			encryptedpassword = UserService.hashPassword(signupDosenDto.getPassword());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		Dosen user = new Dosen(
				signupDosenDto.getUsername(),
				encryptedpassword, null,
				dosen.getSequrity_question(),
				dosen.getSequrity_answer(), null, null,
				dosen.getEmailDosen(),
				null, null, null, null, null);

		dosenRepo.save(user);

		// create token
		// final AuthToken authToken = new AuthToken(user);
		// authService.saveConfirmationToken(authToken);

		model.addAttribute("logindata", new Dosen());
		return "login";
	}

}
