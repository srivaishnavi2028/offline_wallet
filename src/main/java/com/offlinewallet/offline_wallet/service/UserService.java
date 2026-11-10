package com.offlinewallet.offline_wallet.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.offlinewallet.offline_wallet.entity.User;
import com.offlinewallet.offline_wallet.entity.Wallet;
import com.offlinewallet.offline_wallet.repository.UserRepository;
import com.offlinewallet.offline_wallet.repository.WalletRepository;
import com.offlinewallet.offline_wallet.security.JwtService;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
	private final WalletRepository walletRepository;
	private final JwtService jwtService;

	public UserService(UserRepository userRepository, WalletRepository walletRepository, JwtService jwtService) {
		super();
		this.userRepository = userRepository;
		this.walletRepository = walletRepository;
		this.jwtService = jwtService;
	}
	
	public User registerUser(User user) {
		String encodedPassword=passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		User savedUser=userRepository.save(user);
		Wallet wallet=new Wallet();
		wallet.setUser(savedUser);
		wallet.setBalance(0);
		walletRepository.save(wallet);
		savedUser.setWallet(wallet);
		return savedUser;
	}
	public String loginUser(String email,String password) {
		Optional<User>user=userRepository.findByEmail(email);
		if(user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
			return jwtService.generateToken(email);
		}
		return null;
	}
}
