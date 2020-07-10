package app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.security.utils.JavaUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.xml.sax.SAXException;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import crypto.AsymmetricKeyDecryption;
import model.mailclient.MailBody;
import signature.VerifySignatureEnveloped;
import support.MailHelper;
import support.MailReader;
import util.Base64;
import util.GzipUtil;
import xml.CreateXMLDOM;

public class ReadMailClient extends MailClient {

	public static long PAGE_SIZE = 3;
	public static boolean ONLY_FIRST_PAGE = true;
	
	private static final String KEY_FILE = "./data/session.key";
	private static final String EMAIL = "./data/user_dec.xml";

//	private static final String IV1_FILE = "./data/iv1.bin";
//	private static final String IV2_FILE = "./data/iv2.bin";
	
	
	static {
		// staticka inicijalizacija
		Security.addProvider(new BouncyCastleProvider());
		org.apache.xml.security.Init.init();
	}
	
	
	public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
				InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, MessagingException, NoSuchPaddingException,
				InvalidAlgorithmParameterException, UnrecoverableKeyException, KeyStoreException, CertificateException, SAXException, ParserConfigurationException {
        // Build a new authorized API client service.
        Gmail service = getGmailService();
        ArrayList<MimeMessage> mimeMessages = new ArrayList<MimeMessage>();
        
        String user = "me";
        String query = "is:unread label:INBOX";
        
        List<Message> messages = MailReader.listMessagesMatchingQuery(service, user, query, PAGE_SIZE, ONLY_FIRST_PAGE);
        for(int i=0; i<messages.size(); i++) {
        	Message fullM = MailReader.getMessage(service, user, messages.get(i).getId());
        	
        	MimeMessage mimeMessage;
			try {
				
				mimeMessage = MailReader.getMimeMessage(service, user, fullM.getId());
				
				System.out.println("\n Message number " + i);
				System.out.println("From: " + mimeMessage.getHeader("From", null));
				System.out.println("Subject: " + mimeMessage.getSubject());
				System.out.println("Body: " + MailHelper.getText(mimeMessage));
				System.out.println("\n");
				
				mimeMessages.add(mimeMessage);
	        
			} catch (MessagingException e) {
				e.printStackTrace();
			}	
        }
        
        System.out.println("Select a message to decrypt:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	        
	    String answerStr = reader.readLine();
	    Integer answer = Integer.parseInt(answerStr);
	    
	    //preuzimanje fajla i kreiranje user_enc.xml
		MimeMessage chosenMessage = mimeMessages.get(answer);
		getAttachement(chosenMessage);
		
		//dekriptovanje fajla
		AsymmetricKeyDecryption decrypt = new AsymmetricKeyDecryption();			
		decrypt.testIt();
		
		//provera potpisa
		VerifySignatureEnveloped verify = new VerifySignatureEnveloped();
		verify.testIt();
		
		//ispis sadrzaja poruke1
		System.out.print("\nEMAIL");
		CreateXMLDOM.writeEmailContent(EMAIL);
		
		
		//SA KONTROLNE TACKE, VISE MI NIJE POTREBNO


//      //TODO: Decrypt a message and decompress it. The private key is stored in a file.
//		Cipher aesCipherDec = Cipher.getInstance("AES/CBC/PKCS5Padding");
//		//SecretKey secretKey = new SecretKeySpec(JavaUtils.getBytesFromFile(KEY_FILE), "AES");
//		
//		//izvlacenje enkriptovane poruke
//		MailBody mb = new MailBody(MailHelper.getText(chosenMessage));
//		IvParameterSpec ivParameterSpec1 = new IvParameterSpec(mb.getIV1Bytes());
//		IvParameterSpec ivParameterSpec2 = new IvParameterSpec(mb.getIV2Bytes());
//		byte[] encSecretkey = mb.getEncKeyBytes();
//		String encBody = mb.getEncMessage();
//		
//		
//		//Keystore
//		KeyStore ks = KeyStore.getInstance("JKS");
//		ks.load(new FileInputStream("./data/userbKS.jks"), "userbp".toCharArray());
//		PrivateKey ubpk = (PrivateKey) ks.getKey("userb", "userbp".toCharArray());
//		
//		Cipher rsaCipherDec = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//		rsaCipherDec.init(Cipher.DECRYPT_MODE, ubpk);
//		byte[] decryptedKey = rsaCipherDec.doFinal(encSecretkey);
//		
//		SecretKey secretKey = new SecretKeySpec(decryptedKey, "AES");
//		System.out.println("Dekriptovan kljuc: " + secretKey.hashCode());
//		
//		//inicijalizacija za dekriptovanje
//		aesCipherDec.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec1);
//				
//		
//		//dekripcija i dekompresija body-a
//		String receivedBodyTxt = new String(aesCipherDec.doFinal(Base64.decode(encBody)));
//		String decompressedBodyText = GzipUtil.decompress(Base64.decode(receivedBodyTxt));
//		System.out.println("Body text: " + decompressedBodyText);
//		
//		
//		//inicijalizacija za dekriptovanje
//		aesCipherDec.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec2);
//		
//		//dekompresovanje i dekriptovanje subject-a
//		String decryptedSubjectTxt = new String(aesCipherDec.doFinal(Base64.decode(chosenMessage.getSubject())));
//		String decompressedSubjectTxt = GzipUtil.decompress(Base64.decode(decryptedSubjectTxt));
//		System.out.println("Subject text: " + new String(decompressedSubjectTxt));
	}
	
	
	
	public static void getAttachement(MimeMessage message) throws MessagingException, IOException {
		String contentType = message.getContentType();
		 
		if (contentType.contains("multipart")) {
		    // this message may contain attachment
			Multipart multiPart = (Multipart) message.getContent();
			for (int i = 0; i < multiPart.getCount(); i++) {
			    MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
			    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
			        // this part is attachment
			    	// save an attachment from a MimeBodyPart to a file
			    	String destFilePath = "./data/";
			    	part.saveFile(destFilePath+part.getFileName());
			    	System.out.print("Sacuvan fajl.\n");
			    } else {
			    	System.out.print("if nije prosao.");
			    }
			}

		}
	}
}
