package com.fmowinconf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmowinconf.dto.response.RespaldoDTO;
import com.fmowinconf.models.Respaldo;
import com.fmowinconf.repository.RespaldoRepository;

@Service
public class RespaldoServiceImpl {
    @Autowired
    private RespaldoRepository respaldoRepository;    
    
    public Respaldo crearRespaldo(RespaldoDTO respaldoDTO){
        Respaldo respaldo = new Respaldo();
        respaldo.setAnalista(respaldoDTO.getAnalista());
        respaldo.setFmo_equipo(respaldoDTO.getFmo_equipo());
        respaldo.setSistema_operativo(respaldoDTO.getSistema_operativo());
        respaldo.setCreated_at(respaldoDTO.getCreated_at());
        respaldo.setCompletado_con_exito(2);

        return respaldoRepository.save(respaldo);
    }

    public Respaldo finalizarRespaldo(Respaldo respaldo, int estadoFinalizacion) {
        respaldo.setCompletado_con_exito(estadoFinalizacion);
        return respaldoRepository.save(respaldo);
    }
}