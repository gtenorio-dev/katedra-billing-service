package com.katedra.biller.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillProcessDetail {

    private Long ventaId;
    private Long numComprobante;
    private String cae;
    private String error;

}
