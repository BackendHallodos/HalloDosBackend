package com.backend.hallodos.repository;

import java.util.List;

import com.backend.hallodos.model.Dosen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DosenRepository extends JpaRepository<Dosen, Long> {

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

    @Query(value = "SELECT * FROM dosen ORDER BY rating DESC LIMIT 4", nativeQuery = true)
    public List<Dosen> findByRating();

    @Query(value = "SELECT * FROM dosen where email_dosen=?1 ", nativeQuery = true)
    public Dosen findBySecQuest(String dsnEmail);

    // untuk jawaban dari user forget pasword
    @Query(value = "SELECT * FROM dosen where security_answer=?1 AND email_dosen=?2", nativeQuery = true)

    public Dosen findAnswerbyInputan2(String security_answer, String email_dosen);

    @Query(value = "select dosen.*, topik.name_topik from dosen INNER JOIN topik ON dosen.topic_id = topik.id WHERE name_topik LIKE %?1% OR full_name LIKE %?1% OR major LIKE %?1%", nativeQuery = true)
    public List<Dosen> findAllDosenByWord(String keyword);

    public Dosen findDosenById(long operDsnId);

    @Query(value = "Select*from dosen where username=?1", nativeQuery = true)
	public Dosen findByUsername(String username);

    
}
