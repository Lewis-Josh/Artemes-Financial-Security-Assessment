package com.snhu.sslserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SslServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SslServerApplication.class, args);
	}

}

@RestController
class ServerController{
	
	// Generate secure random keys
	private static final SecureRandom random = new SecureRandom();
	
	
	@GetMapping("/testHash")
	public ResponseEntity<Map<String, String>> testHash() throws Exception {
		String testData = "Joshua Lewis";
		return myHash(testData);
	}
	
	@GetMapping("/hash") //** Changed from RequestMapping to reduce vulnerability Surface Area**//
	public ResponseEntity<Map<String, String>> myHash(@RequestParam(value = "data", required = true) String data) {
		Map<String, String> response = new HashMap<>(); // New Hash map for data
		if(data == null || data.isEmpty()) {// Check Data to be hashed
			response.put("error", "Data cannot be null or empty");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}		
		
		String hashValue = ""; // Post Encryption hash
		String preEncryptionHashValue = ""; // Pre-Encryption hash
		String algorithm = "SHA-256"; // Hash Algorithm used
		String secretKey = ""; // Encryption key, included only for demonstration
		
		try { //Try to get data, convert bytes to hex, generate secret key, and encrypt hash...
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(data.getBytes("UTF-8")); // MessageDigest .digest bytes encoding for data string
			preEncryptionHashValue = bytesToHex(hash); // Call bytesToHex Method
			secretKey = generateSecretKey(); // Call generateSecretKey Method
			hashValue = encrypt(preEncryptionHashValue, secretKey); // Call encrypt method and assign value to hashValue
			}
		catch (Exception e) { //Catch anything bad and terminate...
			throw new RuntimeException("Error while hashing data: " + e.getMessage(), e);
		}
		
		// Return valid HashMap to the Web Browser
		response.put("Data", data);
		response.put("Name of Cipher Algorithm Used", algorithm);
		response.put("Encryption Algorithm Used", "AES-GCM 256");
		response.put("Pre-Encryption CheckSum Value", preEncryptionHashValue);
		response.put("Encrypted CheckSum Value", hashValue);
		response.put("Encryption Key", secretKey); //Only returned for demonstration
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	private static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if(hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		
		return hexString.toString();
	}
	
	private static String encrypt(String value, String secretKey) throws Exception {
	    byte[] iv = new byte[12]; // IV for AES-GCM
	    random.nextBytes(iv);
	    GCMParameterSpec gcmspec = new GCMParameterSpec(128, iv); // Create IV buffer from Byte Array
	    byte[] decodedKey = Base64.getDecoder().decode(secretKey); // Decode the Base64 secret key
	    SecretKeySpec skeySpec = new SecretKeySpec(decodedKey, "AES"); // SecretKey encoding
	    Cipher cipherInstance = Cipher.getInstance("AES/GCM/NoPadding"); // Initialize Cipher object
	    cipherInstance.init(Cipher.ENCRYPT_MODE, skeySpec, gcmspec); // Transformation from buffer and key
	    byte[] encrypted = cipherInstance.doFinal(value.getBytes()); // Encrypt value based on transformation of bytes in Byte Array

	    return Base64.getEncoder().encodeToString(encrypted); // Return encrypted value
	}
	
	// Method to generate a SecretKey for AES-Encryption
	private static String generateSecretKey() {
		byte[] key = new byte[32]; // Key for AES-256
		random.nextBytes(key);
		
		return Base64.getEncoder().encodeToString(key);
	}
}