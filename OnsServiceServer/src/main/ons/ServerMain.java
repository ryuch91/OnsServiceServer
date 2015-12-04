package main.ons;

//imported for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//imported for communication with android client
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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