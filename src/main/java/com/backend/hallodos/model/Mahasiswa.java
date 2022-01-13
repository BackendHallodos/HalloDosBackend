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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String nama_mahasiswa;
	
	private String datebrith;
	
	private String gender;

	private String type_user;
	
	private String email_mahasiswa;
	
	private String password;
	
	private String alamat;
	
	private String asal_sekolah;
	
	private String program_study;
	
	private String status;
	private String photos;

	
}
