package com.coolshop.task;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.coolshop.bean.Orders;
import com.example.coolshop.R;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
/**
 * 上下拉订单处理事件
 */
public class ChangeOrder extends AsyncTask<String, Void, String> {

	private PullToRefreshScrollView mPullScrollView;
	private Context mcontext;
	private String Result;
	private RelativeLayout order_detail;

	public ChangeOrder(PullToRefreshScrollView mPullScrollView,Context mcontext, String Result,RelativeLayout order_detail) {
		this.mPullScrollView = mPullScrollView;
		this.mcontext = mcontext;
		this.Result = Result;
		this.order_detail = order_detail;
	}

	@Override
	protected String doInBackground(String... params) {
		String ret = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(params[0]);
		HttpResponse response;
		try {
			response = httpClient.execute(httpget);
			int res = response.getStatusLine().getStatusCode();
			if (res == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String Result = EntityUtils.toString(entity, "utf8");
					System.out.println("Result-->>" + Result);
					JSONObject json = new JSONObject(Result);
					ret = json.getString("ret");
					return ret;
				}
			} else {
				Toast.makeText(mcontext, "响应不通过！",Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	@Override
	protected void onPostExecute(String result) {

		if (result.equals("success")) {
			mPullScrollView.onRefreshComplete();
			if(Result.equalsIgnoreCase("订单已删除!")){
				order_detail.setBackgroundResource(R.drawable.panel_side_gray);
			}else{
				order_detail.setBackgroundResource(R.drawable.panel_side_green);
			}
			Toast.makeText(mcontext, Result, Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}

}
