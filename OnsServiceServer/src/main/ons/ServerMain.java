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
import java.net.SocketException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import main.ons.JsonBuilder;

public class ServerMain{
	private final static Logger logger = LoggerFactory.getLogger(ServerMain.class);
	
	private final static int PORT_NUM = 7777;
	private static JsonBuilder jsonObject=null;
	
	public ServerMain(){
		jsonObject = new JsonBuilder();
	}
	
	public static void main(String[] args) throws IOException{
		
		Thread serverThread = new Thread(new TCPServer());
		serverThread.start();
	}
	
	private static class TCPServer implements Runnable{
		private ServerSocket serverSocket;
		public void run(){
			try{
				logger.info("Server: Openning...");
				serverSocket = new ServerSocket(PORT_NUM);
				
				while(true){
					Socket client = serverSocket.accept();
					logger.info("Server: Receiving...");
					logger.info("{} send connection request", client.getInetAddress());
					try{
						BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
						String str = in.readLine();
						logger.info("Server : Received '{}' from client",str);
						
						//<-- here, NAPTR lookup code needs
						
						//<-- here, Translate it with JSON and send JSON value to client
						
						//<JSON USAGE>	
						//String strJson = jsonObject.jsonToString();
						//jsonObject.addJson("a", "Hello");
						//jsonObject.addJson("b", "Hi");
						//String strJson = jsonObject.jsonToString();
						
						PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
						out.println("Server received "+str);
					}catch(Exception e){
						logger.info("Stream open error");
						e.printStackTrace();
					}finally{
						client.close();
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