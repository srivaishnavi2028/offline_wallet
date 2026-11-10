package com.offlinewallet.offline_wallet.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.offlinewallet.offline_wallet.response.TransactionResponse;
import com.offlinewallet.offline_wallet.service.TransactionService;

@RestController
public class TransactionController {
	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	@GetMapping("/transactions")
	public List<TransactionResponse> getAllTransactions(){
		return transactionService.getAllTransactions();
	}

}
