package com.red263.commmodule;

import java.lang.reflect.Type;
import java.util.LinkedList;
import com.google.gson.Gson;  
import com.google.gson.reflect.TypeToken;

public class OrderJsonUtils {
	
	public LinkedList<OrderListOrder> parseUserFromJson(String jsonData){	
		Type listType = new TypeToken<LinkedList<OrderListOrder>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<OrderListOrder> order = gson.fromJson(jsonData, listType);
	    return order;
	}
}
