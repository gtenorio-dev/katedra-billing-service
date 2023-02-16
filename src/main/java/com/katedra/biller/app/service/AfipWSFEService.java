package com.katedra.biller.app.service;

import com.katedra.biller.app.client.AfipWSFEClient;
import com.katedra.biller.app.client.gen.*;
import com.katedra.biller.app.dto.BillingPayload;
import com.katedra.biller.app.model.TicketAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AfipWSFEService {

	@Autowired
	private AfipWSFEClient wsfeClient;

	public FEParamGetTiposCbteResponse getTiposComprobantes(FEAuthRequest authRequest) {
		return wsfeClient.feParamGetTiposCbte(authRequest);
	}

	public FECompUltimoAutorizadoResponse getUltimoComprobanteAutorizado(FEAuthRequest authRequest, int ptoVenta, int cbteTipo) {
		return wsfeClient.feCompUltimoAutorizado(authRequest, ptoVenta, cbteTipo);
	}
	
	public FECAESolicitarResponse generateBill(FEAuthRequest authRequest) {

		// Generate Header
		FECAECabRequest fecaeCabRequest = new FECAECabRequest();
		fecaeCabRequest.setPtoVta(2); // Punto de venta 2
		fecaeCabRequest.setCbteTipo(11); // Factura C - Monotributo
		fecaeCabRequest.setCantReg(1); // Cantidad de productos a facturar

		// Generate Bill Details
		FECAEDetRequest feDetRequest = new FECAEDetRequest();
		feDetRequest.setConcepto(2); // Productos y servicios = 3
		feDetRequest.setDocNro(96); // Tipo DNI = 96
		feDetRequest.setDocNro(19003022); // Numero de DNI
		feDetRequest.setCbteDesde(1);
		feDetRequest.setCbteHasta(1);
		feDetRequest.setCbteFch("20230216"); // Formato yyyymmdd
		double total = 5000.5;
		feDetRequest.setImpTotal(total);
		feDetRequest.setImpTotConc(0); // Para factura C = 0
		feDetRequest.setImpNeto(total); // Para factura C = impTotal
		feDetRequest.setImpOpEx(0); // Para factura C = 0
		feDetRequest.setImpIVA(0); // Para factura C = 0
		feDetRequest.setImpTrib(0); // No obligatorio para factura C
		feDetRequest.setFchServDesde("20230216");
		feDetRequest.setFchServHasta("20230216");
		feDetRequest.setFchVtoPago("20230216");
		feDetRequest.setMonId("PES"); // Pesos Argentinos = PES
		feDetRequest.setMonCotiz(1); // Para ARS = 1

		// NO OBLIGATORIO
//
//		// Comprobantes Asociados
//		CbteAsoc cbteAsoc = new CbteAsoc();
//		cbteAsoc.setPtoVta(2);
//		// TODO cbteAsoc. ...
//
//		ArrayOfCbteAsoc arrayOfCbteAsoc = new ArrayOfCbteAsoc();
//		List<CbteAsoc> cbteAsocs = arrayOfCbteAsoc.getCbteAsoc();
//		cbteAsocs.add(cbteAsoc);
//
//		feDetRequest.setCbtesAsoc(arrayOfCbteAsoc);
//
//
//		ArrayOfTributo arrayOfTributo = new ArrayOfTributo();
//		List<Tributo> tributoList = arrayOfTributo.getTributo();
////		feDetRequest.setTributos(); // TODO
//
//
//		// IVA
//		AlicIva iva = new AlicIva();
//		//TODO
//		ArrayOfAlicIva arrayOfAlicIva = new ArrayOfAlicIva();
//		List<AlicIva> alicIvas = arrayOfAlicIva.getAlicIva();
//		alicIvas.add(iva);
//		feDetRequest.setIva(arrayOfAlicIva);


		// Building Request
		FECAERequest fecaeRequest = new FECAERequest();
		fecaeRequest.setFeCabReq(fecaeCabRequest);

		ArrayOfFECAEDetRequest arrayOfFECAEDetRequest = new ArrayOfFECAEDetRequest();
		List<FECAEDetRequest> fecaeDetRequests = arrayOfFECAEDetRequest.getFECAEDetRequest();
		fecaeDetRequests.add(feDetRequest);
		fecaeRequest.setFeDetReq(arrayOfFECAEDetRequest);


		// Send Bill
		return wsfeClient.feCAESolicitar(authRequest, fecaeRequest);
	}

}
