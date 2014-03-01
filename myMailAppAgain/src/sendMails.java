

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class sendMails{
	private static HashMap<String, ProductAndUser> map=new HashMap<String, ProductAndUser>();
	
	//Constructor
	public sendMails(HashMap<String, ProductAndUser> hm){
		//System.out.println("trd 2 const");
		sendMails.map = hm;
	}
	
	
	//@Override
	public HashMap<String, ProductAndUser> call() throws Exception {
		System.out.println("trd 2 run");
		
		
	      System.out.println("Task 2 Running"); 
	      if(!map.isEmpty()){ 
	    	  
	    	  System.out.println("keys in hashmap in task 2" +map.keySet());
	    	  //process the data in hashmap by sending emails and deleting the corresponding products
	    	  //for(int i=0;i<map.size();i++){
	    		for(String e: map.keySet()){  
	    			System.out.println("For product "+e);
	    			retrieveProductStatus(e);
	    			
	    			//for(int j=1;j<e.getValue().size();j++){
	    		//	retrieveProductStatus(e.getKey(),e.getValue().get(j));
	    		//	}
	    	  }
	    		sendEmails();
	      }
	     // sendEmails();  //only to test the method, comment it later
	      
	      deleteEntries();
		return map;
	}

	
	private void deleteEntries() {
		Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        ProductAndUser tm=(ProductAndUser) pairs.getValue();
	        if(Integer.parseInt(tm.percentOff)>20){
	        System.out.println("Removing entry for "+pairs.getKey());
	        it.remove(); // avoids a ConcurrentModificationException
	        }
	    }
		
		
	}


	protected static void sendEmails() {
		
		System.out.println("-------");
		System.out.println("Starting sendEmails ");
	    for(Entry<String, ProductAndUser> entry: map.entrySet()) {
	    	
	    	System.out.println(entry.getKey() + " : " + entry.getValue());
	        for(String em:entry.getValue().emailIds){
	        	
	        	System.out.println(em);
	        	System.out.println("///////////////////////////////");
	        }

	    }
	    
	    
		for(Entry<String, ProductAndUser> e : map.entrySet()){ //this wont execute if map is empty
			int pid =Integer.parseInt((String) e.getKey());
			int poff=Integer.parseInt(e.getValue().percentOff);
			
			//System.out.println(pid+" "+e.getValue());
			
			if((!e.getValue().percentOff.isEmpty())&& e.getValue().emailIds.size()>0){
				if(poff>20){
					/*for(String eid: e.getValue().emailIds){
						sendMail(pid,poff,eid);
					}*/
					sendMail(e.getValue());
				}
				System.out.println("emails sent");
			}
		
		}
		
	
		System.out.println("Finishing sendEmails ");
		
	}

private static void sendMail(ProductAndUser prodUsers) {

	
	Session session = Session.getDefaultInstance(fMailServerConfig, new javax.mail.Authenticator(){
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(fMailServerConfig.getProperty("mail.smtp.user"),
            		fMailServerConfig.getProperty("mail.smtp.password"));}});
    MimeMessage message = new MimeMessage(session);
    try {
      //the "from" address may be set in code, or set in the
      //config file under "mail.from" ; here, the latter style is used
      //message.setFrom(new InternetAddress(aFromEmailAddr));
    	StringBuilder sb=new StringBuilder();
    	for(String eid:prodUsers.emailIds){
    		sb.append(eid+",");
    	}
    	sb.deleteCharAt(sb.lastIndexOf(","));
    	System.out.println(sb);
    	
    	message.addRecipients(
        Message.RecipientType.TO, InternetAddress.parse(sb.toString()));
    		  
    		  //following line is only to test sending an email, comment it later
      //Message.RecipientType.TO, new InternetAddress(fMailServerConfig.getProperty("toAddress")));
     message.setSubject("Zappos: "+prodUsers.productName+" has reached "+prodUsers.percentOff+"% discount!! :)");
     // message.setText("Hello, The product "+pid+" has a discount of "+poff+ "now ! Hurry to grab it !!");
     
   // Create the message part 
     // MimeBodyPart messageBodyPart = new MimeBodyPart();

      String body="<h1>Hello,</h1>";
      body+="<p>The product you requested now has a whopping discount of "+ prodUsers.percentOff+"%";
      body+="<br><br>"+"<a href=\""+prodUsers.productUrl+"\">Click me for the product's link!</a>";
      body+="<br><br>Thank you,<br>The Zappos Team"+"</p>";
      // Fill the message
    //  messageBodyPart.setText(body,"UTF-8","html");

    //  Multipart multipart = new MimeMultipart();
    //  multipart.addBodyPart(messageBodyPart);

      // Put parts in message
      message.setText(body,"UTF-8","html");
      
      Transport.send(message);
    }
    catch (MessagingException ex){
      System.err.println("Cannot send email. " + ex);
    }
   // System.out.println(pid);
    
   
  // System.out.println("removed = "+ s.remove(aToEmailAddr));
   // map.get(pid).remove(aToEmailAddr);
  }

  /**
  * Allows the config to be refreshed at runtime, instead of
  * requiring a restart.
  */
  public static void refreshConfig() {
    fMailServerConfig.clear();
    fetchConfig();
  }

  // PRIVATE 

  private static Properties fMailServerConfig = new Properties();

  static {
    fetchConfig();
  }

  /**
  * Open a specific text file containing mail server
  * parameters, and populate a corresponding Properties object.
  */
  private static void fetchConfig() {
    //This file contains the javax.mail config properties mentioned above.
    Path path = Paths.get("MyMailConfig.txt");
    try (InputStream input = Files.newInputStream(path)) {
      fMailServerConfig.load(input);
    }
    catch (IOException ex){
      System.err.println("Cannot open and load mail server properties file.");
    }

		
	}


private static void sendMail(int pid, int poff, String aToEmailAddr) {
	
	Session session = Session.getDefaultInstance(fMailServerConfig, new javax.mail.Authenticator(){
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(fMailServerConfig.getProperty("mail.smtp.user"),
            		fMailServerConfig.getProperty("mail.smtp.password"));}});
    MimeMessage message = new MimeMessage(session);
    try {
      //the "from" address may be set in code, or set in the
      //config file under "mail.from" ; here, the latter style is used
      //message.setFrom(new InternetAddress(aFromEmailAddr));
      message.addRecipient(
    		  //uncomment the following line to send mail to the users
        Message.RecipientType.TO, new InternetAddress(aToEmailAddr));
    		  
    		  //following line is only to test sending an email, comment it later
      //Message.RecipientType.TO, new InternetAddress(fMailServerConfig.getProperty("toAddress")));
      message.setSubject("Product "+pid+" has reached "+poff+" discount!!");
      message.setText("Hello, The product "+pid+" has a discount of "+poff+ "now ! Hurry to grab it !!");
      Transport.send(message);
    }
    catch (MessagingException ex){
      System.err.println("Cannot send email. " + ex);
    }
    System.out.println(pid);
    
   
  // System.out.println("removed = "+ s.remove(aToEmailAddr));
   // map.get(pid).remove(aToEmailAddr);
  }

  

public static void retrieveProductStatus(String pid) {
	
// open the URL connection to the REST api
try {
	URL url = new URL(
			"http://api.zappos.com/Search?&filters={\"productId\":[\""+pid+"\"]}&key=67d92579a32ecef2694b74abfc00e0f26b10d623");
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setDoOutput(true);
	conn.setAllowUserInteraction(false);

	// bad news: the URL is not right. We have to play the guessing game!
	if (conn.getResponseCode() != 200) {
		//return fixSKU(skuNumber);
	}

	// buffer the response into a string
	BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	StringBuilder sb = new StringBuilder();
	String line;
	while ((line = rd.readLine()) != null) {
		sb.append(line);
	}
	rd.close();
	conn.disconnect(); 
	
	// parse response and return URL
	String JSONstring = sb.toString();
	productJSONParse(JSONstring,pid);
	
	
//	return prices;
	
} catch (MalformedURLException e) {
	e.printStackTrace();
} catch (IOException e) {
	e.printStackTrace();
}

//return null;
}


/**
* Given a JSON object of the response after searching a product, return a list with the 
* default images of the products in the response.
* @param ImageJSON
* @return
*/
public static void productJSONParse(String ProdJSON, String pid) {
JSONObject JSONresponse;
String tempPoff = null;
String tPoff = null;
String turl=null;
String tpname=null;
String comparedUrl=null;
String comparedPname=null;
Integer toCompTmp=0, toComp;
try {
	JSONresponse = new JSONObject(ProdJSON);
	JSONArray JSONproducts = JSONresponse.getJSONArray("results");
	
//	String[] prices = new String[JSONprices.length()];
	if(JSONproducts.length()<1){System.out.println("The product does not exist !");}
	else{
	
	
	
	for (int i = 0; i < JSONproducts.length(); i++) {
		//prices[i] = ((JSONObject) JSONprices.get(i)).getString("price");
		tPoff=((JSONObject) JSONproducts.get(i)).getString("percentOff");
		tempPoff=String.valueOf(tPoff.substring(0,tPoff.lastIndexOf('%')));
		
		turl=((JSONObject) JSONproducts.get(i)).getString("productUrl");
		tpname=((JSONObject) JSONproducts.get(i)).getString("productName");
		toComp=Integer.parseInt(tempPoff);
		if(toComp>toCompTmp){
			toCompTmp=toComp;
			comparedPname=tpname;
			comparedUrl=turl;
		}
		
	//	Product prod=new Product(pid,tempPoff);
		
	}
	//if(there==false){
	if(map.containsKey(pid)){//add the email address to the arraylist of map
		System.out.println("percent off in hashmap updated to "+toCompTmp);
		map.get(pid).setPoff(toCompTmp.toString());
		map.get(pid).setPname(comparedPname);
		map.get(pid).setPurl(comparedUrl);
		//map.get(pid).set(0, toCompTmp.toString());
	}
	
	
//	}	
}
	System.out.println(map.entrySet());
} catch (JSONException e) {
	// this might mean that the API sent us a malformed JSON object and that is bad!
	e.printStackTrace();
}
//return null;
}


}

