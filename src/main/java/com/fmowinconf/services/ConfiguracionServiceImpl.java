package com.fmowinconf.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmowinconf.dto.response.ConfiguracionDTO;
import com.fmowinconf.models.Configuracion;
import com.fmowinconf.models.ConfiguracionIP;
import com.fmowinconf.models.ConfiguracionImpresora;
import com.fmowinconf.repository.ConfiguracionIPRepository;
import com.fmowinconf.repository.ConfiguracionImpresoraRepository;
import com.fmowinconf.repository.ConfiguracionRepository;

@Service
public class ConfiguracionServiceImpl {
    @Autowired
    private ConfiguracionRepository configuracionRepository;
    private ConfiguracionIPRepository configuracionIPRepository;
    private ConfiguracionImpresoraRepository configuracionImpresoraRepository;

    public Configuracion crearConfiguracion(ConfiguracionDTO configuracionDTO) {
        // Lógica para crear una configuración utilizando los repositorios
        Configuracion configuracion = new Configuracion();
        configuracion.setFmo_equipo(configuracionDTO.getFmo_equipo());
        configuracion.setConfigurar_escaner(configuracionDTO.getConfigurar_escaner());
        configuracion.setConfigurar_impresora(configuracionDTO.getConfigurar_impresora());
        configuracion.setConfigurar_ip(configuracionDTO.getConfigurar_ip());
        configuracion.setCrear_usuario(configuracionDTO.getCrear_usuario());
        configuracion.setHostname_dominio(configuracionDTO.getHostname_dominio());
        configuracion.setMozilla_firefox(configuracionDTO.getMozilla_firefox());
        configuracion.setMozilla_thunderbird(configuracionDTO.getMozilla_thunderbird());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String created_at = LocalDateTime.now().format(formatter);

        configuracion.setCreated_at(created_at);

        if(configuracionDTO.getConfigurar_ip() == 1 || configuracionDTO.getConfigurar_ip() == 2) {
            ConfiguracionIP ipConfig = new ConfiguracionIP();
            ipConfig.setIp_address(configuracionDTO.getIpConfig().getIp_address());
            ipConfig.setDefault_gateway(configuracionDTO.getIpConfig().getDefault_gateway());
            ipConfig.setSubnet_mask(configuracionDTO.getIpConfig().getSubnet_mask());
            ipConfig.setCreated_at(created_at);
            configuracion.setIpConfig(ipConfig);
        }

        if(configuracionDTO.getConfigurar_impresora() == 1 || configuracionDTO.getConfigurar_impresora() == 2) {
            ConfiguracionImpresora impresora = new ConfiguracionImpresora();
            impresora.setModelo(configuracionDTO.getImpresora().getModelo());
            impresora.setIp_address(configuracionDTO.getImpresora().getIp_address());
            impresora.setCreated_at(created_at);
            configuracion.setImpresora(impresora);
        }

        return configuracionRepository.save(configuracion);
    }
}
