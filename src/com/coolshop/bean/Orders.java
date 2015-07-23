package com.coolshop.bean;

import java.io.Serializable;
import java.util.List;

public class Orders implements Serializable  {

	public int ID;
	public String orderID;
	public String createtime;
	public String status;
	public int sid;
	public String total;
	public List<Info> info;
	public String orderPhone;
	public String orderNote;
	public int orderNum;
	public String isTreated;
	public String isDeleted;
	public int payway;
	public String tname;

	public static class Info implements Serializable{
		public int goods_id;
		public String price;
		public String num;
		public String name;

		public int getGoods_id() {
			return goods_id;
		}

		public void setGoods_id(int goods_id) {
			this.goods_id = goods_id;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public List<Info> getInfo() {
		return info;
	}

	public void setInfo(List<Info> info) {
		this.info = info;
	}

	public String getOrderPhone() {
		return orderPhone;
	}

	public void setOrderPhone(String orderPhone) {
		this.orderPhone = orderPhone;
	}

	public String getOrderNote() {
		return orderNote;
	}

	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getIsTreated() {
		return isTreated;
	}

	public void setIsTreated(String isTreated) {
		this.isTreated = isTreated;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getPayway() {
		return payway;
	}

	public void setPayway(int payway) {
		this.payway = payway;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

}
