package com.backend.hallodos.repository;



import com.backend.hallodos.model.AuthToken;
import com.backend.hallodos.model.Mahasiswa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<AuthToken, Long>{

    AuthToken findByUser(Mahasiswa user);
    
}
