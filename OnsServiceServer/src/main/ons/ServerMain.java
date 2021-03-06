package main.ons;

//imported for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//imported for communication with android client
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.net.SocketException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import main.ons.JsonBuilder;

import main.ons.NaptrLookup;

import org.fosstrak.tdt.TDTException;

public class ServerMain{
	private final static Logger logger = LoggerFactory.getLogger(ServerMain.class);
	private final static int PORT_NUM = 7777;

	public ServerMain(){
	}
	
	public static void main(String[] args) throws IOException{
		
		Thread serverThread = new Thread(new TCPServer());
		serverThread.start();
	}
	
	private static class TCPServer implements Runnable{
		private String domainName=null;
		private ServerSocket serverSocket;
		private NaptrLookup naptrLookup;
		private JsonBuilder jsonBuilder=null;
		private String lookupResult;
		private TagTranslator tagTranslator;
		
		private String onsHostName;
		private String onsHostNameArranged;
		private String epcTagURI;
		public void run(){
			try{
				logger.info("Server: Openning...");
				serverSocket = new ServerSocket(PORT_NUM);
				naptrLookup = new NaptrLookup();
				jsonBuilder = new JsonBuilder();
				tagTranslator = new TagTranslator();
				
				while(true){
					Socket client = serverSocket.accept();
					logger.info("Server: Receiving...");
					logger.info("{} send connection request", client.getInetAddress());
					try{
						BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
						String str = in.readLine();
						//Test String : (01)0 8800001 00024 3 (21)001
						//String str = "010880000100024321001";
						
						logger.info("Server : Received '{}' from client",str);
						
						try{
							epcTagURI = tagTranslator.elementStringToEpcTagURI(str);
							onsHostName = tagTranslator.epcTagUriToOnsHostname(epcTagURI);
							onsHostNameArranged = tagTranslator.arrangeOnsHostname(onsHostName);
							logger.info("Translation success: {} -> {}", str, onsHostNameArranged);
							
							logger.info("Server starts to lookup...");
							try{
								domainName = onsHostNameArranged;
								lookupResult = naptrLookup.getNode(domainName);
								logger.info("Lookup result is : {}",lookupResult);
							}catch(NullPointerException e){
								logger.info("Lookup failed");
								e.printStackTrace();
							}
							
						}catch(TDTException e){
							e.printStackTrace();
							logger.info("Translation failed. Input value '{}' is wrong.",str);
						}
					
						//JSON building part
						int tokenCounter = 1;
						StringTokenizer strToken = new StringTokenizer(lookupResult," ");
						while(strToken.hasMoreTokens()){
							jsonBuilder.addJson("service"+Integer.toString(tokenCounter),strToken.nextToken());
							tokenCounter++;
						}
						//<-- here, Translate it with JSON and send JSON value to client
						
						//<JSON USAGE>	
						//String strJson = jsonObject.jsonToString();
						//jsonObject.addJson("a", "Hello");
						//jsonObject.addJson("b", "Hi");
						//String strJson = jsonObject.jsonToString();
						
						PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
						out.println("Lookup Result:"+jsonBuilder.jsonToString());
					}catch(Exception e){
						logger.info("Stream open error");
						e.printStackTrace();
					}finally{
						client.close();
						jsonBuilder.initializeHash();
						logger.info("Server: Done");
					}
				}
			}catch(IOException e){
				logger.info("Server socket open error");
				try {
					serverSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}
}