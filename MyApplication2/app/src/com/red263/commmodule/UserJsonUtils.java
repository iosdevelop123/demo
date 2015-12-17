package com.red263.commmodule;

import java.lang.reflect.Type;
import java.util.LinkedList;
import com.google.gson.Gson;  
import com.google.gson.reflect.TypeToken;

public class UserJsonUtils {
	
	public LinkedList<UserOrder> parseUserFromJson(String jsonData){	
		Type listType = new TypeToken<LinkedList<UserOrder>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<UserOrder> order = gson.fromJson(jsonData, listType);
	    return order;
	}
}
