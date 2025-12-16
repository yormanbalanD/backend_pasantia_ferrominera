package com.fmowinconf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmowinconf.models.Respaldo;

@Repository
public interface RespaldoRepository extends JpaRepository<Respaldo, Long> {

}
