package com.offlinewallet.offline_wallet.response;

import java.time.LocalDateTime;

public class TransactionResponse {
	private Long id;
	private Double amount;
	private String type;
	private String receiver;
	private LocalDateTime createdAt;
	public TransactionResponse(Long id, Double amount, String type, String receiver, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.amount = amount;
		this.type = type;
		this.receiver = receiver;
		this.createdAt = createdAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
}
