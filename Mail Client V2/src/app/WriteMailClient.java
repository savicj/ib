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

//	private static final String KEY_FILE = "./data/session.key";
//	private static final String IV1_FILE = "./data/iv1.bin";
//	private static final String IV2_FILE = "./data/iv2.bin";
//	private static final String SUBJ = "Enrypted mail";
	private static final String FILE = "./data/user_enc.xml";
	
	
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
            
    		
    		File file = new File(FILE);
            
            //slanje
    		MimeMessage mimeMessage = MailHelper.createMimeMessage(reciever, "", "", file);
        	MailWritter.sendMessage(service, "me", mimeMessage);
    		//git greska

        	
        
        }catch (Exception e) {
        	e.printStackTrace();
		}
	}
}
