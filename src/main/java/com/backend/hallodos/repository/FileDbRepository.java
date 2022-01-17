package com.backend.hallodos.repository;



import com.backend.hallodos.model.FileDb;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface FileDbRepository extends JpaRepository<FileDb, String> {

}
