package com.android.zmark.bean.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

public class JSONUtil {

	public static ObjectMapper obm= new ObjectMapper();
	private static JSONUtil jsonutil=null;
	private JSONUtil(){
		
	}
	public static JSONUtil getInstance(){
		if(jsonutil==null){
			jsonutil= new JSONUtil();
		}
		return jsonutil;
	}
	public List<?> toList(String json, Class<?> bean) throws Exception{
		List<?>list=obm.readValue(json, TypeFactory.collectionType(ArrayList.class, bean));
		
		return list;
		
	}
	
	 public Map<?, ?> toMap(String json) throws Exception {
	        return obm.readValue(json, Map.class);
	    }

}
