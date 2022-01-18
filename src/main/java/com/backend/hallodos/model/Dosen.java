package com.backend.hallodos.model;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dosen")
@Entity
public class Dosen {

	public Dosen(String username2, String email_dosen2, Object full_name2, String security_question2,
            String security_answer2, String encryptedpassword, String graduateFrom2, String major2, String affiliate2,
            Object birthdate2, Object phoneNumber2, Object gender2, Object address2, Object object, Object object2,
            Object object3, Object photos2, Object object4, Object bank2, Object accountNumber2, Object topicId2) {
    }

    @Id
	private String username;
	private String email_dosen;
	private String full_name;
	private String security_question;
	private String security_answer;
	private String password;
	private String graduateFrom;
	private String major;
	private String affiliate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")	
	private String birthdate;
	private String phoneNumber;
	private String gender;
	private String address;
	private int rating;
	private int totalConsultation;
	private int price;
	private String photos;
	private int balance;
	private String bank;
	private String accountNumber;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn (name="topicId", referencedColumnName="id")
	private Topik topicId;

}
