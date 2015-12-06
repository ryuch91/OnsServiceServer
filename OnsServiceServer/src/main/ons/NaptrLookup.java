package main.ons;

import java.net.UnknownHostException;

import java.net.InetAddress;

import org.xbill.DNS.Cache;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.NAPTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class NaptrLookup {
	
	private static String RESOLVER_ADDRESS = "143.248.55.143";
	private static int RESOLVER_PORT = 53;
	private static final String[] LOCAL_SEARCH_PATH = { "127.0.1.1" };
	private static String DN;
	
	public NaptrLookup(){
		RESOLVER_ADDRESS = "143.248.55.143";
		RESOLVER_PORT = 53;
	}
	
	public NaptrLookup(String resolver_addr, int resolver_port){
		RESOLVER_ADDRESS = resolver_addr;
		RESOLVER_PORT = resolver_port;
	}
	
	public String getNode(String domain_name) throws InterruptedException, UnknownHostException, TextParseException{
		DN = domain_name;
		
		SimpleResolver resolver = new SimpleResolver(RESOLVER_ADDRESS);
		
		resolver.setPort(RESOLVER_PORT);
		
		Lookup.setDefaultResolver(resolver);
		Lookup.setDefaultSearchPath(LOCAL_SEARCH_PATH);
		Lookup.setDefaultCache(new Cache(), DClass.IN);
		
		Lookup lookup = new Lookup(DN, Type.NAPTR);
		Record[] records = lookup.run();
		String nodeName = null;
		
		if(lookup.getResult() == Lookup.SUCCESSFUL){
			for(Record record : records){
				NAPTRRecord naptrRecord = (NAPTRRecord) record;
				if(naptrRecord.getFlags().contains("U")){
					if(nodeName == null){
						nodeName = naptrRecord.getService().toString();
					}else{
						nodeName = nodeName + " " + naptrRecord.getService().toString();
					}
					String resource = naptrRecord.getRegexp();
					System.out.println("Candidate node: " + nodeName);
					System.out.println("Regexp : "+ resource);
				}
			}
		}
		else{
			System.out.println("Lookup fails!");
			nodeName = "";
		}
		return nodeName;
	}
	
public String getResource(String domain_name) throws InterruptedException, UnknownHostException, TextParseException{
		DN = domain_name;
	
		SimpleResolver resolver = new SimpleResolver(RESOLVER_ADDRESS);
		
		resolver.setPort(RESOLVER_PORT);
		
		Lookup.setDefaultResolver(resolver);
		Lookup.setDefaultSearchPath(LOCAL_SEARCH_PATH);
		Lookup.setDefaultCache(new Cache(), DClass.IN);
		
		Lookup lookup = new Lookup(DN, Type.NAPTR);
		Record[] records = lookup.run();
		String resource = null;
		
		if(lookup.getResult() == Lookup.SUCCESSFUL){
			for(Record record : records){
				NAPTRRecord naptrRecord = (NAPTRRecord) record;
				if(naptrRecord.getFlags().contains("U")){
					String nodeName = naptrRecord.getService().toString();
					resource = naptrRecord.getRegexp();
					System.out.println("Candidate node: " + nodeName);
					System.out.println("Regexp : "+ resource);
				}
			}
		}
		else{
			System.out.println("Lookup fails!");
			resource = "";
		}
		return resource;
	}
	
}