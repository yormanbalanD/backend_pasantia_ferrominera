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
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.OneToMany;
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

    // nuevas relaciones
    @OneToMany(mappedBy = "analista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Configuracion> configuraciones = new ArrayList<>();

    @OneToMany(mappedBy = "analista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Respaldo> respaldos = new ArrayList<>();
}
