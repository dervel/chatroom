package security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class CryptController {

	private String ALGORITHM = "RSA";
	private KeyPair my_key;
	private Key remote_public_key = null;
	
	public CryptController() throws NoSuchAlgorithmException{
		generateKey();
	}
	
	private void generateKey() throws NoSuchAlgorithmException{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		keyGen.initialize(1024);
		my_key = keyGen.generateKeyPair();
	}
	
	public byte[] decrypt(byte[] encrypted) throws Exception{
		byte[] decrypted_text = null;
		final Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, my_key.getPrivate());
		decrypted_text = cipher.doFinal(encrypted);
		return decrypted_text;
	}
	
	public byte[] encrypt(byte[] plaintext) throws Exception{
		if(!canEncrypt())
			throw new Exception("Public key is null");
		
		byte[] encrypted_text = null;
		final Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, remote_public_key);
		encrypted_text = cipher.doFinal(plaintext);
		return encrypted_text;
	}
	
	public void setRemotePublicKey(byte[] key){
		try {
			KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
			PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(key));
			remote_public_key = publicKey;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] getPublicKeyAsArray(){
		Key public_key = my_key.getPublic();
		byte[] temp = new byte[public_key.getEncoded().length];
		System.arraycopy(public_key.getEncoded(), 0, temp, 0, public_key.getEncoded().length);
		return temp;
	}
	
	public boolean canEncrypt(){
		return (remote_public_key == null ? false : true);
	}
}
