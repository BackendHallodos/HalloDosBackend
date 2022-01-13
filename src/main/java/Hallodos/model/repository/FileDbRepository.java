package Hallodos.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import Hallodos.model.entities.FileDb;

@Repository
public interface FileDbRepository extends JpaRepository<FileDb, String> {

}
