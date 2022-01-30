package com.backend.hallodos.repository;

import java.sql.Date;
import java.util.List;

import com.backend.hallodos.model.History;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query(value = "SELECT NOW()", nativeQuery = true)
    public String getDate();

    @Query(value = "SELECT * FROM history where dosen_id = ?1 AND withdrawal != 0", nativeQuery = true)
    public List<History> findWithdrawalById(long id);

    @Query(value = "SELECT * FROM history where dosen_id = ?1 AND income != 0", nativeQuery = true)
    public List<History> findIncomeById(long id);

}
