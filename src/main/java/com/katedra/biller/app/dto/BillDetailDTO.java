package com.katedra.biller.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillDetailDTO {

    private Long ventaId;
    private Long dniComprador;
    private String fchServDesde;
    private String fchServHasta;
    private String fchVtoPago;
    private double importe;

}
