package com.example.coolshop;

import android.app.Application;

public class Myapplication extends Application {

	int ID;
	String token;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
