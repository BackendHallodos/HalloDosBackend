package com.backend.hallodos.model;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="mahasiswa")
@Entity
public class Mahasiswa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String username;
	private String password;
	private String full_name;
	private String security_question;
	private String security_answer;
	@DateTimeFormat(pattern = "yyyy-MM-dd")	
	private String datebrith;
	private String gender;
	private String email_mahasiswa;
	private String alamat;
	private String asal_sekolah;
	private String program_study;
	private String status;
	private String photos;
	@Transient
    public String getPhotosImagePath() {
        if (photos == null || username == null) return null;  
        return "/user-photos/" + username + "/" + photos;
    }
}
