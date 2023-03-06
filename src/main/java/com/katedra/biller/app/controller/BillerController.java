package com.katedra.biller.app.controller;

import com.katedra.biller.app.client.gen.FECAESolicitarResponse;
import com.katedra.biller.app.client.gen.FECompUltimoAutorizadoResponse;
import com.katedra.biller.app.client.gen.FEParamGetPtosVentaResponse;
import com.katedra.biller.app.dto.BillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.katedra.biller.app.dto.BillingPayload;
import com.katedra.biller.app.service.BillerService;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/billing")
public class BillerController {
	
	@Autowired
	private BillerService billerService;

	@PostMapping("/create")
	public ResponseEntity<FECAESolicitarResponse> create(@RequestBody BillingPayload billingPayload) throws Exception {
		return new ResponseEntity<>(billerService.create(billingPayload), HttpStatus.OK);
	}

	@GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> pdf(@RequestBody BillDTO billDTO) throws Exception {
		ByteArrayInputStream file = billerService.buildFile(billDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=".concat(billerService.getFileName(billDTO)));
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(file));
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
