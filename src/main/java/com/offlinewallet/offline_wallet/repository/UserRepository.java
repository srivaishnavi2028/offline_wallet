package com.offlinewallet.offline_wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.offlinewallet.offline_wallet.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
	Optional<User>findByEmail(String email);

}
