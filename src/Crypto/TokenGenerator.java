package Crypto;

import java.security.SecureRandom;

public class TokenGenerator {
	public static String generate() {
	    SecureRandom random = new SecureRandom();
	    byte bytes[] = new byte[20];
	    random.nextBytes(bytes);
	    
		String stringToken = "";
		for(byte b: bytes) { 
			 stringToken += b;
			} 
		
		System.out.println("Generated token: " + stringToken);
	    return stringToken;
	}
}
