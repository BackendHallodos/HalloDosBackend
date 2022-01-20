package com.backend.hallodos.repository;

import java.util.List;

import com.backend.hallodos.model.Dosen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DosenRepository extends JpaRepository<Dosen, String> {

    @Query(value = "Select*from dosen where email_dosen=?1 AND password =?2", nativeQuery = true)
    public Dosen getDosen(String email_dosen, String password);

    @Query(value = "Select*from dosen where status=?1", nativeQuery = true)
    public List<Dosen> findByStatus(String status);

    @Query(value = "SELECT*FROM dosen where email_dosen=?1", nativeQuery = true)
    public Dosen findByEmail_dosen(String email_dosen);

    @Query(value = "SELECT*FROM dosen where email_dosen=?1", nativeQuery = true)
    public Dosen findByEmail_dosen2(String email_dosen);

    @Query(value = "SELECT * FROM dosen where sequrity_answer=?1", nativeQuery = true)
    public Dosen findAnswerbyInputan(String sequrity_answer);

    @Query(value = "SELECT * FROM dosen where password=?1", nativeQuery = true)
    public Dosen findByPassword(String password);

    @Query(value = "SELECT * FROM `dosen` ORDER BY rating DESC LIMIT 3", nativeQuery = true)
    public Dosen findByRating();
    // public Dosen findAnswerbyInputan(String answerUser, String dosenEmail);
}
