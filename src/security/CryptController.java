package security;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
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
		
		//Encypt it using asymmetrical key and return it
		return cipher.doFinal(symmetric_key);
	}
	
	public byte[] initSymmetricKey() throws NoSuchAlgorithmException{
		KeyGenerator keyGen = KeyGenerator.getInstance(SYMETRIC_ALGORITHM);
		keyGen.init(128);
		secKey = keyGen.generateKey();
		return Base64.getEncoder().encode(secKey.getEncoded());
	}
	
	public void autoDecryptSymmetricalKey(byte[] encrypted_key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		//Prepare the asymmetrical cipher
		final Cipher cipher = Cipher.getInstance(ASYMETRIC_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
		
		//Decrypt the key
		byte[] decrypted_key = cipher.doFinal(encrypted_key);
		setSymmetricKey(decrypted_key);
	}
	
	public void setSymmetricKey(byte[] key) throws NoSuchAlgorithmException{
		byte[] decodedKey = Base64.getDecoder().decode(key);
		secKey = new SecretKeySpec(decodedKey, SYMETRIC_ALGORITHM);
	}
	
	public byte[] generateKey() throws NoSuchAlgorithmException{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ASYMETRIC_ALGORITHM);
		keyGen.initialize(1024);
		key = keyGen.generateKeyPair();
		byte[] encoded_key = key.getPublic().getEncoded();
		return encoded_key;
	}
	
	public byte[] decrypt(byte[] encrypted) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, ShortBufferException{
		//byte[] padding = new byte[encrypted.length + 16%encrypted.length];
		//System.arraycopy(encrypted, 0, padding, 0, encrypted.length);
		//int decrypt_length = encrypted.length + 16%encrypted.length;
		//System.out.println("L:"+encrypted.length);
		
		final Cipher cipher = Cipher.getInstance(SYMETRIC_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secKey);
		
		byte[] decrypted_text = new byte[encrypted.length];
		cipher.doFinal(encrypted, 2, encrypted.length-2, decrypted_text, 2);
		
		System.arraycopy(encrypted, 0, decrypted_text, 0, 2);

		return decrypted_text;
	}
	
	public byte[] encrypt(byte[] plaintext,int length) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, ShortBufferException{
		int  crypt_length = length-2;
		crypt_length += crypt_length % 16;
		
		byte[] buffer = new byte[crypt_length];
		for(int i = 0; i < crypt_length ;i++){
			buffer[i] = 0;
		}
		System.arraycopy(plaintext, 2, buffer, 0, length);

		
		final Cipher cipher = Cipher.getInstance(SYMETRIC_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secKey);
		
		byte[] temp = cipher.doFinal(buffer);
		
		byte[] encrypted_text = new byte[temp.length+2];
		
		encrypted_text[0] = (byte) (encrypted_text.length & 0x00FF);
		encrypted_text[1] = (byte)((encrypted_text.length & 0xFF00) >> 8);
		System.arraycopy(temp, 0, encrypted_text, 2, temp.length);
		
		return encrypted_text;
	}
	
	public boolean cryptReady(){
		return ready;
	}
	
	public void setReady(boolean bool){
		ready = bool;
	}
}
