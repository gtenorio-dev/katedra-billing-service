package com.katedra.biller.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// "Ticket de Acceso" (TA)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TicketAccess {

    private String token;
    private String sign;
    private String expirationTime;

}
