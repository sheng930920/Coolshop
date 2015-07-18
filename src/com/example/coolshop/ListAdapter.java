package com.example.coolshop;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.coolshop.Orders.Info;

public class ListAdapter extends BaseAdapter {

	private List<Info> info;
	private Context mContext ;

	public ListAdapter(Context mContext,List<Info> info) {
		this.mContext = mContext;
		this.info = info;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return info.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return info.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.style, null);
            
            viewHolder.id = (TextView) convertView.findViewById(R.id.id);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.num = (TextView) convertView.findViewById(R.id.num);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);

		}else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
		String Id = String.valueOf(arg0+1);
		viewHolder.id.setText(Id);
		viewHolder.name.setText(info.get(arg0).getName());
		viewHolder.num.setText(info.get(arg0).getNum());
		viewHolder.price.setText(info.get(arg0).getPrice());
		
		return convertView;
	}

	private static class ViewHolder {

		TextView id;
		TextView name;
		TextView num;
		TextView price;
		
	}

}
