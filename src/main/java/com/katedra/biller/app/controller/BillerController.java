package com.katedra.biller.app.controller;

import com.katedra.biller.app.client.gen.FECAESolicitarResponse;
import com.katedra.biller.app.client.gen.FECompUltimoAutorizadoResponse;
import com.katedra.biller.app.client.gen.FEParamGetPtosVentaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.katedra.biller.app.dto.BillingPayload;
import com.katedra.biller.app.service.BillerService;

@RestController
@RequestMapping("/billing")
public class BillerController {
	
	@Autowired
	private BillerService billerService;

	@PostMapping("/create")
	public ResponseEntity<FECAESolicitarResponse> create(@RequestBody BillingPayload billingPayload) throws Exception {
		return new ResponseEntity<>(billerService.create(billingPayload), HttpStatus.OK);
	}

	@GetMapping("/last")
	public ResponseEntity<FECompUltimoAutorizadoResponse> last(@RequestParam Long cuit) throws Exception {
		return new ResponseEntity<>(billerService.getUltimoComprobanteAutorizado(cuit), HttpStatus.OK);
	}

	@GetMapping("/selling-point")
	public ResponseEntity<FEParamGetPtosVentaResponse> ptosVenta(@RequestParam Long cuit) throws Exception {
		return new ResponseEntity<>(billerService.getPuntosVenta(cuit), HttpStatus.OK);
	}

}
