package com.example.coolshop;

import java.util.Timer;
import java.util.TimerTask;

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
import android.os.Handler;
import android.os.Message;
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
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class MainActivity extends FragmentActivity {

	private ViewPager mPager;
	private Myapplication app;
	private OrderData orderdata;
	private int uid;
	private String token;
	private Myadapter myadapter;
	
	private String jsonstring;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		timer.schedule(task,0,5000);
		init();
	}

	private void init() {
		app = (Myapplication) getApplication();		
		uid = app.getID();
		token = app.getToken();
		mPager = (ViewPager) findViewById(R.id.viewpager);
		new GetJson().execute("http://shop.coolpoint.cc/admin/api/get/?ac=get_order");
	}
	
	Timer timer = new Timer( );
	TimerTask task = new TimerTask( ) {
		public void run ( ) {
			new GetJson().execute("http://shop.coolpoint.cc/admin/api/get/?ac=get_order");
		}
	};
	
	/**
	 *异步访问网络
	 */
	class GetJson extends AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String... params) {

			return getJSONFromServer(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			
			System.out.println("result是-->>>" + result);
			jsonstring = result;
			DisplayJson(result);
		}

	}

	/**
	 * 访问服务器获取Json数据
	 */
	public String getJSONFromServer(String url) {
		
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
		myadapter = new Myadapter(MainActivity.this,orderdata);
		mPager.setAdapter(myadapter);
    	mPager.setCurrentItem(0);
	}
	
	/**
	 * 返回键响应事件
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			 Builder dialog = new AlertDialog.Builder(this). setMessage("是否退出应用？")
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

}
