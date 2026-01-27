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
import lombok.Builder.Default;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

@Table(name = "configuracion")
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "analista_id")
    @ToString.Exclude
    // @JsonBackReference(value = "configuraciones-analista")
    @JsonIgnoreProperties({"respaldos", "configuraciones"})
    private Analista analista;

    @Column(name = "fmo_equipo")
    private String fmo_equipo;

    @Column(name = "mozilla_firefox")
    private int mozilla_firefox;
    @Column(name = "mozilla_thunderbird")
    private int mozilla_thunderbird;
    @Column(name = "hostname_dominio")
    private int hostname_dominio;
    @Column(name = "configurar_impresora")
    private int configurar_impresora;
    @Column(name = "configurar_escaner")
    private int configurar_escaner;
    @Column(name = "configurar_ip")
    private int configurar_ip;
    @Column(name = "created_at")
    private String created_at;
    @Column(name = "sistema_operativo")
    private String sistema_operativo;
    @Column(name = "visible")
    private int visible;
    @Column(name = "usuario_administrador")
    private int usuario_administrador;
    @Column(name = "desactivar_usuario_actual")
    private int desactivar_usuario_actual;
    // relaciones hacia configuraciones dependientes
    @OneToOne(mappedBy = "configuracion", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference(value = "configuracion-ip")
    private ConfiguracionIP ipConfig;

    @OneToOne(mappedBy = "configuracion", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference(value = "configuracion-impresora")
    private ConfiguracionImpresora impresora;

    // IMPORTANTE: Métodos Helper para sincronizar la relación
    public void setIpConfig(ConfiguracionIP ipConfig) {
        this.ipConfig = ipConfig;
        if (ipConfig != null) {
            ipConfig.setConfiguracion(this); // AQUÍ SE ASIGNA EL ID MÁGICAMENTE
        }
    }

    public void setImpresora(ConfiguracionImpresora impresora) {
        this.impresora = impresora;
        if (impresora != null) {
            impresora.setConfiguracion(this); // AQUÍ SE ASIGNA EL ID MÁGICAMENTE
        }
    }
}
