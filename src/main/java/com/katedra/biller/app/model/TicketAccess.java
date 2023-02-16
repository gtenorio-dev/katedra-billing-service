package com.katedra.biller.app.model;

// "Ticket de Acceso" (TA)
public class TicketAccess {

    private String token;
    private String sign;
    private String expirationTime;

    public TicketAccess() {
        this.token = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhaG9tbywgTz1BRklQLCBDPUFSLCBTRVJJQUxOVU1CRVI9Q1VJVCAzMzY5MzQ1MDIzOSIgZHN0PSJDTj13c2ZlLCBPPUFGSVAsIEM9QVIiIHVuaXF1ZV9pZD0iODg3MzA3ODk3IiBnZW5fdGltZT0iMTY3NjUzODU5MSIgZXhwX3RpbWU9IjE2NzY1ODE4NTEiLz4KICAgIDxvcGVyYXRpb24gdHlwZT0ibG9naW4iIHZhbHVlPSJncmFudGVkIj4KICAgICAgICA8bG9naW4gZW50aXR5PSIzMzY5MzQ1MDIzOSIgc2VydmljZT0id3NmZSIgdWlkPSJTRVJJQUxOVU1CRVI9Q1VJVCAyMDk0MDA1MjMwMSwgQ049ZmFjdHVyYWVsZWN0cm9uaWNhdGVzdDMiIGF1dGhtZXRob2Q9ImNtcyIgcmVnbWV0aG9kPSIyMiI+CiAgICAgICAgICAgIDxyZWxhdGlvbnM+CiAgICAgICAgICAgICAgICA8cmVsYXRpb24ga2V5PSIyMDk0MDA1MjMwMSIgcmVsdHlwZT0iNCIvPgogICAgICAgICAgICA8L3JlbGF0aW9ucz4KICAgICAgICA8L2xvZ2luPgogICAgPC9vcGVyYXRpb24+Cjwvc3NvPgo=";
        this.sign = "OxTbeghgeRu5o7Skyctm/ohlvm3lqFMQvupFVLTOlKfrrEyJ+uygZG7lPn+cNOoOfheT4CgkgYVCNafc331/up0CCGVh3QkqWUevoKSpduO0FesruTYhmRaSImOtJTNXkyaIa/jicJH4DjPKHQFS7UTIi4y2EN94EJwgOcKfxdM=";

//        this.token = null;
//        this.sign = null;
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
