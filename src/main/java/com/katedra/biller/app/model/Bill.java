package com.katedra.biller.app.model;

// TODO create table 'bills'
public class Bill {

    private Long id;
    private Long cuit;
    private Integer puntoVenta;
    private Long numeroComprobante;
    private String fechaProceso; // yyyymmddHHmmss
    private Integer cantRegistros;
    private String resultado; // Aprobado = A ; Parcialmente Aprobado = P ; Rechazado = R
    private String reproceso;
    private Long dni;
    private String comprobanteFecha; // yyyymmdd
    private String cae;
    private String caeFechaVto;


}
