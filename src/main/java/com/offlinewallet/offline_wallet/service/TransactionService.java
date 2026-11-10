package com.offlinewallet.offline_wallet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.offlinewallet.offline_wallet.entity.Transaction;
import com.offlinewallet.offline_wallet.repository.TransactionRepository;
import com.offlinewallet.offline_wallet.response.TransactionResponse;

@Service
public class TransactionService {
	public final TransactionRepository transactionRepository;

	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	public List<TransactionResponse>getAllTransactions(){
		List<Transaction>transactions=transactionRepository.findAll();
		return transactions.stream().map(transaction->new TransactionResponse(
				transaction.getId(),
				transaction.getAmount(),
				transaction.getType(),
				transaction.getReceiver(),
				transaction.getCreatedAt())).toList();
	}

}
