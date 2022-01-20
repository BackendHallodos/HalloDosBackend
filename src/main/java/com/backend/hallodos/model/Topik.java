package com.backend.hallodos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
@Data
@Table (name ="topik")
@Entity

public class Topik {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	String name_topik;
	String deskripsi;


}
