package com.katedra.biller.app.controller;

import com.katedra.biller.app.client.gen.FECompConsultarResponse;
import com.katedra.biller.app.client.gen.FECompUltimoAutorizadoResponse;
import com.katedra.biller.app.client.gen.FEParamGetPtosVentaResponse;
import com.katedra.biller.app.dto.BanlanceDTO;
import com.katedra.biller.app.dto.BillDTO;
import com.katedra.biller.app.dto.BillProcess;
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

	private final BillerService billerService;

	public BillerController (BillerService billerService) {
		this.billerService = billerService;
	}


	@PostMapping("/create")
	public ResponseEntity<BillProcess> create(@RequestBody BillingPayload billingPayload) throws Exception {
		return new ResponseEntity<>(billerService.create(billingPayload), HttpStatus.OK);
	}

	@GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> pdf(@RequestBody BillDTO billDTO) throws Exception {
		ByteArrayInputStream file = billerService.buildFile(billDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=".concat(billerService.getPDFFileName(billDTO)));
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

	@GetMapping("/balance") /// /balance?cuit=20940052301&since=20230101&to=20230401
	public ResponseEntity<BanlanceDTO> balance(
			@RequestParam Long cuit, @RequestParam String since, @RequestParam String to) throws Exception {
		// TODO modificar Bill Entity para poder hacer una busqueda por fechas
		return new ResponseEntity<>(billerService.getTotalBills(cuit,since, to), HttpStatus.OK);
	}

	@GetMapping("/info")
	public ResponseEntity<FECompConsultarResponse> billInfo(
			@RequestParam Long cuit, @RequestParam Long numComprobante) throws Exception {
		return new ResponseEntity<>(billerService.getBillInfo(cuit,numComprobante), HttpStatus.OK);
	}

}
