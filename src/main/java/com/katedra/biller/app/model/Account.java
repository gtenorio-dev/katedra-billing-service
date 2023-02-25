package com.katedra.biller.app.model;

import java.util.List;

//@Entity()
public class Account {

    private Long id;
    private Long cuit;
    private Integer puntoVenta;


    private List<Bill> bills;

}
