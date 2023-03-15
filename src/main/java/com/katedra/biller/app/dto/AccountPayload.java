package com.katedra.biller.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountPayload {

    private Long cuit;
    private Integer puntoVenta;
    private Integer tipoFactura;
    private Integer concepto;
    private String razonSocial;
    private String inicioActividad;
    private String condicionDeVenta;
    private String condicionFrenteAlIva;
    private String certName;
    private String certSigner;
    private String certPassword;
    private Boolean activo;

}
