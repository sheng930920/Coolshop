package com.example.coolshop;

import java.util.ArrayList;


public class Order {
	
	int ID; //外卖单号
	String orderID;  //订单号
	String createtime;  //下单时间
	String orderPhone;  //联系人
	String orderNote;  //顾客留言
	int orderNum;  
	Double total;
	public ArrayList<OrderInfo> orderinfo;
	
	
	public Order(int iD, String orderID, String createtime, String orderPhone,
			String orderNote, int orderNum, Double total,ArrayList<OrderInfo> orderinfo) {
		super();
		ID = iD;
		this.orderID = orderID;
		this.createtime = createtime;
		this.orderPhone = orderPhone;
		this.orderNote = orderNote;
		this.orderNum = orderNum;
		this.total = total;
		this.orderinfo = orderinfo;
	}
	
	

	


}
