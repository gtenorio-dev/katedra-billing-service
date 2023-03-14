package com.katedra.biller.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
/*
 * https://www.afip.gob.ar/fe/qr/especificaciones.asp
 *
 * Version 1
 */
public class QRData {

    private int ver;
    private String fecha;
    private Long cuit;
    private int ptoVta;
    private int tipoCmp;
    private Long nroCmp;
    private double importe;
    private String moneda;
    private double ctz;
    private int tipoDocRec;
    private Long nroDocRec;
    private String tipoCodAut;
    private Long codAut;

}
