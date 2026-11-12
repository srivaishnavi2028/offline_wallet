
package com.offlinewallet.offline_wallet.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.offlinewallet.offline_wallet.entity.Transaction;
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletIdOrderByIdDesc(Long walletId);
    boolean existsByClientTransactionId(String clientTransactionId);
}
