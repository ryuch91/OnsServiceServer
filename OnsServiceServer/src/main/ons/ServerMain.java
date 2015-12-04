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
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;

public class ServerMain{
	private final static Logger logger = LoggerFactory.getLogger(ServerMain.class);
	
	public static void main(String[] args) throws IOException{
		ServerSocket serverSck = null;
		Socket clientSck = null;
		PrintWriter out = null;
		BufferedReader in = null;
		
		
		serverSck = new ServerSocket(7777);
		
		try{
			logger.info("Server is listening...");
			clientSck = serverSck.accept();
			logger.info("Client is connected.");
			out = new PrintWriter(clientSck.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSck.getInputStream()));
			while(true){
				String inputLine = null;
				inputLine = in.readLine();
				logger.info("String from Client : {}",inputLine);
				if(inputLine.equals("quit"))
					break;
			}
			out.close();
			in.close();
			clientSck.close();
			serverSck.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}