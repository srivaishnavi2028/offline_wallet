package com.offlinewallet.offline_wallet.entity;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Double amount;
	private String type;
	@Column(unique = true)
	private String clientTransactionId;
	private String  receiver;
	public Transaction(Double amount, String type, String receiver) {
		super();
		this.amount = amount;
		this.type = type;
		this.receiver = receiver;
		this.createdAt = LocalDateTime.now();
	}
	public String getClientTransactionId() {
	    return clientTransactionId;
	}

	public void setClientTransactionId(String clientTransactionId) {
	    this.clientTransactionId = clientTransactionId;
	}
	@ManyToOne
	@JsonIgnore
	private Wallet wallet;
	@Column(columnDefinition="DATETIME")
	private LocalDateTime createdAt;
	
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Wallet getWallet() {
		return wallet;
	}
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	public Transaction() {
		
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public Long getId() {
		return id;
	}
//	public void setId(Long id) {
//		this.id = id;
//	}
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
	

}
