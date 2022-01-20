package com.backend.hallodos.repository;

import com.backend.hallodos.model.Topik;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TopikRepository extends JpaRepository<Topik, Long>{
    
}
