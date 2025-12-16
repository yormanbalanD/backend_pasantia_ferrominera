package com.fmowinconf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fmowinconf.models.Analista;

@Repository
public interface AnalistaRepository extends JpaRepository <Analista, Long> {
}
