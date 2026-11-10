package com.offlinewallet.offline_wallet.repository;


import org.springframework.data.jpa.repository.JpaRepository;


import com.offlinewallet.offline_wallet.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet,Long>{
	

}
