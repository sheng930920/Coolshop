package com.example.coolshop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OrderData {

	public static ArrayList<Orders> list = new ArrayList<Orders>();

	public OrderData(String jsonstring) {
		list.clear();
		Gson gson = new Gson();
		
		java.lang.reflect.Type listType = new TypeToken<LinkedList<Orders>>(){}.getType();
		LinkedList<Orders> users = gson.fromJson(jsonstring, listType);
		
		
		for (Iterator iterator = users.iterator(); iterator.hasNext();) {
			Orders orders = (Orders) iterator.next();
			System.out.println("ID-->>"+orders.getID()+"   orderPhone-->"+orders.getOrderPhone());
			list.add(orders);
		}
	}
}
