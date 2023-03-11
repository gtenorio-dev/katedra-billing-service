package com.katedra.biller.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BanlanceDTO {

    private Long cuit;
    private Long facturas;
    private double facturado;

    // Todo
    // private double notaCredito;

}
