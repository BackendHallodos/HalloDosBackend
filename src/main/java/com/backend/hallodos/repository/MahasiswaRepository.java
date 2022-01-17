package com.backend.hallodos.repository;

import java.util.List;

import com.backend.hallodos.model.Mahasiswa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MahasiswaRepository extends JpaRepository<Mahasiswa, String> {
	@Query(value="Select*from mahasiswa where email_mahasiswa=?1 AND password =?2", nativeQuery=true)
	public Mahasiswa getMahasiswa(String email_mahasiswa, String password);
	
	@Query(value="Select*from mahasiswa where status=?1", nativeQuery=true)
	public List<Mahasiswa> findByStatus(String status);

	@Query(value="SELECT*FROM mahasiswa where email_mahasiswa=?1",nativeQuery=true)
    public Mahasiswa findByEmail_mahasiswa(String email_mahasiswa);

	@Query(value="SELECT * FROM mahasiswa where email_mahasiswa=?1",nativeQuery=true)
    public Mahasiswa findBySecQuest(String email_mahasiswa);

	@Query(value="SELECT * FROM mahasiswa where sequrity_answer=?1",nativeQuery=true)
    public Mahasiswa findAnswerbyInputan(String sequrity_answer);

	@Query(value="SELECT * FROM mahasiswa where password=?1",nativeQuery=true)
    public Mahasiswa findByPassword(String password);




   







}
