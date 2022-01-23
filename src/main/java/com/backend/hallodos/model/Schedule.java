package com.backend.hallodos.model;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule")
@Entity
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "dosenId", referencedColumnName = "id")
	private Dosen dosenId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "mhsId", referencedColumnName = "id")
	private Mahasiswa mhsId;

	private String day;

	private String timeSessionStart;

	private String timeSessionEnd;
	
	// 19:00 - 20:00 //timeStart = 19:00
	// schedule.setTimeEnd (timeStart +1)

	// ||idSession || // days // hours //
	// || =========||==================//===============//================//
	// ||1 || // Mon // 19:00 - 20:00 //
	// ||2 || // Tue // 19:00 - 20:00 //

}
