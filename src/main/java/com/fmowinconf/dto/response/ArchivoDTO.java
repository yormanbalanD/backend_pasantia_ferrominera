package com.fmowinconf.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fmowinconf.models.Archivo;
import com.fmowinconf.models.Respaldo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

@Data
public class ArchivoDTO {
    private long id;
    private RespaldoDTO respaldo;
    private ArchivoDTO padre;

    private List<ArchivoDTO> hijos = new ArrayList<>();
    private String nombre_archivo;
    private String es_carpeta;
    private String ruta;
    private String extension;
    private String tamaño;
}
