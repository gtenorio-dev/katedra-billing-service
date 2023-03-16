package com.katedra.biller.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long cuit;
    @Column(name = "punto_venta", nullable = false)
    private Integer puntoVenta;
    @Column(name = "tipo_factura", nullable = false)
    private Integer tipoFactura;
    @Column(name = "concepto", nullable = false)
    private Integer concepto;
    @Column(name = "razon_social", nullable = false)
    private String razonSocial;
    @Column(name = "inicio_actividad", nullable = false)
    private String inicioActividad;
    @Column(name = "condicion_de_venta", nullable = false)
    private String condicionDeVenta;
    @Column(name = "condicion_frente_al_iva", nullable = false)
    private String condicionFrenteAlIva;
    @Column(nullable = false)
    private Boolean activo;
    @Column(nullable = false)
    private Double limite;
    @Column(name = "limite_anterior")
    private Double limiteAnterior;

}
