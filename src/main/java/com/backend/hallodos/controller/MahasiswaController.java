package com.backend.hallodos.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
// import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import com.backend.hallodos.config.FileUploadUtil;
import com.backend.hallodos.dto.SignInDto;
import com.backend.hallodos.dto.SignupDto;
import com.backend.hallodos.exceptions.AuthFailException;
import com.backend.hallodos.exceptions.CustomExceptoon;
import com.backend.hallodos.model.AuthToken;
import com.backend.hallodos.model.Dosen;
import com.backend.hallodos.model.Mahasiswa;
import com.backend.hallodos.model.Schedule;
import com.backend.hallodos.model.Topik;
import com.backend.hallodos.repository.DosenRepository;
import com.backend.hallodos.repository.MahasiswaRepository;
import com.backend.hallodos.repository.ScheduleRepository;
import com.backend.hallodos.repository.TopikRepository;
import com.backend.hallodos.services.AuthService;
import com.backend.hallodos.services.SearchService;
import com.backend.hallodos.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MahasiswaController {
	@Autowired
	DosenRepository dosenRepo;

	@Autowired
	AuthService authService;

	@Autowired
	MahasiswaRepository mahasiswaRepo;

	@Autowired
	TopikRepository topikRepo;

	@Autowired
	SearchService searchService;

	@Autowired
	ScheduleRepository scheduleRepo;

	@PostMapping("/mahasiswa/save")
	public RedirectView saveUser(Mahasiswa mahasiswa,
			@RequestParam(value = "image") MultipartFile multipartFile) throws IOException {

		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

		mahasiswa.setPhotos(fileName);

		Mahasiswa saveMaha = mahasiswaRepo.save(mahasiswa);

		String uploadDir = "user-photos/" + saveMaha.getUsername();

		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		return new RedirectView("/profilemahasiswa", true);
	}

	// Tampilan awal dasboard awal mahasiswa
	@GetMapping("/dasboard")
	public String getdasboard(Model model, Principal principal) {
		return "index";
	}

	// proses penggabungan dengan frontend
	// untuk login mahasiswa
	@GetMapping("/loginMahasiswa")
	public String getIndex(Model model) {
		model.addAttribute("loginData", new Mahasiswa());
		return "LoginMahasiswa";
	}

	// setelah masuk login, ini untuk menerima data dari login
	@PostMapping("/afterLogin")
	public String masukk(@ModelAttribute("loginData") Mahasiswa mahasiswa, Dosen dosen, Topik topik, Model model) {
		Mahasiswa maha = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		if (Objects.isNull(maha)) {
			return "kenihilan";
		}
		// hash the pass
		try {
			if (!maha.getPassword().equals(UserService.hashPassword(mahasiswa.getPassword()))) {
				throw new AuthFailException("wrong password!");
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// compare pass in DB

		// if pass match
		AuthToken token = authService.getToken(maha);

		// retrive token
		if (Objects.isNull(token)) {
			throw new CustomExceptoon("token is not present!");
		}
		List<Dosen> topDos = dosenRepo.findByRating();
		List<Topik> topikDashboard = topikRepo.findAll();
		model.addAttribute("loginData", maha);
		model.addAttribute("dataTopDos", topDos);
		model.addAttribute("dataTopik", topikDashboard);
		return "dashboarduser";
	}

	@GetMapping("/dashboarduser")
	public String getDashboardDosen(@ModelAttribute("loginData") Mahasiswa mahasiswa, Model model) {
		Mahasiswa maha = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		if (maha == null) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", maha);

			return "dashboarduser";
		}
	}

	// @PostMapping("/searchPage")
	// public String searchPage(@ModelAttribute("loginData") Mahasiswa mahasiswa,
	// Model model) {
	// Mahasiswa maha =
	// mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
	// Keyword keywordBaru = new Keyword();
	// keywordBaru.setKeyWord(keywordBaru.getKeyWord());
	// if (Objects.isNull(maha)) {
	// return "kenihilan";
	// } else {
	// model.addAttribute("loginData", maha);
	// model.addAttribute("dataSearch", keywordBaru);
	// return "searchpage";
	// }
	// }

	// @PostMapping("/searchfor")
	// public String searchfor(@ModelAttribute("loginData") Mahasiswa mahasiswa,
	// Model model) {
	// Mahasiswa maha =
	// mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
	// List<Dosen> searchResult = dosenRepo.findAllDosenByWord(dataInputSearch);
	// String dataInputSearch = "";
	// if (Objects.isNull(maha)) {
	// return "kenihilan";
	// } else {
	// model.addAttribute("loginData", maha);
	// model.addAttribute("dataSearchInput", dataInputSearch);
	// return "searchpage";
	// }
	// }

	// @PostMapping("/searchResult")
	// public String searchResult(@ModelAttribute("loginData") Mahasiswa
	// mahasiswa,@ModelAttribute("dataSearchInput") Dosen dosen, Model model) {
	// Mahasiswa maha =
	// mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
	// List<Dosen> dataResult = dosenRepo.findAllDosenByWord();
	// if (Objects.isNull(maha)) {
	// return "kenihilan";
	// } else {
	// model.addAttribute("loginData", maha);
	// return "searchpage";
	// }
	// }

	@PostMapping("/afterDashboardMahasiswa")
	public String afterDashboardMahasiswa(@ModelAttribute("loginData") Mahasiswa mahasiswa, Model model) {
		Mahasiswa maha = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		if (Objects.isNull(maha)) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", maha);
			return "profilmahasiswa";
		}

	}

	@GetMapping("/profilmahasiswa")
	public String getprofilmahasiswa(@ModelAttribute("loginData") Mahasiswa mahasiswa, Model model) {
		Mahasiswa maha = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		if (maha == null) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", maha);
			return "editProfileMahasiswa";
		}
	}

	@PostMapping("/editProfileMhsResult")
	public String editProfileMhsResult(@ModelAttribute("loginData") Mahasiswa mahasiswa, Model model) {
		Mahasiswa dataMhsBaru = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		if (dataMhsBaru == null) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", dataMhsBaru);
			dataMhsBaru.setUsername(mahasiswa.getUsername());
			dataMhsBaru.setEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
			dataMhsBaru.setFull_name(mahasiswa.getFull_name());
			dataMhsBaru.setDatebrith(mahasiswa.getDatebrith());
			dataMhsBaru.setGender(mahasiswa.getGender());
			dataMhsBaru.setAlamat(mahasiswa.getAlamat());
			dataMhsBaru.setAsal_sekolah(mahasiswa.getAsal_sekolah());
			dataMhsBaru.setProgram_study(mahasiswa.getProgram_study());
			mahasiswaRepo.save(dataMhsBaru);
			return "profilmahasiswa";
		}
	}

	@PostMapping("/saveProfileMhs")
	public String saveProfileMhs(@ModelAttribute("loginData") Mahasiswa mahasiswa, Model model) {
		Mahasiswa dataMhsBaru = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		if (dataMhsBaru == null) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", dataMhsBaru);
			// mahasiswaRepo.saveAndFlush(data)
			mahasiswaRepo.save(dataMhsBaru);
			return "profilemahasiswa";
		}
	}

	// untuk forgot
	@GetMapping("/forgotMahasiswa")
	public String getforgot(Model model) {
		model.addAttribute("forgotData", new Mahasiswa());
		return "forgotMahasiswa";
	}

	// ini untuk cari email dan menerima data email, untuk mencari data security
	// question dan memunculkannya
	@PostMapping("/cariEmail")
	public String cariEmail(@ModelAttribute("forgotData") Mahasiswa mahasiswa, Model model) {
		String mhsemail = mahasiswa.getEmail_mahasiswa();
		Mahasiswa user = mahasiswaRepo.findBySecQuest(mhsemail);
		if (user == null) {
			// error 404
			return "kenihilan";
		} else {
			user.setSecurity_answer("");
			model.addAttribute("datamhs", user);
			return "qSecMahas";
		}

		// ini menerima jawaban dari security question dari mahasiswa
	}

	@PostMapping("/securityResult")
	public String secResult(@ModelAttribute("datamhs") Mahasiswa mahasiswa, Model model) {
		String mhsemail = mahasiswa.getEmail_mahasiswa();
		// Mahasiswa user = mahasiswaRepo.findBySecQuest(mhsemail);
		// String questionUser = mahasiswa.getSequrity_question();
		String answerUser = mahasiswa.getSecurity_answer();
		Mahasiswa result = mahasiswaRepo.findAnswerbyInputan(answerUser, mhsemail);
		if (result == null) {
			return "kenihilan";
		} else {
			model.addAttribute("newDataPassword", result);
			model.addAttribute("dataForgot", result);
			return "nPasswordMaha";
		}
	}

	// setelah menerima jawaban dari mahasiswa, form ini untuk memasukan form untuk
	// membuat password baru
	@PostMapping("/newPassword")
	public String newPassword(@ModelAttribute("newDataPassword") Mahasiswa mahasiswa, SignInDto signupDto,
			Model model) {
		Mahasiswa user = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		// String mhsemail = mahasiswa.getEmail_mahasiswa();

		String encryptedpassword = mahasiswa.getPassword();
		try {
			encryptedpassword = UserService.hashPassword(mahasiswa.getPassword());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		user.setPassword(encryptedpassword);

		mahasiswaRepo.save(user);
		// model.addAttribute("PasswordBaru", mahasiswa);
		return "redirect:/loginMahasiswa";
	}

	// Register Start mahasiswa

	@GetMapping("/registerMahasiswa")
	public String register(Model model) {
		model.addAttribute("data", new Mahasiswa());
		return "registerMahasiswa";
	}

	@PostMapping("/afterRegisterMaha")
	public String daftar(@ModelAttribute("data") SignupDto signupDto, Mahasiswa maha, Model model) {
		Dosen dosen = dosenRepo.findByEmail_dosen2(maha.getEmail_mahasiswa());
		// check if user is already
		if (Objects.nonNull(mahasiswaRepo.findByEmail_mahasiswa(maha.getEmail_mahasiswa())) || dosen != null) {
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
				maha.getId(),
				signupDto.getUsername(),
				encryptedpassword, null,
				maha.getSecurity_question(),
				maha.getSecurity_answer(), null, null,
				maha.getEmail_mahasiswa(),
				null, null, null, null, null);

		mahasiswaRepo.save(user);

		// create token
		final AuthToken authToken = new AuthToken(user);
		authService.saveConfirmationToken(authToken);

		model.addAttribute("loginData", new Mahasiswa());
		return "loginMahasiswa";
	}

	@GetMapping("/data")
	public String getData() {
		return "index";
	}

	@GetMapping("/getuser")
	public String getUser(Model model) {
		return "user";

	}

	@RequestMapping("/searchpage")
	public String searchPage(@ModelAttribute("loginData") Mahasiswa mahasiswa, Model model,
			@Param("keyword") String keyword) {
		Mahasiswa emailMahasiswa = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		List<Dosen> listDosen = searchService.listDosenAll(keyword);
		model.addAttribute("loginData", emailMahasiswa);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listDosen", listDosen);
		return "searchpage";
	}

	@PostMapping("/keDetailDsn")
	public String keDetailDsn(@ModelAttribute("loginData") Mahasiswa mahasiswa, Model model, Dosen dosen) {
		Mahasiswa maha = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		Dosen dsnOnClicked = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		if (Objects.isNull(maha) && Objects.isNull(dsnOnClicked)) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", maha);
			model.addAttribute("DataDsn", dsnOnClicked);
			model.addAttribute("dataSchedule", new Schedule());
			return "detaildosen";
		}

	}

	// @GetMapping("/bookingdosen")
	// public String bookingdosen(@ModelAttribute("loginData") Mahasiswa mahasiswa,
	// Model model, Dosen dosen) {
	// Mahasiswa emailMaha =
	// mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
	// Dosen dsnOnClicked = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
	// model.addAttribute("loginData", emailMaha);
	// model.addAttribute("DataDsn", dsnOnClicked);
	// model.addAttribute("dataSchedule", new Schedule());
	// return "redirect:/keBookingDsn";
	// }

	@PostMapping("/keBookingDsn")
	public String keBookingDsn(@ModelAttribute("loginData") Mahasiswa mahasiswa, Model model, Dosen dosen) {
		Mahasiswa maha = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		Dosen dsnOnClicked = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		if (Objects.isNull(maha) && Objects.isNull(dsnOnClicked)) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", maha);
			model.addAttribute("DataDsn", dsnOnClicked);
			model.addAttribute("dataSchedule", new Schedule());
			return "bookingpayment";
		}
	}

	@PostMapping("/schedulebooking")
	public String schedulebooking(@RequestParam(value = "loginData") String mahasiswa, Model model,
			@RequestParam(value = "DataDsn") String dosen,
			Schedule schedule) {

		Mahasiswa maha = mahasiswaRepo.findById(Long.parseLong(mahasiswa)).get();
		Dosen dsnOnClicked = dosenRepo.findById(Long.parseLong(dosen)).get();
		// Dosen dsnID = scheduleRepo.insertDataIncludeForeign(dsnOnClicked.getId(),
		// maha.getId());
		// dosen.setId(dsnOnClicked.getId();
		// schedule.setDosenId(dsnOnClicked);
		// long operDsnId = schedule.getDosenId().getId();
		// Dosen dsnById = dosenRepo.findDosenById(operDsnId);
		// schedule.setDosenId(dsnById);
		// scheduleRepo.save(schedule);

		// schedule.setMhsId(maha);

		Schedule dataHasilInput = new Schedule(
				dsnOnClicked,
				maha,
				schedule.getDay(),
				schedule.getTimeSessionStart(),
				schedule.getTimeSessionEnd());

		// dataHasilInput.setId(dsnOnClicked.getId());
		// dataHasilInput.setMhsId(maha.getClass());
		// dataHasilInput.setId(schedule.getId());
		// dataHasilInput.setDosenId(dsnOnClicked.getId());
		// dataHasilInput.setMhsId(maha.getId());
		// dataHasilInput.setDay(schedule.getDay());
		// dataHasilInput.getTimeSessionStart(schedule.getTimeSessionStart());

		scheduleRepo.save(dataHasilInput);
		model.addAttribute("loginData", maha);
		model.addAttribute("DataDsn", dsnOnClicked);
		model.addAttribute("dataBook", dataHasilInput);
		return "paymentResult";
	}

}
// @GetMapping("/detaildosen")
// public String detaildosen(@ModelAttribute("loginData") Mahasiswa mahasiswa,
// Model model, Dosen dosen) {
// Mahasiswa emailMaha =
// mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
// Dosen dsnOnClicked = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
// model.addAttribute("loginData", emailMaha);
// model.addAttribute("DataDsn", dsnOnClicked);
// return "detaildosen";
// }