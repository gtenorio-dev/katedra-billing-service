package com.katedra.biller.app.dto;

import java.util.List;

public class BillingPayload {

	private Long cuit;

	private List<BillDetailDTO> details;

	public Long getCuit() {
		return cuit;
	}

	public void setCuit(Long cuit) {
		this.cuit = cuit;
	}

	public List<BillDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<BillDetailDTO> details) {
		this.details = details;
	}
}
