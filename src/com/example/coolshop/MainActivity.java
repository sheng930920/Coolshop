package com.example.coolshop;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class MainActivity extends FragmentActivity {

	private ViewPager mPager;
	private Myapplication app;
	private OrderData orderdata;
	private int uid;
	private String token;
	Myadapter myadapter;
	public String mjson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		app = (Myapplication) getApplication();		
		mPager = (ViewPager) findViewById(R.id.viewpager);
		new GetJson().execute("http://shop.coolpoint.cc/admin/api/get/?ac=get_order");
		System.out.println("json-->>"+mjson);
//		orderdata = new OrderData(mjson);	
//		myadapter = new Myadapter(orderdata);
//		mPager.setAdapter(myadapter);
//		mPager.setCurrentItem(0);
//		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			 Builder dialog = new AlertDialog.Builder(this)
			 . setMessage("是否退出应用？")
			 .setPositiveButton("取消", new DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                    	dialog.cancel();
                    } 
                })
                .setNegativeButton("确认", new DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                       System.exit(0);
                    } 
                });
			 dialog.show();
			
		}
		return false;
	}

	class GetJson extends AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String... params) {

			return getJSONFromServer(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			mjson = result;
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
					//System.out.println("Json是-->>>" + temp);
					return temp;
				}
			} else {
				Toast.makeText(getApplication(), "响应不通过！", Toast.LENGTH_SHORT).show();
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
		myadapter = new Myadapter(orderdata);
		mPager.setAdapter(myadapter);
    	mPager.setCurrentItem(0);
	}

	/**
	 * Viewpager适配器
	 */
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

			final PullToRefreshScrollView mPullScrollView = (PullToRefreshScrollView) mLayout.findViewById(R.id.mPullScrollView);

			final int iD = orderdata.list.get(position).getID();
			final String phoneNum = orderdata.list.get(position).getOrderPhone();
			
			mPullScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

						@Override
						public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
							// 下拉确认订单
							if (PullToRefreshBase.Mode.PULL_FROM_START == mPullScrollView.getCurrentMode()) {

								String Url = "http://shop.coolpoint.cc/admin/api/get/?ac=set_order&uid="
										+ uid
										+ "&token="
										+ token
										+ "&id="
										+ iD
										+ "&phone=" + phoneNum;
								String result = "订单已确认!";

								new ChangeOrder(mPullScrollView,MainActivity.this, result).execute(Url);

							} else if (PullToRefreshBase.Mode.PULL_FROM_END == mPullScrollView.getCurrentMode()) {
								// 上拉删除订单
								String Url = "http://shop.coolpoint.cc/admin/api/get/?ac=delete_order&uid="
										+ uid + "&token=" + token + "&id=" + iD;
								String result = "订单已删除!";
								
								new ChangeOrder(mPullScrollView,MainActivity.this, result).execute(Url);
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
			detail.setAdapter(new ListAdapter(getApplication(), orderdata.list.get(position).info));

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
