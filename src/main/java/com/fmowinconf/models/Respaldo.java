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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

@Table(name = "respaldo")
public class Respaldo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analista_id")
    @ToString.Exclude
    @JsonBackReference(value = "respaldos-analista")
    private Analista analista;

    @Column(name = "fmo_equipo")
    private String fmo_equipo;
    @Column(name = "sistema_operativo")
    private String sistema_operativo;
    @Column(name = "created_at")
    private String created_at;

    @Column(name = "completado_con_exito")
    private int completado_con_exito; // 1 = COMPLETADO, 0 = ERROR, 2 = EN PROCESO

    @OneToMany(mappedBy = "respaldo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonManagedReference(value = "archivo-respaldo")
    private List<Archivo> archivos = new ArrayList<>();
}
