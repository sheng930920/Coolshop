package com.example.coolshop;

import java.util.ArrayList;


public class Order {
	
	int ID; //��������
	String orderID;  //������
	String createtime;  //�µ�ʱ��
	String orderPhone;  //��ϵ��
	String orderNote;  //�˿�����
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
