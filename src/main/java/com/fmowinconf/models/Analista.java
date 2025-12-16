package com.fmowinconf.models;

import jakarta.annotation.Generated;
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
import lombok.Builder.Default;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

@Table(name = "analista")
public class Analista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "ficha")
    private String ficha;
    @Column(name = "nombre_completo")
    private String nombre_completo;
    @Column(name = "password")
    private String password;
    @Column(name = "created_at")
    private String created_at;
    @Column(name = "permisos")
    private String permisos;

    // nuevas relaciones
    @OneToMany(mappedBy = "analista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonManagedReference(value = "configuraciones-analista")
    private List<Configuracion> configuraciones = new ArrayList<>();

    @OneToMany(mappedBy = "analista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonManagedReference(value = "respaldos-analista")
    private List<Respaldo> respaldos = new ArrayList<>();
}
