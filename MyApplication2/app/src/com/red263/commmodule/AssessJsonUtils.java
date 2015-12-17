package com.red263.commmodule;

import java.lang.reflect.Type;
import java.util.LinkedList;
import com.google.gson.Gson;  
import com.google.gson.reflect.TypeToken;

public class AssessJsonUtils {
	
	public LinkedList<AssessListOrder> parseUserFromJson(String jsonData){	
		Type listType = new TypeToken<LinkedList<AssessListOrder>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<AssessListOrder> order = gson.fromJson(jsonData, listType);
	    return order;
	}
}
