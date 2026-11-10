package com.offlinewallet.offline_wallet.Controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.offlinewallet.offline_wallet.entity.User;
import com.offlinewallet.offline_wallet.security.JwtService;
import com.offlinewallet.offline_wallet.service.UserService;


@RestController
public class UserController {
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	
	public UserController(UserService userService, AuthenticationManager authenticationManager,
			JwtService jwtService) {
		super();
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	
	
	@PostMapping("/users/register")
	public User register(@RequestBody User user) {
		return userService.registerUser(user);
	}

	@PostMapping("/users/login")
	public String login(@RequestBody User user) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
		String token=jwtService.generateToken(user.getEmail());
		return token;
	}
}
