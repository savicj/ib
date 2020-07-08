package app;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.utils.JavaUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.api.services.gmail.Gmail;

import crypto.AsymmetricKeyEncryption;
import model.mailclient.MailBody;
import signature.SignEnveloped;
import util.Base64;
import util.GzipUtil;
import util.IVHelper;
import xml.CreateXMLDOM;
import support.MailHelper;
import support.MailWritter;

public class WriteMailClient extends MailClient {

	private static final String KEY_FILE = "./data/session.key";
	private static final String IV1_FILE = "./data/iv1.bin";
	private static final String IV2_FILE = "./data/iv2.bin";
	
	
	static {
		// staticka inicijalizacija
		Security.addProvider(new BouncyCastleProvider());
		org.apache.xml.security.Init.init();
	}
	
	
	public static void main(String[] args) {
		
        try {
        	Gmail service = getGmailService();
            
        	System.out.println("Insert a reciever:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String reciever = reader.readLine();
        	
            System.out.println("Insert a subject:");
            String subject = reader.readLine();
            
            
            System.out.println("Insert body:");
            String body = reader.readLine();
            
            //kreiran xml fajl koji sadrzi subj i body
            CreateXMLDOM.createXML(subject, body);
            
            //potpisivanje fajla
            SignEnveloped sign = new SignEnveloped();
    		sign.testIt();
            
            //enkriptovanje fajla
            AsymmetricKeyEncryption encrypt = new AsymmetricKeyEncryption();
    		encrypt.testIt();
            
            
            //slanje
//    		MailBody mb = new MailBody(ciphertextStr, ivParameterSpec1.getIV(), ivParameterSpec2.getIV(), cipherkeyStr);
//			String mailBody = mb.toCSV();
//			System.out.println("Telo email-a: " + mailBody);
//			
//			
//			//snimaju se bajtovi kljuca i IV. 
//			JavaUtils.writeBytesToFilename(KEY_FILE, secretKey.getEncoded()); 
//			JavaUtils.writeBytesToFilename(IV1_FILE, ivParameterSpec1.getIV()); 
//			JavaUtils.writeBytesToFilename(IV2_FILE, ivParameterSpec2.getIV());
//			
//        	MimeMessage mimeMessage = MailHelper.createMimeMessage(reciever, ciphersubjectStr, mailBody);
//        	MailWritter.sendMessage(service, "me", mimeMessage);
//        	
        	
            
            /*
            //Compression
            String compressedSubject = Base64.encodeToString(GzipUtil.compress(subject));
            String compressedBody = Base64.encodeToString(GzipUtil.compress(body));
            
            //Key generation
            KeyGenerator keyGen = KeyGenerator.getInstance("AES"); 
			SecretKey secretKey = keyGen.generateKey();
			Cipher aesCipherEnc = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
			//inicijalizacija za sifrovanje 
			IvParameterSpec ivParameterSpec1 = IVHelper.createIV();
			aesCipherEnc.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec1);
			
			
			//sifrovanje
			byte[] ciphertext = aesCipherEnc.doFinal(compressedBody.getBytes());
			String ciphertextStr = Base64.encodeToString(ciphertext);
			System.out.println("Kriptovan tekst: " + ciphertextStr);
			
			
			//inicijalizacija za sifrovanje 
			IvParameterSpec ivParameterSpec2 = IVHelper.createIV();
			aesCipherEnc.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec2);
			
			byte[] ciphersubject = aesCipherEnc.doFinal(compressedSubject.getBytes());
			String ciphersubjectStr = Base64.encodeToString(ciphersubject);
			System.out.println("Kriptovan subject: " + ciphersubjectStr);
			
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream("./data/useraKS.jks"), "userap".toCharArray());
			Certificate ubc = ks.getCertificate("userb");
			PublicKey ubpk = ubc.getPublicKey();
			
			//inicijalizacija za sifrovanje
			Cipher rsaCipherEnc = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			rsaCipherEnc.init(Cipher.ENCRYPT_MODE, ubpk);
			
			//sifrovanje
			byte[] cipherkey = rsaCipherEnc.doFinal(secretKey.getEncoded());
			String cipherkeyStr = Base64.encodeToString(cipherkey);
			System.out.println("Kljuc: " + secretKey.hashCode());
			System.out.println("Kriptovan kljuc: " + cipherkeyStr);
			
			
			MailBody mb = new MailBody(ciphertextStr, ivParameterSpec1.getIV(), ivParameterSpec2.getIV(), cipherkeyStr);
			String mailBody = mb.toCSV();
			System.out.println("Telo email-a: " + mailBody);
			
			
			//snimaju se bajtovi kljuca i IV. 
			JavaUtils.writeBytesToFilename(KEY_FILE, secretKey.getEncoded()); 
			JavaUtils.writeBytesToFilename(IV1_FILE, ivParameterSpec1.getIV()); 
			JavaUtils.writeBytesToFilename(IV2_FILE, ivParameterSpec2.getIV());
			
        	MimeMessage mimeMessage = MailHelper.createMimeMessage(reciever, ciphersubjectStr, mailBody);
        	MailWritter.sendMessage(service, "me", mimeMessage);
        	*/
        }catch (Exception e) {
        	e.printStackTrace();
		}
	}
}
