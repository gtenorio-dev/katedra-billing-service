package com.katedra.biller.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDTO {

    private String nombre;
    private int cantidad;
    private double precioUnitario;
    private double total;

}
