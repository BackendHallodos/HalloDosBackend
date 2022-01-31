package com.backend.hallodos.controller;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.sql.Date;
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
import com.backend.hallodos.model.History;
import com.backend.hallodos.model.Mahasiswa;
import com.backend.hallodos.model.Schedule;
import com.backend.hallodos.model.Topik;
import com.backend.hallodos.repository.DosenRepository;
import com.backend.hallodos.repository.HistoryRepository;
import com.backend.hallodos.repository.MahasiswaRepository;
import com.backend.hallodos.repository.ScheduleRepository;
import com.backend.hallodos.repository.TopikRepository;
import com.backend.hallodos.services.AuthService;
import com.backend.hallodos.services.SearchService;
import com.backend.hallodos.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@Autowired
	HistoryRepository historyRepo;

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

	@PostMapping("/dashboardmhs")
	public String dashboard(@ModelAttribute("loginData") Mahasiswa mahasiswa, Dosen dosen, Topik topik, Model model) {
		Mahasiswa maha = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		if (Objects.isNull(maha)) {
			return "kenihilan";
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
		return "ForgotMahasiswa";
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
			return "qSecMahasiswa";
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
			return "nPasswordMahasiswa";
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
		model.addAttribute("dataMaha", new Mahasiswa());
		return "RegisterMahasiswa";
	}

	@PostMapping("/afterRegisterMaha")
	public String daftar(@ModelAttribute("dataMaha") SignupDto signupDto, Mahasiswa maha, Model model) {
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

	@PostMapping("/consultpagemhs")
	public String consultPagemhs(@ModelAttribute("loginData") Mahasiswa mahasiswa, Dosen dosen, Model model) {
		Mahasiswa dataMaha = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		Dosen dataDsn = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		List<Schedule> waitingList = scheduleRepo.findByMahaIdWaiting(dataMaha.getId());
		List<Schedule> acceptedList = scheduleRepo.findByMahaIdAccepted(dataMaha.getId());
		List<Schedule> finishedList = scheduleRepo.findByMahaIdFinished(dataMaha.getId());
		if (Objects.isNull(dataMaha)) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", dataMaha);
			model.addAttribute("loginDosen", dataDsn);
			model.addAttribute("listConsult", waitingList);
			model.addAttribute("listAccepted", acceptedList);
			model.addAttribute("listFinished", finishedList);
			return "consultpagemhs";
		}
	}

	@PostMapping("/otwchatmhs")
	public ResponseEntity<Object> otwchatmhs(@ModelAttribute("loginData") Mahasiswa mahasiswa, Dosen dosen,
			Schedule schedule,
			Model model) {
		Dosen dosenAll = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		Mahasiswa dataMaha = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		long idDosenTsb = dosenAll.getId();
		long idMhsTsb = dataMaha.getId();
		Schedule skejul = scheduleRepo.findByForeignId(idDosenTsb, idMhsTsb);
		System.out.println(skejul.toString());
		// if (Objects.isNull(dataMaha)) {
		// return "kenihilan";
		// } else {
		skejul.setStatus("Finished");
		scheduleRepo.save(skejul);
		model.addAttribute("loginData", dataMaha);
		model.addAttribute("listConsult", skejul);
		// return "redirect:/kechatmhs?username=" + dataMaha.getUsername();
		return ResponseEntity.status(HttpStatus.FOUND)
				.location(URI.create("https://8444-149-110-56-201.ngrok.io/login?username=" + dataMaha.getUsername()))
				.build();
		// }
	}

	// @GetMapping("/kechatmhs")
	// ResponseEntity<Void> redirect(@RequestParam(value = "username") String
	// username) {
	// return ResponseEntity.status(HttpStatus.FOUND)
	// .location(URI.create("http://5781-149-110-56-201.ngrok.io/login?username=" +
	// username))
	// .build();
	// }

	@RequestMapping(path = "/backToLocal", method = RequestMethod.GET)
	public String keConsultDariChat(@RequestParam(value = "username") String username, Model model) {
		// String usernameDariSono = (String)
		// request.getSession().getAttribute("username");
		Mahasiswa dataLogin = mahasiswaRepo.findByUsername(username);
		if (Objects.nonNull(dataLogin)) {
			model.addAttribute("loginData", dataLogin);
			return "profilmahasiswa";
		} else {
			Dosen dataDosen = dosenRepo.findByUsername(username);
			model.addAttribute("loginData", dataDosen);
			return "profildosen";
		}
	}

	// @PostMapping("/declineConsult")
	// public String declineResult(@ModelAttribute("loginData") Dosen dosen,
	// Mahasiswa mahasiswa, Schedule schedule,
	// Model model) {
	// Dosen dosenAll = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
	// Mahasiswa dataMaha =
	// mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
	// List<Schedule> jadwalKu = scheduleRepo.findByDosenId(dosenAll.getId());
	// long idDosenTsb = dosenAll.getId();
	// long idMhsTsb = dataMaha.getId();
	// Schedule skejul = scheduleRepo.findByForeignId(idDosenTsb, idMhsTsb);
	// if (Objects.isNull(dosenAll)) {
	// return "kenihilan";
	// } else {
	// skejul.setStatus("Declined");
	// scheduleRepo.delete(skejul);
	// model.addAttribute("loginData", dosenAll);
	// model.addAttribute("listConsult", jadwalKu);
	// return "redirect:/halamanconsult/" + dosenAll.getEmail_dosen();
	// }
	// }

	// @GetMapping("/halamanconsult/{Email_dosen}")
	// public String halamanconsult(@PathVariable("Email_dosen") String dosen, Model
	// model) {
	// Dosen dosenProfile = dosenRepo.findByEmail_dosen(dosen);
	// List<Schedule> jadwalKu = scheduleRepo.findByDosenId(dosenProfile.getId());
	// model.addAttribute("loginData", dosenProfile);
	// model.addAttribute("listConsult", jadwalKu);
	// return "consultpagedsn";
	// }

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
		// id si ranger = id di database
		// jika di db memiliki kombinasi idmhs dan dosenmhs yang sama dari input, maka
		// sebutkan customexceptoon
		// kalo (find by dosenid & mahasiswa id) not null, book alrady exittst
		// schedule repo.findByidmhsAnddosenId maha.getID

		// Objects.nonNull(mahasiswaRepo.findByEmail_mahasiswa(maha.getEmail_mahasiswa()))
		// || dosen != null
		// Objects.nonNull(scheduleRepo.findByForeignId(maha.getId(),
		// dsnOnClicked.getId()))
		// mahasiswaRepo.findById(Long.parseLong(mahasiswa)).get() !=null &&
		// dosenRepo.findById(Long.parseLong(dosen)).get() != null

		// jika id mhs di db == id mhs di input & id dsn di db == id dsn di input, throw
		// error
		// if schedule.getdosenId() && schedule.getMhsId() == maha.getId() &&
		// dosen.getId()
		// schedule.getDosenId().getId() && schedule.getMhsId().getId() ==
		// dsnOnClicked.getId() && maha.getId()
		//
		// if (maha.getId() == schedule.getMhsId().getId() && dsnOnClicked.getId() ==
		// schedule.getDosenId().getId()) {
		// throw new CustomExceptoon("Your book already exists");
		// // return "kenihilan";
		// }
		Schedule dataHasilInput = new Schedule(
				dsnOnClicked,
				maha,
				schedule.getDay(),
				schedule.getTimeSessionStart(),
				schedule.getTimeSessionEnd());

		List<Schedule> cekdata = scheduleRepo.findByDsnIdAndMhsId(dataHasilInput.getDosenId().getId(),
				dataHasilInput.getMhsId().getId());

		if (cekdata.size() > 0) {
			throw new CustomExceptoon("Your book already exists");
		}

		// if
		// (Objects.nonNull(scheduleRepo.findByForeignId(dataHasilInput.getDosenId().getId(),
		// dataHasilInput.getMhsId().getId())) ) {
		// throw new CustomExceptoon("Your book already exists");
		// // return "kenihilan";
		// }

		dataHasilInput.setStatus("Waiting");
		scheduleRepo.save(dataHasilInput);
		model.addAttribute("loginData", maha);
		model.addAttribute("DataDsn", dsnOnClicked);
		model.addAttribute("dataBook", dataHasilInput);
		return "paymentResult";
	}

	@PostMapping("/ratingpage")
	public String ratingPage(@ModelAttribute("loginData") Mahasiswa mahasiswa, Dosen dosen, Schedule schedule,
			Model model) {
		Mahasiswa maha = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		Dosen dataDosen = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		Schedule hasilSchedule = scheduleRepo.findByForeignId(dataDosen.getId(), maha.getId());
		int income = 23750;
		String date = historyRepo.getDate();
		if (Objects.isNull(maha)) {
			return "kenihilan";
		} else {

			History inputHistory = new History(
					dataDosen,
					maha,
					income,
					date);

			dataDosen.setBalance(dataDosen.getBalance() + income);
			dosenRepo.save(dataDosen);
			inputHistory.setDate(date);
			historyRepo.save(inputHistory);
			model.addAttribute("dataDsn", dataDosen);
			model.addAttribute("loginData", maha);
			return "ratingDosen";
		}

	}

	// @PostMapping("/rating")
	// public String getSaldoDosen(@ModelAttribute("loginData") Dosen dosen, Model
	// model, Mahasiswa mahasiswa) {
	// Dosen dosenAll = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
	// Mahasiswa mahasiswaAll =
	// mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
	// if (Objects.isNull(dosenAll)) {
	// return "kenihilan";
	// } else {
	// model.addAttribute("loginData", dosenAll);
	// model.addAttribute("loginData2", mahasiswaAll);
	// return "ratingDosen";
	// }
	// }

	@PostMapping("/editRating")
	public String ratingTerbaru(@ModelAttribute("loginData") Dosen dosen, Mahasiswa mahasiswa, Model model,
			Schedule schedule) {
		Dosen dataDosen = dosenRepo.findByEmail_dosen(dosen.getEmail_dosen());
		Mahasiswa dataLogin = mahasiswaRepo.findByEmail_mahasiswa(mahasiswa.getEmail_mahasiswa());
		Schedule skejul = scheduleRepo.findByForeignId(dataDosen.getId(), dataLogin.getId());
		// ambil data rating dari input
		int inputRating = dosen.getRating();

		// ambil data total consultation
		int consul = dataDosen.getTotalConsultation() + 1;

		// Nambah total rating
		int totalRating = dataDosen.getTotalRating() + inputRating;

		int newRating = totalRating / consul;

		skejul.setStatus("Rated");
		scheduleRepo.save(skejul);
		dataDosen.setTotalConsultation(consul);
		dataDosen.setTotalRating(totalRating);
		dataDosen.setRating(newRating);
		dosenRepo.save(dataDosen);

		return "redirect:/profileMahasiswa/" + dataLogin.getId();

	}

	@GetMapping("/profileMahasiswa/{id}")
	public String ratingDosen(@PathVariable("id") long mahasiswa, Model model) {
		Mahasiswa profileMahasiswa = mahasiswaRepo.findById(mahasiswa).get();
		if (profileMahasiswa == null) {
			return "kenihilan";
		} else {
			model.addAttribute("loginData", profileMahasiswa);
			return "profilmahasiswa";
		}
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