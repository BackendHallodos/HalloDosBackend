package com.backend.hallodos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.backend.hallodos.model.FileDb;

@Repository
public interface FileDbRepository extends JpaRepository<FileDb, String> {

}
