package com.backend.hallodos.repository;

import com.backend.hallodos.model.AuthTokenDos;
import com.backend.hallodos.model.Dosen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDosenRepository extends JpaRepository<AuthTokenDos,Long>{
    AuthTokenDos findByDosen(Dosen dosen);
}
