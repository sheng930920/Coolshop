package com.example.coolshop;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private ViewPager mPager;
	private Myapplication app;
	OrderData orderdata;

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
		int uid = app.getID();
		String token = app.getToken();
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
			View mLayout = inflater.inflate(R.layout.detail, container,false);
			
			TextView ID = (TextView) mLayout.findViewById(R.id.ID);
			TextView orderID = (TextView) mLayout.findViewById(R.id.orderID);
			TextView orderPhone = (TextView) mLayout.findViewById(R.id.orderPhone);
			TextView createtime = (TextView) mLayout.findViewById(R.id.createtime);
			TextView orderNote = (TextView) mLayout.findViewById(R.id.orderNote);
			
			//System.out.println("ID值是--->>"+orderdata.list.get(position).getID());
			String id = String.valueOf(orderdata.list.get(position).getID());
			ID.setText(id);
			orderID.setText(orderdata.list.get(position).getOrderID());
			orderPhone.setText(orderdata.list.get(position).getOrderPhone());
			createtime.setText(orderdata.list.get(position).getCreatetime());
			orderNote.setText(orderdata.list.get(position).getOrderNote());
			((ViewPager) container).addView(mLayout, 0); // 将视图增加到ViewPager
			return mLayout;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

}
