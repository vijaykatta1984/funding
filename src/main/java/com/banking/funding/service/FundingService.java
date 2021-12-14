package com.banking.funding.service;

import com.banking.funding.rest.FundTransferRequest;

public interface FundingService {

	void depositAmount(FundTransferRequest transfer);

}
