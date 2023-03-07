package com.katedra.biller.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillDTO {

    private Long cuit;
    private Long numComprobante;
    private Long dni;
    private String nombreYApellido;
    private String formaDePago;
    private List<ProductDTO> productos;

}
