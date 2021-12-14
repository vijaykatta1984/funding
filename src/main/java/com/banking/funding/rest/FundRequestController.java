package com.banking.funding.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.funding.service.FundingService;

@RestController()
@RequestMapping("account")
public class FundRequestController {
	private final FundingService fundingService;

	public FundRequestController(FundingService fundingService) {
		this.fundingService = fundingService;
	}

	@PostMapping("/transfer-funds")
	public void transferFunds(@RequestBody FundTransferRequest fundTransferRequest) {
		fundingService.depositAmount(fundTransferRequest);
	}
}