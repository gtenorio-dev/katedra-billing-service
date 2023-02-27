package com.katedra.biller.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long cuit;
    @Column(name = "punto_venta", nullable = false)
    private Integer puntoVenta;

    @Column(name = "sesion_clave", length = 2000)
    private String token;
    @Column(name = "sesion_firma")
    private String sign;
    @Column(name = "sesion_expiracion")
    private String expirationTime;

}
