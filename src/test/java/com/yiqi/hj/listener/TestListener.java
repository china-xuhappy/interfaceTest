package com.yiqi.hj.listener;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import net.sf.json.JSONArray;

public class TestListener extends TestListenerAdapter{
	public static Logger log = Logger.getLogger(TestListener.class);
	public static String url = "";
	public static String chinaName = "";
	public static JSONArray failures = new JSONArray();
	//拦截错误，把有问题接口加入数组中
	public void onTestFailure(ITestResult tr) {
		failures.add("{\"接口地址\":\""+url+"\",\"接口中文名\":\""+chinaName+"\"}");
	}
}
