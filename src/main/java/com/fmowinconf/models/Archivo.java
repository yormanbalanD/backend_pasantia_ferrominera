package com.fmowinconf.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.val;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

@Table(name = "archivo")
public class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_respaldo")
    @ToString.Exclude
    @JsonBackReference(value = "archivo-respaldo")
    private Respaldo respaldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_padre")
    @ToString.Exclude
    @JsonBackReference(value = "archivo-padre")
    private Archivo padre;

    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonManagedReference(value = "archivo-hijos")
    private List<Archivo> hijos = new ArrayList<>();

    @Column(name = "nombre_archivo")
    private String nombre_archivo;

    @Column(name = "es_carpeta")
    private int es_carpeta;

    @Column(name = "ruta")
    private String ruta;

    @Column(name = "ruta_destino")
    private String ruta_destino;

    @Column(name = "extension")
    private String extension;

    @Column(name = "tamaño")
    private String tamaño;

    @PrePersist
    public void prePersist() {
        // Verificamos que la ruta no sea nula ni vacía
        if (this.ruta != null && !this.ruta.isEmpty()) {
            
            // Si es una carpeta, generalmente no queremos extensión (opcional según tu lógica)
            // Asumiendo que 'es_carpeta' puede ser "true", "S", "1", etc.
            // Si no necesitas esta validación, puedes quitar este if.
            if (this.es_carpeta == 1) {
                this.extension = "";
                return;
            }

            // Buscamos la posición del último punto en la cadena
            int ultimoPunto = this.ruta.lastIndexOf('.');

            // Validamos:
            // 1. Que el punto exista (ultimoPunto > -1)
            // 2. Que el punto no sea el último caracter (para evitar errores de índice)
            // 3. (Opcional) Que el punto no sea el primer caracter (index > 0) para archivos ocultos de unix como .bashrc
            if (ultimoPunto > 0 && ultimoPunto < this.ruta.length() - 1) {
                this.extension = this.ruta.substring(ultimoPunto + 1);
            } else {
                // Si no tiene extensión válida
                this.extension = ""; 
            }
        }
    }
}
