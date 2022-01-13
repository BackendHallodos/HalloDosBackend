package Hallodos.model.entities;

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
@Table(name="schedule")
@Entity
public class Schedule {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	 @ManyToOne(cascade = CascadeType.MERGE)
	 @JoinColumn(name = "id_mahasiswa", referencedColumnName = "id")
	private Mahasiswa id_mahasiswa;
	 
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "id_dosen", referencedColumnName = "id")
	private Dosen id_dosen;
	
	private Date date;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	 @JoinColumn(name = "id_topik", referencedColumnName = "id")
	private Topik id_topik;
	

}
