package com.example.coolshop;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private ViewPager mPager;
	private Myapplication app;
	OrderData orderdata;
	private int uid;
	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();

	}

	private void init() {
		app = (Myapplication) getApplication();
		new GetJson().execute("http://shop.coolpoint.cc/admin/api/get/?ac=get_order");
		mPager = (ViewPager) findViewById(R.id.viewpager);

	}

	class GetJson extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			return getJSONFromServer(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			DisplayJson(result);
		}

	}

	/**
	 * 访问服务器获取Json数据
	 */
	public String getJSONFromServer(String url) {
		 uid = app.getID();
		 token = app.getToken();
		String Url = url + "&uid=" + uid + "&token=" + token;
		String temp = "";
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(Url);
		try {
			HttpResponse response = httpClient.execute(httpget);
			int res = response.getStatusLine().getStatusCode();
			if (res == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String Result = EntityUtils.toString(entity, "utf8");
					// System.out.println("获得的json-->>"+Result);
					int start = Result.indexOf("[{");
					int stop = Result.lastIndexOf("}]");
					temp = Result.substring(start, stop + 2);
					System.out.println("Json是-->>>" + temp);
					return temp;

				}
			} else {
				Toast.makeText(getApplication(), "响应不通过！", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * JSON解释
	 */

	public void DisplayJson(String jsonstring) {
		orderdata = new OrderData(jsonstring);
		mPager.setAdapter(new Myadapter(orderdata));
		mPager.setCurrentItem(0);
	}

	public class Myadapter extends PagerAdapter {

		private LayoutInflater inflater;
		OrderData orderdata;

		public Myadapter(OrderData orderdata) {
			inflater = getLayoutInflater();
			this.orderdata = orderdata;
		}

		@Override
		public int getCount() {
			return orderdata.list.size();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View mLayout = inflater.inflate(R.layout.detail, container, false);

			TextView ID = (TextView) mLayout.findViewById(R.id.ID);
			TextView orderID = (TextView) mLayout.findViewById(R.id.orderID);
			TextView orderPhone = (TextView) mLayout
					.findViewById(R.id.orderPhone);
			TextView createtime = (TextView) mLayout
					.findViewById(R.id.createtime);
			TextView orderNote = (TextView) mLayout
					.findViewById(R.id.orderNote);
			TextView total = (TextView) mLayout.findViewById(R.id.total);
			ListView detail = (ListView) mLayout.findViewById(R.id.detail);

			final PullToRefreshScrollView mPullScrollView = (PullToRefreshScrollView) mLayout
					.findViewById(R.id.mPullScrollView);
			
			final int iD = orderdata.list.get(position).getID();
			final String phoneNum = orderdata.list.get(position).getOrderPhone();
			
			mPullScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

						@Override
						public void onRefresh(
								PullToRefreshBase<ScrollView> refreshView) {
							// mPullScrollView.onRefreshComplete();
							if (PullToRefreshBase.Mode.PULL_FROM_START == mPullScrollView
									.getCurrentMode()) {
			String Url = "http://shop.coolpoint.cc/admin/api/get/?ac=set_order&uid="+uid+"&token="+token+"&id="+iD+"&phone="+phoneNum;
								new ChangeOrder(mPullScrollView).execute(Url);
								
							} else if (PullToRefreshBase.Mode.PULL_FROM_END == mPullScrollView
									.getCurrentMode()) {
								Toast.makeText(MainActivity.this, "上拉啦", 4000)
										.show();
							}

						}
					});

			// System.out.println("ID值是--->>"+orderdata.list.get(position).getID());total
			String id = String.valueOf(orderdata.list.get(position).getID());
			ID.setText(id);
			orderID.setText(orderdata.list.get(position).getOrderID());
			orderPhone.setText(orderdata.list.get(position).getOrderPhone());
			createtime.setText(orderdata.list.get(position).getCreatetime());
			orderNote.setText(orderdata.list.get(position).getOrderNote());
			total.setText(orderdata.list.get(position).getTotal());
			detail.setAdapter(new ListAdapter(getApplication(), orderdata.list
					.get(position).info));

			((ViewPager) container).addView(mLayout, 0); // 将视图增加到ViewPager
			return mLayout;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

	private class ChangeOrder extends AsyncTask<String, Void, String> {
		
		PullToRefreshScrollView mPullScrollView;
		public ChangeOrder(PullToRefreshScrollView mPullScrollView){
			this.mPullScrollView = mPullScrollView;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			String ret = "";
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(arg0[0]);
			HttpResponse response;
			try {
				response = httpClient.execute(httpget);
				int res = response.getStatusLine().getStatusCode();
				if(res == 200){
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						String Result = EntityUtils.toString(entity,"utf8");
						System.out.println("Result-->>"+Result);
						JSONObject json = new JSONObject(Result);
						ret = json.getString("ret");
						return ret;
					}
				}else{
					Toast.makeText(getApplication(), "响应不通过！", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {

			if(result.equals("success")){
				mPullScrollView.onRefreshComplete();
				Toast.makeText(MainActivity.this, "订单已确认", 4000).show();
			}
			super.onPostExecute(result);
		}

	}

}
