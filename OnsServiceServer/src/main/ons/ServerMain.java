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
import java.nio.charset.CharsetDecoder;
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
import java.io.DataOutput;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.net.UnknownHostException;
import java.net.SocketException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import main.ons.JsonBuilder;

public class ServerMain{
	private final static Logger logger = LoggerFactory.getLogger(ServerMain.class);
	
	private final static int PORT_NUM = 7777;
	private static JsonBuilder jsonObject=null;
	private static ServerSocket serverSocket = null;
	private PrintWriter pw_sock = null;
	
	public ServerMain(){
		jsonObject = new JsonBuilder();
	}
	
	public static void main(String[] args) throws IOException{
		try{
			serverSocket = new ServerSocket(PORT_NUM);
			logger.info("Server is ready...");
		}catch(IOException e){
			e.printStackTrace();
		}
		
		while(true){
			try{
				logger.info("Server is listening...");
				Socket socket = serverSocket.accept();
				logger.info("{} send connection request", socket.getInetAddress());
				
				InputStream in = socket.getInputStream();
				DataInputStream din = new DataInputStream(in);
				OutputStream out = socket.getOutputStream();
				DataOutputStream dout = new DataOutputStream(out);
				
				String sgtinCode = din.readUTF();
				logger.info("IP: {} sent sgtin string : {}",socket.getInetAddress(),sgtinCode);
												
				//String strJson = jsonObject.jsonToString();
				String strJson = "Hello!";
				
				//<-- here, NAPTR lookup code needs
				try{
					dout.writeUTF(strJson);
				}catch(IOException e){
					e.printStackTrace();
				}
				
				din.close();
				dout.close();
				socket.close();
				logger.info("Connection from {} is closed...",socket.getInetAddress());
				
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}