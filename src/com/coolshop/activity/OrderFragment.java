package com.coolshop.activity;

import java.io.Serializable;
import java.util.ArrayList;

import com.coolshop.adapter.ListAdapter;
import com.coolshop.app.Myapplication;
import com.coolshop.bean.Orders;
import com.coolshop.task.ChangeOrder;
import com.coolshop.task.OrderData;
import com.example.coolshop.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class OrderFragment extends Fragment {

	private TextView ID;
	private TextView orderID;
	private TextView orderPhone;
	private TextView createtime;
	private TextView orderNote;
	private ListView detail;
	private TextView total;
	private PullToRefreshScrollView mScrollView;
	private RelativeLayout order_detail;
	
	private Orders orders;
	private Myapplication app;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.detail, container, false);
		ID = (TextView) view.findViewById(R.id.ID);
		orderID = (TextView) view.findViewById(R.id.orderID);
		orderPhone = (TextView) view.findViewById(R.id.orderPhone);
		createtime = (TextView) view.findViewById(R.id.createtime);

		orderNote = (TextView) view.findViewById(R.id.orderNote);
		detail = (ListView) view.findViewById(R.id.detail);
		total = (TextView) view.findViewById(R.id.total);
		mScrollView = (PullToRefreshScrollView) view.findViewById(R.id.mPullScrollView);
		order_detail = (RelativeLayout)view.findViewById(R.id.order_detail);
		orders = (Orders) getArguments().get("data");
		app = (Myapplication) getActivity().getApplication();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ID.setText(String.valueOf(orders.ID));
		orderID.setText(orders.getOrderID());
		orderPhone.setText(orders.getOrderPhone());
		createtime.setText(orders.getCreatetime());
		orderNote.setText(orders.getOrderNote());
		total.setText(orders.getTotal());
		detail.setAdapter(new ListAdapter(getActivity(), orders.info));
		
		if(orders.getIsTreated().equals("n")&&orders.getIsDeleted().equals("y")){
			order_detail.setBackgroundResource(R.drawable.panel_side_gray);
		}
		if(orders.getIsTreated().equals("n")&&orders.getIsDeleted().equals("n")){
			order_detail.setBackgroundResource(R.drawable.panel_side_red);
		}
		
		mScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// 下拉确认订单
				if (PullToRefreshBase.Mode.PULL_FROM_START == mScrollView.getCurrentMode()) {

					String Url = "http://shop.coolpoint.cc/admin/api/get/?ac=set_order&uid="
							+ app.getID()
							+ "&token="
							+ app.getToken()
							+ "&id="
							+ orders.getID()
							+ "&phone="
							+ orders.getOrderPhone();
					String result = "订单已确认!";

					new ChangeOrder(mScrollView, getActivity(), result,order_detail).execute(Url);

				} else if (PullToRefreshBase.Mode.PULL_FROM_END == mScrollView.getCurrentMode()) {
					// 上拉删除订单
					String Url = "http://shop.coolpoint.cc/admin/api/get/?ac=delete_order&uid="
							+ app.getID()
							+ "&token="
							+ app.getToken()
							+ "&id="
							+ orders.getID();
					String result = "订单已删除!";

					new ChangeOrder(mScrollView, getActivity(), result,order_detail).execute(Url);
				}
			}
		});

	}

}
