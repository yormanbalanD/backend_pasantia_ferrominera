package com.fmowinconf.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

@Table(name = "configuracion_ip")
public class ConfiguracionIP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_configuraciones")
    @ToString.Exclude
    @JsonManagedReference(value = "configuracion-ip")
    private Configuracion configuracion;

    @Column(name = "ip_address")
    private String ip_address;
    @Column(name = "subnet_mask")
    private String subnet_mask;
    @Column(name = "default_gateway")
    private String default_gateway;
    @Column(name = "created_at")
    private String created_at;
}
