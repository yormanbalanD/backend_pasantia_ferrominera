package com.fmowinconf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmowinconf.models.Archivo;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivo, Long> {
    
}
