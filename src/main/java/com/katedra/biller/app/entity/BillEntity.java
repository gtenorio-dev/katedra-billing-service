package com.katedra.biller.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "proceso_fecha")
    private Date fechaProceso; // yyyymmddHHmmss

    @Column(name = "comprobante_num")
    private Long numComprobante;

    @Column(name = "comprobante_fecha")
    @Temporal(TemporalType.DATE)
    private Date fechaComprobante; // yyyymmdd

    // TODO private Date fechaServicioDesde

    private Long dni; // Del comprador

    private String cae;

    @Column(name = "cae_fecha_vto")
    @Temporal(TemporalType.DATE)
    private Date caeFechaVto; // yyyymmdd

    private double importe;

    @Column(name = "venta_id")
    private Long ventaId;

    @ManyToOne
    private AccountEntity account;

}
