package com.offlinewallet.offline_wallet.response;

public class PaymentResponse {
	private String message;
	private Double balance;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public PaymentResponse(String message, Double balance) {
		this.message = message;
		this.balance = balance;
	}
	

}
