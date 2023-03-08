package com.katedra.biller.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;


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
    private String fechaProceso; // yyyymmddHHmmss

    @Column(name = "comprobante_num")
    private Long numComprobante;

    @Column(name = "comprobante_fecha")
    private String fechaComprobante; // yyyymmdd

    private Long dni; // Del comprador

    private String cae;

    @Column(name = "cae_fecha_vto")
    private String caeFechaVto;

    private double importe;

    @ManyToOne
    private AccountEntity account;

}
