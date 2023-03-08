package com.katedra.biller.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillingPayload {

	private Long cuit;
	private List<BillDetailDTO> details;

}
