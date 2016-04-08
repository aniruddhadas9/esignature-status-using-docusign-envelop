package com.etouch.eoriginal;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.etouch.service.HtppResponseHelper;

public class TransactionImpl {
	
	private HttpClient httpclient = new DefaultHttpClient();
	
	private HttpGet httpGet = null;
	private HttpResponse httpResponse = null;
	private InputStream inputStream = null;
	
	public boolean loginEOriginal() {
		boolean returnValue = false;
		String url = EoriginalHelper.baseUrl+"?action=eoLogin&loginUsername="+EoriginalHelper.username+"&loginOrganization="+EoriginalHelper.companyName+"&loginPassword="+EoriginalHelper.password;
		try {
			httpGet = new HttpGet(url);
			httpResponse = httpclient.execute(httpGet);
			inputStream = httpResponse.getEntity().getContent();
			String response = HtppResponseHelper.getResponseBody(inputStream);
			String status = EoriginalHelper.parseXMLBody(response, "status");
			String userTransaction = EoriginalHelper.parseXMLBody(response, "userTransaction");
			
			
			if(httpResponse.getStatusLine().getStatusCode()==200) {
				returnValue = true;
			}
			
			if(status.equals("ok")) {
				returnValue = true;
			}
			
			System.out.println("userTransaction: " + userTransaction);
			System.out.println("xml: " + response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	public String getSIDByEnvId(String envId) {
		String returnValue = "";
		try {
			httpGet = new HttpGet(EoriginalHelper.baseUrl+EoriginalHelper.getTransactionSIDByEnvId+envId);
			httpResponse = httpclient.execute(httpGet);
			inputStream = httpResponse.getEntity().getContent();
			String response = HtppResponseHelper.getResponseBody(inputStream);
			System.out.println("getPropertiesByEnvSID|xml: " + response);
			returnValue = EoriginalHelper.parseXMLBody(response, "sid");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	public String getPropertiesByEnvSID(String SID) {
		String returnValue = "";
		try {
			httpGet = new HttpGet(EoriginalHelper.baseUrl+EoriginalHelper.getTransactionPropertyBySID+SID);
			//httpGet.addHeader("loginUsername", EoriginalHelper.username);
			//httpGet.addHeader("loginOrganization", EoriginalHelper.username);
			//httpGet.addHeader("loginPassword", EoriginalHelper.username);
			httpResponse = httpclient.execute(httpGet);
			
			/*for(int i=0; i < httpGet.getAllHeaders().length; i++){
				System.out.println(httpGet.getAllHeaders()[i].getName()+"-->"+httpGet.getAllHeaders()[i].getValue());
			}*/
			
			inputStream = httpResponse.getEntity().getContent();
			String response = HtppResponseHelper.getResponseBody(inputStream);
			System.out.println("getPropertiesByEnvSID|xml: " + response);
			returnValue = response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	public boolean deleteTransactionBySID(String SID) {
		boolean returnValue = false;
		try {
			httpGet = new HttpGet(EoriginalHelper.baseUrl+EoriginalHelper.deleteTransactionBySID+SID);
			httpResponse = httpclient.execute(httpGet);
			inputStream = httpResponse.getEntity().getContent();
			String response = HtppResponseHelper.getResponseBody(inputStream);
			System.out.println("deleteTransactionBySID|xml: " + response);
			if(httpResponse.getStatusLine().getStatusCode()==200) {
				returnValue = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}
	
	public boolean deleteTransactionByEnvId(String envId) {
		boolean returnValue = false;
		//TransactionImpl transactionImpl = new TransactionImpl();
		if(loginEOriginal()) {
			String SID = getSIDByEnvId(envId);
			String properties = getPropertiesByEnvSID(SID);
			System.out.println("deleteTransactionByEnvId|properties: "+properties);
			System.out.println("deleteTransactionByEnvId|state: "+EoriginalHelper.parseXMLBody(properties, "state"));
			System.out.println("deleteTransactionByEnvId|xRef1: "+EoriginalHelper.parseXMLBody(properties, "xRef1"));
			System.out.println("deleteTransactionByEnvId|xRef2: "+EoriginalHelper.parseXMLBody(properties, "xRef2"));
			if(EoriginalHelper.parseXMLBody(properties, "state").equals("eDeposit Complete")) {
				if(deleteTransactionBySID(SID)){
					returnValue = true;
					System.out.println("deleteTransactionByEnvId|env with id "+envId+" is deleted successfully...");
				} else {
					System.out.println("deleteTransactionByEnvId|Unable to delete transaction with env with id "+envId+"...");
				}
				
			} else {
				System.out.println("deleteTransactionByEnvId|its not complete so not deleting it|state: "+EoriginalHelper.parseXMLBody(properties, "state"));
			}

		}
		return returnValue;
	}
	
	public String getPropertiesByEnvEnvId(String envId) {
		String returnValue = "";
		if(loginEOriginal()) {
			String SID = getSIDByEnvId(envId);
			returnValue = getPropertiesByEnvSID(SID);
			
			System.out.println("deleteTransactionByEnvId|properties: "+returnValue);
			System.out.println("deleteTransactionByEnvId|state: "+EoriginalHelper.parseXMLBody(returnValue, "state"));
			System.out.println("deleteTransactionByEnvId|xRef1: "+EoriginalHelper.parseXMLBody(returnValue, "xRef1"));
			System.out.println("deleteTransactionByEnvId|xRef2: "+EoriginalHelper.parseXMLBody(returnValue, "xRef2"));
		}
		return returnValue;
	}
	
	
	
	public static void main(String args[]) {
		TransactionImpl transactionImpl = new TransactionImpl();
		transactionImpl.getPropertiesByEnvEnvId(EoriginalHelper.tmpEnvId);
	}
	

	
	
}
