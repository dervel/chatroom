package security;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptController {

	//Symmetric Encryption
	private String SYMETRIC_ALGORITHM = "AES";
	private SecretKey secKey;
	
	//Asymmetric Encryption
	private String ASYMETRIC_ALGORITHM = "RSA";
	private KeyPair key;
	
	private boolean ready;
	
	public CryptController() throws NoSuchAlgorithmException{
		ready = false;
	}
	
	public byte[] autoEncryptSymmetricKey(byte[] akey) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException{
		//Prepare the public key
		KeyFactory kf = KeyFactory.getInstance(ASYMETRIC_ALGORITHM);
		PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(akey));
		
		//Prepare the asymmetrical cipher
		final Cipher cipher = Cipher.getInstance(ASYMETRIC_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		
		//Make a new symmetric key
		byte[] symmetric_key = initSymmetricKey();
		ready = true;
		
		//Encypt it using asymmetrical key and return it
		return cipher.doFinal(symmetric_key);
	}
	
	public byte[] initSymmetricKey() throws NoSuchAlgorithmException{
		KeyGenerator keyGen = KeyGenerator.getInstance(SYMETRIC_ALGORITHM);
		keyGen.init(128);
		secKey = keyGen.generateKey();
		return secKey.getEncoded();
	}
	
	public void autoDecryptSymmetricalKey(byte[] encrypted_key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		//Prepare the asymmetrical cipher
		final Cipher cipher = Cipher.getInstance(ASYMETRIC_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
		
		//Decrypt the key
		byte[] decrypted_key = cipher.doFinal(encrypted_key);
		setSymmetricKey(decrypted_key);
		
		ready = true;
	}
	
	public void setSymmetricKey(byte[] key) throws NoSuchAlgorithmException{
		secKey = new SecretKeySpec(key, 0, key.length, SYMETRIC_ALGORITHM); 
	}
	
	public byte[] generateKey() throws NoSuchAlgorithmException{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(SYMETRIC_ALGORITHM);
		keyGen.initialize(1024);
		key = keyGen.generateKeyPair();
		return key.getPublic().getEncoded();
	}
	
	public byte[] decrypt(byte[] encrypted) throws Exception{
		byte[] decrypted_text = null;
		final Cipher cipher = Cipher.getInstance(SYMETRIC_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secKey);
		decrypted_text = cipher.doFinal(encrypted);
		return decrypted_text;
	}
	
	public byte[] encrypt(byte[] plaintext) throws Exception{
		byte[] encrypted_text = null;
		final Cipher cipher = Cipher.getInstance(SYMETRIC_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secKey);
		encrypted_text = cipher.doFinal(plaintext);
		return encrypted_text;
	}
	
	public boolean cryptReady(){
		return ready;
	}
	
	/*
	public void setRemotePublicKey(byte[] key){
		try {
			KeyFactory kf = KeyFactory.getInstance(SYMETRIC_ALGORITHM);
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
	*/
}
