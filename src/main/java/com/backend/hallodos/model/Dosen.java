package com.backend.hallodos.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dosen")
@Entity
public class Dosen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String emailDosen;

	private String nameDosen;
	private String password;
	private String bidangStudi;
	private String dosen;
	private String userName;
	private String sequrity_question;
	private String sequrity_answer;

}
