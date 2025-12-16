package com.fmowinconf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmowinconf.repository.AnalistaRepository;

@Service
public class AnalistaServiceImpl implements IAnalistaService {

    @Autowired  
    private AnalistaRepository analistaRepository;

    public boolean existsByFicha(String ficha) {
        return analistaRepository.findByFicha(ficha) != null;
    }

}
