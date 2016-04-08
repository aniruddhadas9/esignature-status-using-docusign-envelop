package com.etouch.eoriginal;

import java.io.StringReader;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.InputSource;

public class EoriginalHelper {
	
	public static final String username = "your esignauter username here";
	public static final String password = "your esignature password here";
	public static final String companyName = "your e signature company name here (which you provided in the eSignature)";
	
	public static final String tmpWorkingTrnsURL = "https://<company specific url>.com/ecore/?action=eoSearchTransactions&transactionXRef2=<sid>";
	public static final String tmpEnvId = "<envelop id>";
	public static final String tmpSId = "<sid>";
	public static final String searchTransactionBySid = "?action=eoSearchTransactions&transactionXRef2=<sid>";
	
	public static final String baseUrl = "https://<company specific url>.eoriginal.com/ecore/";
	public static final String getTransactionSIDByEnvId = "?action=eoSearchTransactions&transactionXRef1=";
	public static final String getTransactionPropertyBySID = "?action=eoGetTransactionProperties&transactionSid=";
	public static final String deleteTransactionBySID = "?action=eoDeleteTransaction&transactionSid=";
	
	public static final String createtransaction = "?action=eoCreateTransaction&configureSmartSign=true&"
			+ "transactionBusinessProcessName=&transactionDescription="
			+ "&transactionName=BlogTransaction&transactionStateName"
			+ "=&transactionTaskTemplateName=&transactionTypeName="
			+ "&transactionXRef1=&transactionXRef2=<sid>&transactionXRef3=";

	
	public static String parseXMLBody(String body, String searchToken) {
		String xPathExpression;
		try {
			xPathExpression = String.format("//*[1]/*[local-name()='%s']", searchToken);
			XPath xPath = XPathFactory.newInstance().newXPath();
			return (xPath.evaluate(xPathExpression, new InputSource(new StringReader(body))));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
