package com.backend.hallodos.model;

import javax.persistence.*;

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
	private String username;
	private String password;
	private String nama_mahasiswa;
	private String role;
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
