package com.example.coolshop;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	private EditText tv_name;
	private EditText tv_password;
	private Button btn_login;
	private Myapplication app;
	private static final String PREFS_NAME = "UserInfo";
	// 全局数组
	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		app = (Myapplication) getApplication();
		initview();
	}

	private void initview() {
		tv_name = (EditText) findViewById(R.id.name);
		tv_password = (EditText) findViewById(R.id.password);
		btn_login = (Button) findViewById(R.id.login);
		LoadUserDate();
		
		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new LoginTask().execute("http://shop.coolpoint.cc/admin/api/get/?ac=login");
			}
		});
	}

	/**
	 * 载入已记住的用户名、密码
	 */
	private void LoadUserDate() {
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);

		if (sp.getBoolean("isSave", false)) {
			String username = sp.getString("name", "");
			String userpassword = sp.getString("password", "");
			if (!("".equals(username) && "".equals(userpassword))) {
				tv_name.setText(username);
				tv_password.setText(userpassword);
			}
		}
	}

	/**
	 * 保存用户名、密码
	 */
	private void SaveUserDate() {
		// 载入配置文件
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
		// 写入配置文件
		Editor spEd = sp.edit();
		spEd.putBoolean("isSave", true);
		spEd.putString("name", tv_name.getText().toString());
		spEd.putString("password", tv_password.getText().toString());

		spEd.commit();
	}

	class LoginTask extends AsyncTask<String, Integer, String> {
		
		ProgressDialog proDialog;

		@Override
		protected void onPreExecute() {
			proDialog = new ProgressDialog(Login.this);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			proDialog.show();
			super.onProgressUpdate(values);
		}

		protected String doInBackground(String... params) {

			return getJSONFromServer(params[0]);
		}

		protected void onPostExecute(String result) {
			if (result.equals("success")) {
				SaveUserDate();
				proDialog.dismiss();
				Intent intent = new Intent(Login.this, MainActivity.class);
				startActivity(intent); // 跳转到成功页面
				Login.this.finish();
			}
		}

	}

	public String getJSONFromServer(String url) {

		String ret = "";
		String name = tv_name.getText().toString();
		String temp = tv_password.getText().toString();
		String password = GetMD5Code("coolpoint2015@#$!" + temp);
		String Url = url + "&uname=" + name + "&upassword=" + password;
		//System.out.println("Url-->>" + Url);
		//System.out.println("密码-->>" + password);

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(Url);

		try {
			HttpResponse response = httpClient.execute(httpget);
			int res = response.getStatusLine().getStatusCode();
			if (res == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String Result = EntityUtils.toString(entity, "utf8");
					//System.out.println("Result-->>" + Result);
					JSONObject json = new JSONObject(Result);
					ret = json.getString("ret");
					String status = json.getString("status");
					JSONObject data = json.getJSONObject("data");
					app.setID(data.getInt("ID"));
					app.setToken(data.getString("token"));
					//System.out.println("ID-->>" + data.getInt("ID"));
					//System.out.println("token-->>" + data.getString("token"));
					return ret;
				}
			} else {
				Toast.makeText(getApplication(), "响应不通过！", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	// 返回形式为数字跟字符串
	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		// System.out.println("iRet="+iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	// 返回形式只为数字
	private static String byteToNum(byte bByte) {
		int iRet = bByte;
		System.out.println("iRet1=" + iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		return String.valueOf(iRet);
	}

	// 转换字节数组为16进制字串
	private static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	public static String GetMD5Code(String strObj) {
		String resultString = null;
		try {
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.digest() 该函数返回值为存放哈希值结果的byte数组
			resultString = byteToString(md.digest(strObj.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return resultString;
	}

}
