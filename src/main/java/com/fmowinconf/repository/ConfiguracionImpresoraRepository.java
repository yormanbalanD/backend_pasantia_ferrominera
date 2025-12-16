package com.fmowinconf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.fmowinconf.models.ConfiguracionImpresora;

@Repository
public interface ConfiguracionImpresoraRepository extends JpaRepository<ConfiguracionImpresora, Long> {
    
}
