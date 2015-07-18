package com.example.coolshop;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.widget.Toast;

public class Download extends AsyncTask<String, String, String> {
	OrderData orderdata;
	public Download(OrderData orderdata){
		this.orderdata = orderdata;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return Mydownjson(params[0]);
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);		
		DisplayJson(result);								
	}

	/**
	 * 访问服务器获取Json数据
	 * 
	 */
	public String Mydownjson(String url) {
		String result = "";
		HttpGet httpget = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(httpget);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");

			}else{
				System.out.println("网络异常，请检查网络设置！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * JSON解释
	 */

	public void DisplayJson(String jsonstring) {
		try {
			JSONObject object = new JSONObject(jsonstring);
			//orderdata = new OrderData(object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
