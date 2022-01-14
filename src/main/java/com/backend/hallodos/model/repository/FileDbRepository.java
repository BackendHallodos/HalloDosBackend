package com.backend.hallodos.model.repository;

import com.backend.hallodos.model.entities.FileDb;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface FileDbRepository extends JpaRepository<FileDb, String> {

}
