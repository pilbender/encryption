package net.raescott.encryption;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 */
public class App {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException {
		// SHA-1 hash
		System.out.println("*** SHA-1 Hash ***");
		String s = "scott";
		System.out.println("s: " + s);
		System.out.println("hashed s: " + DigestUtils.shaHex(s));

		System.out.println("");

		// RSA-2048 encryption
		System.out.println("*** RSA-2048 ***");
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		KeyPair kp = kpg.genKeyPair();
		Key publicKey = kp.getPublic();
		Key privateKey = kp.getPrivate();
		System.out.println("public key: " + publicKey.toString());
		System.out.println("private key: " + privateKey.toString());

		System.out.println("");
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		CipherOutputStream cipherOutputStream = new CipherOutputStream(System.out, cipher);
		System.out.println("string: " + s);
		System.out.println("encrypt with public key: ");
		cipherOutputStream.write(s.getBytes());
		cipherOutputStream.close();

	}
}
