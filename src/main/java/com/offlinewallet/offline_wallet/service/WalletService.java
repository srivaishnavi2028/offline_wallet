
package com.offlinewallet.offline_wallet.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.offlinewallet.offline_wallet.entity.Transaction;
import com.offlinewallet.offline_wallet.entity.User;
import com.offlinewallet.offline_wallet.entity.Wallet;
import com.offlinewallet.offline_wallet.exception.WalletException;
import com.offlinewallet.offline_wallet.repository.TransactionRepository;
import com.offlinewallet.offline_wallet.repository.UserRepository;
import com.offlinewallet.offline_wallet.repository.WalletRepository;
import com.offlinewallet.offline_wallet.response.PaymentResponse;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    public WalletService(WalletRepository walletRepository,TransactionRepository transactionRepository,UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }
    public Wallet getWallet(Long id) {
        return walletRepository.findById(id).orElse(null);
    }
    public Wallet getWalletByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        return user.getWallet();
    }
    public Wallet createWallet(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        Wallet wallet = new Wallet();
        wallet.setBalance(0);
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }
    public PaymentResponse addMoney(Long id, Double amount) {
        Wallet wallet = walletRepository.findById(id).orElse(null);
        if (wallet == null) {
            return new PaymentResponse("wallet not found", null);
        }
        if (amount == null || amount <= 0) {
            return new PaymentResponse("Amount must be greater than 0", wallet.getBalance());
        }
        if (wallet.getBalance() + amount > 10000) {
            return new PaymentResponse("Wallet balance cannot exceed 10000", wallet.getBalance());
        }
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
        Transaction transaction =new Transaction(amount, "ADD_MONEY", "Self");
        transaction.setWallet(wallet);
        transactionRepository.save(transaction);
        return new PaymentResponse("Money added successfully", wallet.getBalance());
    }
    public PaymentResponse payMoney(Long id, Double amount, String receiver) {
        Wallet wallet = walletRepository.findById(id).orElse(null);
        if (wallet == null) {
            return new PaymentResponse("wallet not found", null);
        }
        if (amount == null || amount <= 0) {
            return new PaymentResponse("Amount must be greater than 0", wallet.getBalance());
        }
        if (receiver == null || receiver.trim().isEmpty()) {
            return new PaymentResponse("Receiver is required", wallet.getBalance());
        }
        if (wallet.getBalance() < amount) {
            return new PaymentResponse("Insufficient balance", wallet.getBalance());
        }
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);
        Transaction transaction = new Transaction(amount, "PAYMENT", receiver);
        transaction.setWallet(wallet);
        transactionRepository.save(transaction);
        return new PaymentResponse("Payment successful", wallet.getBalance());
    }
    public PaymentResponse addMoneyByEmail(String email, Double amount) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || user.getWallet() == null) {
            return new PaymentResponse("wallet not found", null);
        }
        return addMoney(user.getWallet().getId(), amount);
    }
    @Transactional
    public PaymentResponse payMoneyByEmail(String email,Double amount,String receiver,String clientTransactionId) {
        User sender = userRepository.findByEmail(email).orElse(null);
        if (sender == null || sender.getWallet() == null) {
            return new PaymentResponse("Sender wallet not found", null);
        }
        Wallet senderWallet = sender.getWallet();
        if (clientTransactionId == null|| clientTransactionId.trim().isEmpty()) {
            throw new WalletException("Transaction ID is required");
        }
        clientTransactionId = clientTransactionId.trim();
        // Prevent duplicate payment processing
        if (transactionRepository.existsByClientTransactionId(
                clientTransactionId)) {
            return new PaymentResponse("Payment already processed",senderWallet.getBalance());
        }
        if (amount == null || amount <= 0) {
            throw new WalletException("Amount must be greater than 0");
        }
        if (receiver == null || receiver.trim().isEmpty()) {
            throw new WalletException("Receiver email is required");
        }
        receiver = receiver.trim();
        if (sender.getEmail().equalsIgnoreCase(receiver)) {
            throw new WalletException("You cannot pay yourself");
        }
        User receiverUser =userRepository.findByEmail(receiver).orElse(null);
        if (receiverUser == null) {
            return new PaymentResponse("Receiver does not exist", senderWallet.getBalance());
        }
        if (receiverUser.getWallet() == null) {
            throw new WalletException("Receiver wallet not found");
        }
        Wallet receiverWallet = receiverUser.getWallet();
        if (senderWallet.getBalance() < amount) {
            return new PaymentResponse("Insufficient balance", senderWallet.getBalance());
        }
        senderWallet.setBalance(senderWallet.getBalance() - amount);
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);
        Transaction senderTransaction =new Transaction(amount, "PAYMENT", receiverUser.getEmail());
        senderTransaction.setWallet(senderWallet);
        senderTransaction.setClientTransactionId(clientTransactionId);
        transactionRepository.save(senderTransaction);
        Transaction receiverTransaction =new Transaction(amount, "CREDIT", sender.getEmail());
        receiverTransaction.setWallet(receiverWallet);
        transactionRepository.save(receiverTransaction);
        return new PaymentResponse("Payment successful", senderWallet.getBalance());
    }
    public List<Transaction> getTransactions(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || user.getWallet() == null) {
            return List.of();
        }
        return transactionRepository.findByWalletIdOrderByIdDesc(user.getWallet().getId());
    }
}
