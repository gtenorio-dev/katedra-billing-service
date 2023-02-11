package com.katedra.biller.app.model;

// "Ticket de Acceso" (TA)
public class TicketAccess {

    private String token;
    private String sign;
    private String expirationTime;

    public TicketAccess() {
        this.token = null;
        this.sign = null;
        this.expirationTime = null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

}
