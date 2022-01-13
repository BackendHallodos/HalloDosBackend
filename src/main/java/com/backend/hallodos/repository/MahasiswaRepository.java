package com.backend.hallodos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.hallodos.model.Mahasiswa;


public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Long> {
	@Query(value="Select*from mahasiswa where email_mahasiswa=?1 AND password =?2", nativeQuery=true)
	public Mahasiswa getMahasiswa(String email_mahasiswa, String password);
	
	@Query(value="Select*from mahasiswa where Status=?1", nativeQuery=true)
	public List<Mahasiswa> findByStatus(String status);


}
