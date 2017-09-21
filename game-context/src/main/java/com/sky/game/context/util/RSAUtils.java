package com.sky.game.context.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;

import javax.crypto.Cipher;

import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;


public class RSAUtils {

	static {
		Security
				.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}

	private static RSAUtils rsa = null;

	private static String PUBLIC_KEY_FILE;

	private static String PRIVATE_KEY_FILE;

	private String password;

	public static RSAUtils rsa() {
		if (rsa == null)
			rsa = new RSAUtils();
		return rsa;
	}
	
	
	public static void load(){
		
	}
	
	PublicKey publicKey;
	PrivateKey privateKey;
	private RSAUtils() {
		super();
		// TODO Auto-generated constructor stub
		try {
			
		
			WebsocketConfiguration config = WebsocketConfiguration.getInstance();
			PUBLIC_KEY_FILE = config.getPublicKeyFile();
			PRIVATE_KEY_FILE = config.getPrivateKeyFile();
			password = config.getPassword();
			
			publicKey = getCA(PUBLIC_KEY_FILE);
			privateKey= loadPrivateKey(PRIVATE_KEY_FILE);
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private PublicKey getCA(String path) throws CertificateException,
			NoSuchProviderException, IOException {

		PEMReader pr = openPEMResource(path, null);
		Object o=pr.readObject();

		PublicKey ca=(PublicKey)o;
		pr.close();
		return ca;
	}

	public RSAPrivateKey loadPrivateKey(String path) throws Exception {
		try {
			
			PEMReader pr = openPEMResource(path, new Password(password.toCharArray()));
			Object o = pr.readObject();
			pr.close();

			KeyPair kp = (KeyPair) o;
			PrivateKey privateKey = kp.getPrivate();

			return (RSAPrivateKey) privateKey;
		} catch (NullPointerException e) {
			throw new Exception("˽");
		}
	}

	/**
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public byte[] encrypt(byte[] content) {
		try {
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			
			byte[] output = cipher.doFinal(content);
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * 
	 * @param content
	 * 
	 * @param key
	 * 
	 * @return
	 */
	public byte[] decrypt(byte[] content,int offset,int len) {
		try {
			
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			InputStream ins = new ByteArrayInputStream(content,offset,len);
			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			byte[] buf = new byte[128];
			int bufl;
			while ((bufl = ins.read(buf)) != -1) {
				byte[] block = null;
				if (buf.length == bufl) {
					block = buf;
				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}
				writer.write(cipher.doFinal(block));
			}
			return writer.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private PEMReader openPEMResource(String fileName, PasswordFinder pGet)
			throws FileNotFoundException {
		if (fileName == null)
			throw new FileNotFoundException("PEM Resource can't be empty!");
		PEMReader reader = null;
		if (fileName.startsWith("http://")) {

			InputStream res = null;

			try {
				URL url = new URL(fileName);
				res = url.openStream(); // this.getClass().getResourceAsStream(fileName);
				Reader fRd = new BufferedReader(new InputStreamReader(res));
				reader = new PEMReader(fRd, pGet);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			InputStream res = new FileInputStream(RSAUtils.class.getResource(fileName)
					.getFile());// this.getClass().getResourceAsStream(fileName);
			Reader fRd = new BufferedReader(new InputStreamReader(res));
			try {
				reader = new PEMReader(fRd, pGet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reader;

	}

	private static class Password implements PasswordFinder {
		char[] password;

		Password(char[] word) {
			this.password = word;
		}

		public char[] getPassword() {
			return password;
		}
	}

	
	private static final String CHARSET_ENCODING="utf-8";
	public   String bytes2String(byte[] buffer,int offset,int len){
		String s=null;
		try {
			s=new String(buffer,offset,len,CHARSET_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	public static ByteBuffer getBytes(byte type,byte encrypt,String content){
		ByteBuffer byteBuffer=null;
		try {
			byte[] bytes=content.getBytes(CHARSET_ENCODING);
			if(bytes!=null){
				byteBuffer=ByteBuffer.allocate(bytes.length+2);
				byteBuffer.put(type);
				byteBuffer.put(encrypt);
				byteBuffer.put(bytes);
				byteBuffer.flip();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byteBuffer;
	}
	
	
	public static byte[] getBytes(String content){
		byte[] bytes=null;
		try {
			bytes=content.getBytes(CHARSET_ENCODING);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}
	
	public static boolean isEmpty(Object obj){
		return obj==null;
	}
	
	
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String mi = "123456789";
//		StringBuffer sb=new StringBuffer();
//		for(int i=0;i<200;i++){
//			
//			sb.append(String.valueOf(i));
//		}
//		
//		mi=sb.toString();
		byte[] encrypt = RSAUtils.rsa().encrypt(mi.getBytes("UTF-8"));
		byte[] decrypt = RSAUtils.rsa().decrypt(encrypt,0,encrypt.length);
		String ming = new String(decrypt, "UTF-8");
		System.out.println(ming);
	}
}