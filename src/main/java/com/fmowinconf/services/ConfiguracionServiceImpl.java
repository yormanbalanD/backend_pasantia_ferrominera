package com.fmowinconf.services;

import java.io.ObjectInputFilter.Config;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmowinconf.dto.response.ConfiguracionDTO;
import com.fmowinconf.models.Configuracion;
import com.fmowinconf.models.ConfiguracionIP;
import com.fmowinconf.models.ConfiguracionImpresora;
import com.fmowinconf.models.Respaldo;
import com.fmowinconf.repository.ConfiguracionIPRepository;
import com.fmowinconf.repository.ConfiguracionImpresoraRepository;
import com.fmowinconf.repository.ConfiguracionRepository;

@Service
public class ConfiguracionServiceImpl {
    @Autowired
    private ConfiguracionRepository configuracionRepository;
    @Autowired
    private ConfiguracionIPRepository configuracionIPRepository;
    @Autowired
    private ConfiguracionImpresoraRepository configuracionImpresoraRepository;

    public Configuracion crearConfiguracion(ConfiguracionDTO configuracionDTO) {
        // Lógica para crear una configuración utilizando los repositorios
        Configuracion configuracion = new Configuracion();
        configuracion.setAnalista(configuracionDTO.getAnalista());
        configuracion.setFmo_equipo(configuracionDTO.getFmo_equipo());
        configuracion.setConfigurar_escaner(configuracionDTO.getConfigurar_escaner());
        configuracion.setConfigurar_impresora(configuracionDTO.getConfigurar_impresora());
        configuracion.setConfigurar_ip(configuracionDTO.getConfigurar_ip());
        configuracion.setHostname_dominio(configuracionDTO.getHostname_dominio());
        configuracion.setMozilla_firefox(configuracionDTO.getMozilla_firefox());
        configuracion.setMozilla_thunderbird(configuracionDTO.getMozilla_thunderbird());
        configuracion.setSistema_operativo(configuracionDTO.getSistema_operativo());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String created_at = LocalDateTime.now().format(formatter);

        configuracion.setCreated_at(created_at);

        if (configuracionDTO.getConfigurar_ip() == 1 || configuracionDTO.getConfigurar_ip() == 2) {
            ConfiguracionIP ipConfig = new ConfiguracionIP();
            ipConfig.setIp_address(configuracionDTO.getIpConfig().getIp_address());
            ipConfig.setDefault_gateway(configuracionDTO.getIpConfig().getDefault_gateway());
            ipConfig.setSubnet_mask(configuracionDTO.getIpConfig().getSubnet_mask());
            ipConfig.setCreated_at(created_at);
            configuracion.setIpConfig(ipConfig);
        }

        if (configuracionDTO.getConfigurar_impresora() == 1 || configuracionDTO.getConfigurar_impresora() == 2) {
            ConfiguracionImpresora impresora = new ConfiguracionImpresora();
            impresora.setModelo(configuracionDTO.getImpresora().getModelo());
            impresora.setIp_address(configuracionDTO.getImpresora().getIp_address());
            impresora.setCreated_at(created_at);
            configuracion.setImpresora(impresora);
        }

        return configuracionRepository.save(configuracion);
    }

    public List<Configuracion> obtenerConfiguracionEntreFechas(String fechaInicio, String fechaFin) {
        // Asumimos que el input es solo "yyyy-MM-dd", así que completamos la hora
        // para asegurarnos de que la búsqueda incluya todo el rango.

        // Inicio del día X
        String inicioCompleto = fechaInicio.contains(":") ? fechaInicio : fechaInicio + " 00:00:00";

        // Final del día Y
        String finCompleto = fechaFin.contains(":") ? fechaFin : fechaFin + " 23:59:59";

        return configuracionRepository.buscarPorRangoDeFechas(inicioCompleto, finCompleto);
    }

    public List<Configuracion> obtenerPorFmoEquipo(String fmo_equipo) {
        return configuracionRepository.findByFmoEquipoStartingWithIgnoreCase(fmo_equipo);
    }

    public List<Configuracion> obtenerPorFichaAnalista(String ficha) {
        return configuracionRepository.findByAnalistaFichaStartingWithIgnoreCase(ficha);
    }

    public List<Configuracion> obtenerPorNombreAnalista(String nombre) {
        return configuracionRepository.findByAnalistaNombreCompletoStartingWithIgnoreCase(nombre);
    }

    public List<Configuracion> obtenerPorSistemaOperativo(String sistema_operativo) {
        return configuracionRepository.findBySistemaOperativoStartingWith(sistema_operativo);
    }

    public List<Configuracion> obtenerTodasLasConfiguraciones() {
        return configuracionRepository.findAllWithAnalista();
    }

    public List<Configuracion> obtenerConfiguracionesPorFichaAnalistaExacta(String ficha) {
        return configuracionRepository.findByAnalistaFicha(ficha);
    }

    public List<Configuracion> obtenerPorAnalista(Long id_analista) {
        return configuracionRepository.findByAnalistaId(id_analista);
    }

    public Configuracion ocultarConfiguracion(Long id) {
        try {
            Configuracion existingConfiguracion = configuracionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Configuración no encontrada con id: " + id));

            existingConfiguracion.setVisible(0);

            return configuracionRepository.save(existingConfiguracion);
        } catch (Exception e) {
            throw new RuntimeException("Error al ocultar la configuración: " + e.getMessage());
        }
    }

    public Configuracion editarFmoEquipoConfiguracion(Long id, String fmo_equipo) {
        Optional<Configuracion> data = configuracionRepository.findById(id);

        if (data.isEmpty()) {
            throw new RuntimeException("Configuración no encontrada con id: " + id);
        } else {
            Configuracion existingConfiguracion = data.get();
            existingConfiguracion.setFmo_equipo(fmo_equipo);
            return configuracionRepository.save(existingConfiguracion);
        }
    }
}