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

    @Column(name = "fecha_proceso")
    private String fechaProceso; // yyyymmddHHmmss

    @Column(name = "num_comprobante")
    private Long numComprobante;

    @Column(name = "fecha_comprobante")
    private String fechaComprobante; // yyyymmdd

    private String resultado; // Aprobado = A ; Parcialmente Aprobado = P ; Rechazado = R

    private Long dni; // Del comprador

    private String cae;

    @Column(name = "cae_fecha_vto")
    private String caeFechaVto;

    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private BillEntity bill;

}
