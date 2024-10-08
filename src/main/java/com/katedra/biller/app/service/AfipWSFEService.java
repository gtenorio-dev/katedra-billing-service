package com.katedra.biller.app.service;

import com.katedra.biller.app.client.AfipWSFEClient;
import com.katedra.biller.app.client.gen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AfipWSFEService {

    @Autowired
    private AfipWSFEClient wsfeClient;

    public FEParamGetTiposCbteResponse getTiposComprobantes(FEAuthRequest authRequest) {
		FEParamGetTiposCbte request = new FEParamGetTiposCbte();
		request.setAuth(authRequest);
        return wsfeClient.feParamGetTiposCbte(request);
    }

    public FECompUltimoAutorizadoResponse getUltimoComprobanteAutorizado(FEAuthRequest authRequest, int ptoVenta, int cbteTipo) {
		FECompUltimoAutorizado request = new FECompUltimoAutorizado();
		request.setAuth(authRequest);
		request.setPtoVta(ptoVenta);
		request.setCbteTipo(cbteTipo);
		return wsfeClient.feCompUltimoAutorizado(request);
    }

    public FECAESolicitarResponse generateBill(FEAuthRequest authRequest, FECAERequest fecaeRequest) {
		FECAESolicitar request = new FECAESolicitar();
		request.setAuth(authRequest);
		request.setFeCAEReq(fecaeRequest);
        return wsfeClient.feCAESolicitar(request);
    }

    public FEParamGetPtosVentaResponse getPuntosVenta(FEAuthRequest authRequest) {
        FEParamGetPtosVenta request = new FEParamGetPtosVenta();
        request.setAuth(authRequest);
        return wsfeClient.feParamGetPtosVenta(request);
    }

    public FECompConsultarResponse getBill(FEAuthRequest authRequest, FECompConsultaReq feCompConsultaReq){
        FECompConsultar request = new FECompConsultar();
        request.setAuth(authRequest);
        request.setFeCompConsReq(feCompConsultaReq);
        return wsfeClient.feCompConsultar(request);
    }

}
