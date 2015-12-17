package com.red263.commmodule;

import java.lang.reflect.Type;
import java.util.LinkedList;
import com.google.gson.Gson;  
import com.google.gson.reflect.TypeToken;

public class JsonUtils {
	
	public LinkedList<DriverListOrder> parseUserFromJson(String jsonData){	
		Type listType = new TypeToken<LinkedList<DriverListOrder>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<DriverListOrder> order = gson.fromJson(jsonData, listType);
	    return order;
	}
}
