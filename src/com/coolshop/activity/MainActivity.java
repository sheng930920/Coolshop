package com.coolshop.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.coolshop.adapter.OrderAdapter;
import com.coolshop.app.Myapplication;
import com.coolshop.bean.Orders;
import com.coolshop.task.OrderData;
import com.example.coolshop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class MainActivity extends FragmentActivity {

	private ViewPager mPager;
	private ProgressDialog pDialog;
	private Myapplication app;
	private OrderData orderdata;
	private int uid;
	private String token;
	private OrderAdapter myadapter;
	private SoundPool sp;
	private int music;
	private ArrayList<Fragment> fragments;
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		app = (Myapplication) getApplication();
		timer = new Timer();
		uid = app.getID();
		token = app.getToken();
		mPager = (ViewPager) findViewById(R.id.viewpager);
		fragments = new ArrayList<Fragment>();
		sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		music = sp.load(this, R.raw.ding, 1);
		getJSONFromServer();
	}
	
	TimerTask task = new TimerTask() {
		public void run() {
			getlastOrder();
		}
	};

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				sp.play(music, 1, 1, 0, 0, 1);
				updateOrder();
				break;
			}
			super.handleMessage(msg);
		}
	};
    //更新Fragment
	public void updateOrder() {
		main();
		myadapter.notifyDataSetChanged();
		mPager.setCurrentItem(0);
	}

	/**
	 * 获取最新订单
	 */
	public void getlastOrder() {
		String url = "http://shop.coolpoint.cc/admin/api/get/?ac=get_orders&uid="
				+ uid
				+ "&token="
				+ token
				+ "&id="
				+ orderdata.list.get(0).getID();
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, url,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String Result = responseInfo.result;
						int start = Result.indexOf("[{");
						int stop = Result.lastIndexOf("}]");
						String jsonstring = Result.substring(start, stop + 2);
						if(jsonstring.length()>4){
							Gson gson = new Gson();
							java.lang.reflect.Type listType = new TypeToken<LinkedList<Orders>>(){}.getType();
							LinkedList<Orders> users = gson.fromJson(jsonstring,listType);
							Orders orders = users.getFirst();
							orderdata.list.add(0, orders);
							myHandler.sendEmptyMessage(1);
						}else{
							return;
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}
				});
	}

	/**
	 * 访问服务器获取Json数据
	 */

	public void getJSONFromServer() {

		String Url = "http://shop.coolpoint.cc/admin/api/get/?ac=get_order";
		String url = Url + "&uid=" + uid + "&token=" + token;
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.GET, url,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						pDialog = new ProgressDialog(MainActivity.this);
						pDialog.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						pDialog.dismiss();
						String Result = responseInfo.result;
						int start = Result.indexOf("[{");
						int stop = Result.lastIndexOf("}]");
						String jsonstring = Result.substring(start, stop + 2);
						System.out.println("jsonstring-->>" + jsonstring);
						DisplayJson(jsonstring);
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}
				});
	}

	/**
	 * JSON解释
	 */

	public void DisplayJson(String jsonstring) {
		orderdata = new OrderData(jsonstring);
		main();
		mPager.setCurrentItem(0);
		timer.schedule(task, 1000*10, 1000 * 30);
	}
	
	public void main(){
		fragments.clear();
		for (int i = 0; i < orderdata.list.size(); i++) {
			Fragment fragment = new OrderFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("data", (Serializable) orderdata.list.get(i));
			fragment.setArguments(bundle);
			fragments.add(fragment);
		}
		myadapter = new OrderAdapter(getSupportFragmentManager(), fragments);
		mPager.setAdapter(myadapter);
	}

	/**
	 * 返回键响应事件
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Builder dialog = new AlertDialog.Builder(this)
					.setMessage("是否退出应用？")
					.setPositiveButton("取消",new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									dialog.cancel();
								}
							})
					.setNegativeButton("确认",new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									System.exit(0);
								}
							});
			dialog.show();
		}
		return false;
	}

}
