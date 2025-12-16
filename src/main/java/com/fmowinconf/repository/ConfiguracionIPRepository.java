package com.fmowinconf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fmowinconf.models.ConfiguracionIP;

@Repository
public interface ConfiguracionIPRepository extends JpaRepository<ConfiguracionIP, Long> {
    
}
