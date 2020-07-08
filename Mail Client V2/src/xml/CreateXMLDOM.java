package xml;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CreateXMLDOM {

	
	public static void createXML(String subj, String body){
		final String xmlFilePath = "C:\\Users\\Jana\\git\\ib\\Mail Client V2\\data\\user.xml";

		/*
		 * DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Date
		 * date = new Date();
		 */

		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
		 
	    DocumentBuilder icBuilder;
	     
	    try {
	    	icBuilder = icFactory.newDocumentBuilder();
	    	Document doc = icBuilder.newDocument();
	    	
	    	Element root = doc.createElement("email");
	    	doc.appendChild(root);
	         
	    	Element subject = doc.createElement("subject");
	    	root.appendChild(subject);
	    	subject.setTextContent(subj);
	    	
	    	Element b = doc.createElement("body");
	    	root.appendChild(b);
	    	b.setTextContent(body);
			
			 
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
		    DOMSource source = new DOMSource(doc);
			 
			     
		    StreamResult streamResult = new StreamResult(new File(xmlFilePath));
		    transformer.transform(source, streamResult);
			     
		    // StreamResult result = new StreamResult(System.out);
			 
			System.out.println("File saved!");
			System.out.println("\nXML DOM Created Successfully..");
	

	    } catch (Exception e) {
	         e.printStackTrace();
	    }
 }
	
	
	
	
	
}
