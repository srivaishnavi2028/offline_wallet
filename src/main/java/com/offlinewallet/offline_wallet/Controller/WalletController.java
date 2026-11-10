package com.offlinewallet.offline_wallet.Controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.offlinewallet.offline_wallet.entity.Transaction;
import com.offlinewallet.offline_wallet.entity.Wallet;
import com.offlinewallet.offline_wallet.exception.WalletException;
import com.offlinewallet.offline_wallet.response.PaymentResponse;
import com.offlinewallet.offline_wallet.service.WalletService;

@RestController
public class WalletController {
	private final WalletService walletService;

	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}
	@GetMapping ("/wallet/my-wallet")
	public Wallet getMyWallet(Authentication authentication) {
		String email=authentication.getName();
		return walletService.getWalletByEmail(email);
	}
	@PostMapping("/wallet/my-wallet/add")
	public PaymentResponse addMoney(Authentication authentication,@RequestParam Double amount) {
		String email=authentication.getName();
		return walletService.addMoneyByEmail(email, amount);
	}
	@PostMapping("/wallet/my-wallet/pay")
	public PaymentResponse payMoney(Authentication authentication,@RequestParam Double amount,@RequestParam(required=false) String receiver) {
		String email=authentication.getName();
		return walletService.payMoneyByEmail(email, amount,receiver);
	}
	
	@GetMapping("/wallet/my-transactions")
	public List<Transaction> getMyTransactions(Authentication authentication) {
	    String email = authentication.getName();
	    return walletService.getTransactions(email);
	}
	@GetMapping("/test-error")
	public String testError() {
		throw new WalletException("this is test");
	}

}
