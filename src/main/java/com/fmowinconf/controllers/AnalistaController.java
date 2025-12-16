package com.fmowinconf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fmowinconf.repository.AnalistaRepository;
import com.fmowinconf.services.IAnalistaService;

@RestController
public class AnalistaController {

    @Autowired
    private IAnalistaService analistaService;



    @GetMapping("/usuario/{ficha}")
    public ResponseEntity<?> existsByFicha(@PathVariable String ficha) {

        return ResponseEntity.ok(analistaService.existsByFicha(ficha));
    }


}
