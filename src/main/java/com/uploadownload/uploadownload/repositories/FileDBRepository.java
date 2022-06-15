package com.uploadownload.uploadownload.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uploadownload.uploadownload.entities.FileDB;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, String>{

}
