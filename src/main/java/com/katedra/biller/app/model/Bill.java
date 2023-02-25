package com.katedra.biller.app.model;

// TODO create table 'bills'
public class Bill {

    private Long id;
    private String fechaProceso; // yyyymmddHHmmss
    private Long numComprobante;
    private String fechaComprobante; // yyyymmdd
    private String resultado; // Aprobado = A ; Parcialmente Aprobado = P ; Rechazado = R
    private Long dni; // Del comprador
    private String cae;
    private String caeFechaVto;
    private String mensaje;

}
