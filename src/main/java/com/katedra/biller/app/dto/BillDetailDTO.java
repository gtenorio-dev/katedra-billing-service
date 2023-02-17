package com.katedra.biller.app.dto;

public class BillDetailDTO {

    private Long dniComprador;
    private String fchServDesde;
    private String fchServHasta;
    private String fchVtoPago;
    private double importe;

    public Long getDniComprador() {
        return dniComprador;
    }

    public void setDniComprador(Long dniComprador) {
        this.dniComprador = dniComprador;
    }

    public String getFchServDesde() {
        return fchServDesde;
    }

    public void setFchServDesde(String fchServDesde) {
        this.fchServDesde = fchServDesde;
    }

    public String getFchServHasta() {
        return fchServHasta;
    }

    public void setFchServHasta(String fchServHasta) {
        this.fchServHasta = fchServHasta;
    }

    public String getFchVtoPago() {
        return fchVtoPago;
    }

    public void setFchVtoPago(String fchVtoPago) {
        this.fchVtoPago = fchVtoPago;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
}
