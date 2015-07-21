package com.example.coolshop;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 
 *viewpager适配器
 *
 */
public class Myadapter extends PagerAdapter {
	private LayoutInflater inflater;
	private OrderData orderdata;
	private Context mcontext;
	private int uid;
	private String token;
	private Myapplication app;
	
	public Myadapter(Context mcontext,OrderData orderdata){
		this.orderdata = orderdata;
		this.mcontext = mcontext;
		inflater = LayoutInflater.from(mcontext); 
		app = (Myapplication) mcontext.getApplicationContext();		
		uid = app.getID();
		token = app.getToken();
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
		TextView orderPhone = (TextView) mLayout.findViewById(R.id.orderPhone);
		TextView createtime = (TextView) mLayout.findViewById(R.id.createtime);
		TextView orderNote = (TextView) mLayout.findViewById(R.id.orderNote);
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

							new ChangeOrder(mPullScrollView,mcontext, result).execute(Url);

						} else if (PullToRefreshBase.Mode.PULL_FROM_END == mPullScrollView.getCurrentMode()) {
							// 上拉删除订单
							String Url = "http://shop.coolpoint.cc/admin/api/get/?ac=delete_order&uid="
									+ uid + "&token=" + token + "&id=" + iD;
							String result = "订单已删除!";
							
							new ChangeOrder(mPullScrollView,mcontext, result).execute(Url);
						}
					}
				});

		// System.out.println("ID值是--->>"+orderdata.list.get(position).getID());
		String id = String.valueOf(orderdata.list.get(position).getID());
		ID.setText(id);
		orderID.setText(orderdata.list.get(position).getOrderID());
		orderPhone.setText(orderdata.list.get(position).getOrderPhone());
		createtime.setText(orderdata.list.get(position).getCreatetime());
		orderNote.setText(orderdata.list.get(position).getOrderNote());
		total.setText(orderdata.list.get(position).getTotal());
		detail.setAdapter(new ListAdapter(mcontext, orderdata.list.get(position).info));

		((ViewPager) container).addView(mLayout, 0); // 将视图增加到ViewPager
		return mLayout;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
