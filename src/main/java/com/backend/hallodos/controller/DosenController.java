package com.backend.hallodos.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import com.backend.hallodos.dto.SignInDto;
import com.backend.hallodos.dto.SignupDosenDto;
import com.backend.hallodos.exceptions.AuthFailException;
import com.backend.hallodos.exceptions.CustomExceptoon;
import com.backend.hallodos.model.AuthTokenDos;
import com.backend.hallodos.model.Dosen;
import com.backend.hallodos.model.Mahasiswa;
import com.backend.hallodos.repository.DosenRepository;
import com.backend.hallodos.repository.MahasiswaRepository;
import com.backend.hallodos.services.AuthService;
import com.backend.hallodos.services.SearchService;
import com.backend.hallodos.services.UserService;
import com.backend.hallodos.services.UserServiceDosen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DosenController {

	@Autowired
	MahasiswaRepository mahasiswaRepo;

	@Autowired
	DosenRepository dosenRepo;

	@Autowired
	AuthService authService;

	@Autowired SearchService searchService;

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

	// Register Start Dosen
	@GetMapping("/registerDosen")
	public String registerdosen(Model model) {
		model.addAttribute("dataDosen", new Dosen());
		return "registerDosen";
	}

	// Setelah register Dosen
	@PostMapping("/afterRegisterDosen")
	public String daftarDosen(@ModelAttribute("dataDosen") SignupDosenDto signupDosenDto, Dosen dosen, Model model) {
		// check if user is already
		Mahasiswa mahasiswa = mahasiswaRepo.findByEmail_mahasiswa2(dosen.getEmail_dosen());
		Dosen dosenAll = dosenRepo.findByEmail_dosen2(dosen.getEmail_dosen());
		System.out.println(mahasiswa);
		System.out.println(dosenAll);
		if (dosenAll != null || mahasiswa != null) {
			throw new CustomExceptoon("User Already Present");
		}
		// hash the password
		String encryptedpassword = signupDosenDto.getPassword();
		try {
			encryptedpassword = UserService.hashPassword(signupDosenDto.getPassword());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		Dosen dosenuser = new Dosen(
				dosen.getId(),
				dosen.getUsername(),
				dosen.getEmail_dosen(),
				null,
				dosen.getSecurity_question(),
				dosen.getSecurity_answer(),
				encryptedpassword,
				dosen.getGraduateFrom(),
				dosen.getMajor(),
				dosen.getAffiliate(),
				null, null, null, null, 0, 0, 0, null, 0, null, null, dosen.getTopicId());

		dosenRepo.save(dosenuser);
		// create token
		final AuthTokenDos authTokendos = new AuthTokenDos(dosenuser);
		authService.saveConfirmationTokenDos(authTokendos);
		model.addAttribute("loginData", new Dosen());
		return "loginDosen";
	}

	@GetMapping("/loginDosen")
	public String getIndex(Model model) {
		model.addAttribute("loginData", new Dosen());
		return "loginDosen";
	}

	// setelah masuk login, ini untuk menerima data dari login dosen
	@PostMapping("/afterLoginDosen")
	public String masukk(@ModelAttribute("loginData") Dosen dosen, Model model) {
		Dosen dosenAll = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		if (Objects.isNull(dosenAll)) {
			return "kenihilan";
		}
		// hash the pass
		try {
			if (!dosenAll.getPassword().equals(UserServiceDosen.hashPassword(dosen.getPassword()))) {
				throw new AuthFailException("wrong password!");
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// compare pass in DB

		// if pass match
		AuthTokenDos token = authService.getTokenDos(dosenAll);

		// retrive token
		if (Objects.isNull(token)) {
			throw new CustomExceptoon("token is not present!");
		}

		// Dosen dosenBaru = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		model.addAttribute("loginData", dosenAll);
		return "dashboarddosen";
	}

	@GetMapping("/dashboarddosen")
	public String getDashboardDosen(@ModelAttribute("loginData") Dosen dosen, Model model) {
		// System.out.println(cekEmail);
		Dosen profilDosen = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		// System.out.println(profilDosen.toString());

		if (profilDosen == null) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", profilDosen);
			return "dashboarddosen";
		}
	}

	@PostMapping("/afterDashboardDosen")
	public String afterDoashboardDosen(@ModelAttribute("loginData") Dosen dosen, Model model) {
		Dosen dosenAll = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		if (Objects.isNull(dosenAll)) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", dosenAll);
			return "profilDosen";
		}
	}

	@GetMapping("/profildosen")
	public String getProfilDosen(@ModelAttribute("loginData") Dosen dosen, Model model) {
		Dosen dosenProfile = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		if (dosenProfile == null) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", dosenProfile);
			return "editProfileDosen";
		}
	}

	@PostMapping("/editProfileDsnResult")
	public String editProfileDsnResult(@ModelAttribute("loginData") Dosen dosen, Model model) {
		Dosen dataDsnBaru = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		if (dataDsnBaru == null) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", dataDsnBaru);
			dataDsnBaru.setUsername(dosen.getUsername());
			dataDsnBaru.setEmail_dosen(dosen.getEmail_dosen());
			dataDsnBaru.setFull_name(dosen.getFull_name());
			dataDsnBaru.setGraduateFrom(dosen.getGraduateFrom());
			dataDsnBaru.setMajor(dosen.getMajor());
			dataDsnBaru.setAffiliate(dosen.getAffiliate());
			dataDsnBaru.setBirthdate(dosen.getBirthdate());
			dataDsnBaru.setPhoneNumber(dosen.getPhoneNumber());
			dataDsnBaru.setGender(dosen.getGender());
			dataDsnBaru.setAddress(dosen.getAddress());
			dataDsnBaru.setBank(dosen.getBank());
			dataDsnBaru.setAccountNumber(dosen.getAccountNumber());
			dosenRepo.save(dataDsnBaru);
			return "profilDosen";
		}
	}
	// @PostMapping("/operData")
	// public String operData (@ModelAttribute("loginData") Dosen dosen, Model
	// model){
	// // String dsnEmail = dosen.getEmail_dosen();
	// // Dosen result = dosenRepo.findByEmail_dosen(dsnEmail);
	// Dosen profilDosen = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());

	// if(profilDosen == null){
	// return "kenihilan";
	// }else{
	// model.addAttribute("dataDosen", profilDosen);
	// return "redirect:/profilDosen";
	// }
	// }

	// @GetMapping("/dashboard")
	// public ModelAndView index() {
	// ModelAndView modelAndView = new ModelAndView();
	// modelAndView.setViewName("index");
	// return modelAndView;
	// }

	// @GetMapping("/profilDosen")
	// public String getProfilDosen(@ModelAttribute("loginData") Dosen dosen, Model
	// model) {
	// Dosen profilDosen = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
	// model.addAttribute("dataDosen", profilDosen);
	// return "profilDosen";
	// }

	// untuk forgot Dosen
	@GetMapping("/forgotDosen")
	public String getforgot(Model model) {
		model.addAttribute("forgotData", new Dosen());
		return "forgotDosen";
	}

	// //ini untuk cari email dan menerima data email, untuk mencari data security
	// question dan memunculkannya
	@PostMapping("/cariEmailDosen")
	public String cariEmail(@ModelAttribute("forgotData") Dosen dosen, Model model) {
		String dsnEmail = dosen.getEmail_dosen();
		Dosen user = dosenRepo.findBySecQuest(dsnEmail);
		if (user == null) {
			// error 404
			return "kenihilan";
		} else {
			user.setSecurity_answer("");
			model.addAttribute("dataDosen", user);
			return "qSecDosen";
		}
	}

	// //ini menerima jawaban dari security question dari mahasiswa
	// }
	@PostMapping("/securityResultDosen")
	public String secResult(@ModelAttribute("dataDosen") Dosen dosen, Model model) {
		String dosenEmail = dosen.getEmail_dosen();
		String answerUser = dosen.getSecurity_answer();
		Dosen result = dosenRepo.findAnswerbyInputan2(answerUser, dosenEmail);
		if (result == null) {
			return "kenihilan";
		} else {
			model.addAttribute("newDataPassword", result);
			model.addAttribute("dataForgot", result);
			return "nPasswordDosen";
		}
	}

	// setelah menerima jawaban dari mahasiswa, form ini untuk memasukan form
	// untuk membuat password baru
	@PostMapping("/newPasswordDosen")
	public String newPassword(@ModelAttribute("newDataPassword") Dosen dosen,
			SignInDto signupDto, Model model) {
		Dosen user = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		// String mhsemail = mahasiswa.getEmail_mahasiswa();

		String encryptedpassword = dosen.getPassword();
		try {
			encryptedpassword = UserService.hashPassword(dosen.getPassword());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		user.setPassword(encryptedpassword);

		dosenRepo.save(user);
		// model.addAttribute("PasswordBaru", mahasiswa);
		return "redirect:/loginDosen";
	}

	@PostMapping("/saldoDosenTarik")
	public String getSaldoDosenTarik(@ModelAttribute("loginData") Dosen dosen, Model model) {
		Dosen saldo = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		if (Objects.isNull(saldo)) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", saldo);
			// saldo.setBalance(0);
			return "saldoDosenTarik";
		}
	}
	@PostMapping("/saldoDosen")
	public String getSaldoDosen(@ModelAttribute("loginData") Dosen dosen, Model model) {
		Dosen saldo = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		if (Objects.isNull(saldo)) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", saldo);
			saldo.setBalance(0);
			return "saldoDosen";
		}
	}

	// @GetMapping("/halamanSaldoDosen")
	// public String getHalamanSaldoDosen(@ModelAttribute("loginData") Dosen dosen,
	// Model model) {
	// Dosen saldo = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
	// if (saldo == null) {
	// return "kenihilan";
	// } else {
	// model.addAttribute("loginData", saldo);
	// return "saldoDosen";
	// }
	// }

	// @GetMapping("/saldoDosen")
	// public String showPage (Model model){
	// model.addAttribute("saldo", new Dosen());
	// return "saldoDosen";
	// }

	@PostMapping("/editsaldoDosen")
	// @Reqye
	public String saldoTerbaru(@ModelAttribute("loginData") Dosen dosen, Model model) {
		Dosen saldoDosen = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());

		// ambil data balance
		int balanceDosen = saldoDosen.getBalance();

		// ambil data dari input
		int balanceInput = 0;
		balanceInput = dosen.getBalance();

		int newBalance = balanceDosen - balanceInput;

		System.out.println(balanceDosen);
		System.out.println(balanceInput);
		System.out.println(newBalance);

		model.addAttribute("loginData", saldoDosen);

		saldoDosen.setBalance(newBalance);
		dosenRepo.save(saldoDosen);

		return "saldoDosen";

	}

	@RequestMapping(value = "/editSaldoDosen", method = RequestMethod.POST)
	public String penarikanSaldoDosen(Model model, String tarikSaldo) {
		System.out.println(tarikSaldo);
		return "saldoDosen";
	}

}
