package com.example.coolshop;

public class OrderInfo {
	int price;  //���
	int num;  //����
	String name;  //Ʒ��

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public OrderInfo(String name, int price, int num) {
		this.name = name;
		this.price = price;
		this.num = num;
	}

}
