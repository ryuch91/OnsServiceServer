package main.ons;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.IOException;
import java.io.StringWriter;

public class JsonBuilder{
	private JSONObject jsonObj = null;
	
	public JsonBuilder(){
		jsonObj = new JSONObject();
	}
	
	public void addJson(Object key, Object value){
		jsonObj.put(key, value);
	}
	
	public String jsonToString(){
		StringWriter sw = new StringWriter();
		try{
			jsonObj.writeJSONString(sw);
		}catch(IOException e){
			e.printStackTrace();
		}
		String str = JSONValue.toJSONString(sw);
		return str;
	}
	
	public String getValueToString(Object key){
		String str = null;
		try{
			str = jsonObj.get(key).toString();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		str = jsonObj.get(key).toString();
		return str;
	}
}