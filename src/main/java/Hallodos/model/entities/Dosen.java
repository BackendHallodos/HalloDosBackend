package Hallodos.model.entities;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="dosen")
@Entity
public class Dosen {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String emailDosen;

	private String nameDosen;
	private String password;
	private String bidangStudi;


	private String dosen;

}
