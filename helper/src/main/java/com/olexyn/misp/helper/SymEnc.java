package com.olexyn.misp.helper;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SymEnc {
	public static void main(String[] args) throws Exception {
		// Step 1: Generate a symmetric AES key (alternatively, you can specify your own)
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128); // Key size
		SecretKey secretKey = keyGenerator.generateKey();

		// Step 2: Create and initialize the Cipher for AES encryption
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		// Step 3: Encrypt the string
		String inputString = "Your String Here";
		byte[] encryptedBytes = cipher.doFinal(inputString.getBytes());

		// Convert encrypted bytes to Base64 to get a string representation
		String encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);

		System.out.println("Encrypted String: " + encryptedString);
	}
}
