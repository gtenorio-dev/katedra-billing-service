package com.katedra.biller.app.model;

// "Ticket de Acceso" (TA)
public class TicketAccess {

    private String token;
    private String sign;
    private String expirationTime;

    public TicketAccess() {
        this.token = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhaG9tbywgTz1BRklQLCBDPUFSLCBTRVJJQUxOVU1CRVI9Q1VJVCAzMzY5MzQ1MDIzOSIgZHN0PSJDTj13c2ZlLCBPPUFGSVAsIEM9QVIiIHVuaXF1ZV9pZD0iMTQ2NTg4Mjc5MiIgZ2VuX3RpbWU9IjE2NzY2MjE2MDEiIGV4cF90aW1lPSIxNjc2NjY0ODYxIi8+CiAgICA8b3BlcmF0aW9uIHR5cGU9ImxvZ2luIiB2YWx1ZT0iZ3JhbnRlZCI+CiAgICAgICAgPGxvZ2luIGVudGl0eT0iMzM2OTM0NTAyMzkiIHNlcnZpY2U9IndzZmUiIHVpZD0iU0VSSUFMTlVNQkVSPUNVSVQgMjA5NDAwNTIzMDEsIENOPWZhY3R1cmFlbGVjdHJvbmljYXRlc3QzIiBhdXRobWV0aG9kPSJjbXMiIHJlZ21ldGhvZD0iMjIiPgogICAgICAgICAgICA8cmVsYXRpb25zPgogICAgICAgICAgICAgICAgPHJlbGF0aW9uIGtleT0iMjA5NDAwNTIzMDEiIHJlbHR5cGU9IjQiLz4KICAgICAgICAgICAgPC9yZWxhdGlvbnM+CiAgICAgICAgPC9sb2dpbj4KICAgIDwvb3BlcmF0aW9uPgo8L3Nzbz4K";
        this.sign = "UfRfWdZ0084M0NJOjxkjbl/xfOnv9OWEUgEc8heGvoPe6tpLSSae3u8JejRVe890j1LTQ2fiJrTldWeipl7BeX16vzpG0jO0F6QsjN25T1gAlXUDma+5O4juUZo26IGdYVceo/vrgi0MMxGMUNd4ngL/SAFl1aOwYmrBc2juLM8=";

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
