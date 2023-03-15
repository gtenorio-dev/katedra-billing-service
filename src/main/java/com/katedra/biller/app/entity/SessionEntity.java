package com.katedra.biller.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "sessions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", length = 2000)
    private String token;
    @Column(name = "sign")
    private String sign;
    @Column(name = "expiration_time")
    private String expirationTime;

    @Column(name = "cert_name", nullable = false)
    private String certName;
    @Column(name = "cert_signer", nullable = false)
    private String certSigner;
    @Column(name = "cert_password", nullable = false)
    private String certPassword;

    @OneToOne
    private AccountEntity account;

}
