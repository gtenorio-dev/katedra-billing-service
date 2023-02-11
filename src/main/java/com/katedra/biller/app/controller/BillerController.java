package com.katedra.biller.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.katedra.biller.app.dto.BillingPayload;
import com.katedra.biller.app.service.BillerService;

@RestController
@RequestMapping("/billing")
public class BillerController {
	
	@Autowired
	private BillerService billerService;

	@PostMapping("/create")
	public String create(@RequestBody BillingPayload billingPayload) throws Exception {
		return billerService.create(billingPayload);
	}
	
}
