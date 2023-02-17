package com.katedra.biller.app.controller;

import com.katedra.biller.app.client.gen.FECAESolicitarResponse;
import com.katedra.biller.app.client.gen.FECompUltimoAutorizadoResponse;
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

	/*
	{
		"cuit" : 20940052301,
		"details" : [
			{
				"dniComprador" : 19003022,
				"fchServDesde" : "20230217",
				"fchServHasta" : "20220217",
				"fchVtoPago" : "20230217",
				"importe" : 1000
			},
			{
				"dniComprador" : 19003022,
				"fchServDesde" : "20230217",
				"fchServHasta" : "20230217",
				"fchVtoPago" : "20230217",
				"importe" : 2000
			}
		]
	 }
	 */

	@PostMapping("/create")
	public ResponseEntity<FECAESolicitarResponse> create(@RequestBody BillingPayload billingPayload) throws Exception {
		return new ResponseEntity<>(billerService.create(billingPayload), HttpStatus.OK);
	}

	@GetMapping("/last")
	public ResponseEntity<FECompUltimoAutorizadoResponse> last(@RequestParam Long cuit) throws Exception {
		return new ResponseEntity<>(billerService.getUltimoComprobanteAutorizado(cuit), HttpStatus.OK);
	}
	
}
