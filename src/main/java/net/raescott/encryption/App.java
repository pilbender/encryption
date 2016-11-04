package net.raescott.encryption;

import java.io.*;
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
		System.out.println("*** SHA-1 Hash on a file ***");
		String content1 = fileReader("sample-file1.txt");
		System.out.println("sample-file1.txt: " + content1);
		System.out.println("hashed sample-file1.txt: " + DigestUtils.shaHex(content1));
		System.out.println("hashed sample-file1.txt: " + DigestUtils.shaHex(new FileInputStream("sample-file1.txt")));
		String content2 = fileReader("sample-file2.txt");
		System.out.println("sample-file2.txt: " + content2);
		System.out.println("hashed sample-file2.txt: " + DigestUtils.shaHex(content2));
		System.out.println("hashed sample-file2.txt: " + DigestUtils.shaHex(new FileInputStream("sample-file2.txt")));

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

		// Encrypt with public key
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher);
		System.out.println("string: " + s);
		cipherOutputStream.write(s.getBytes());
		cipherOutputStream.close();
		String encryptedString = byteArrayOutputStream.toString();
		System.out.println("encrypt with public key: " + encryptedString);

		// Decrypt with private key
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byteArrayOutputStream.reset();
		cipherOutputStream.write(encryptedString.getBytes());
		cipherOutputStream.close();
		String decryptedString = byteArrayOutputStream.toString();
		System.out.println("decrypt with private key: " + decryptedString);

		System.out.println("");
		
		// Sign with private key
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		byteArrayOutputStream.reset();
		cipherOutputStream.write(s.getBytes());
		cipherOutputStream.close();
		String signedString = byteArrayOutputStream.toString();
		System.out.println("sign with private key: " + signedString);

		// Verify digital signature
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byteArrayOutputStream.reset();
		cipherOutputStream.write(signedString.getBytes());
		cipherOutputStream.close();
		String verifiedString = byteArrayOutputStream.toString();
		System.out.println("verify digital signature: " + verifiedString);
	}

	public static String fileReader(String fileName) throws IOException {
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			stringBuilder = new StringBuilder();
			while (bufferedReader.ready()) {
				stringBuilder.append(bufferedReader.readLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
		}
		return stringBuilder.toString();
	}
}
