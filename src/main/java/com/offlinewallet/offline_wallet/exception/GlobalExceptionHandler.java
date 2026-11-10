package com.offlinewallet.offline_wallet.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(WalletException.class)
	public ResponseEntity<Map<String,String>> handleWalletException(WalletException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("message",ex.getMessage()));
	}

}
