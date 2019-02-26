package com.yiqi.hj.utils;

import java.util.Iterator;

import org.testng.Assert;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class Utils {
	/**
	 * 判断返回来的参数是否正确
	 * @param string
	 * @param expectedData
	 * @param isobj
	 */
	public static boolean assertEquals(String requestValue, String expectedData,String certainData) {
		JSONObject expected = JSONObject.fromObject(expectedData);
		JSONObject request = JSONObject.fromObject(requestValue);
		
		System.out.println(Integer.parseInt(request.get("code").toString())+"------------------");
		
		if (Integer.parseInt(expected.get("code").toString()) == 100) {
			if (Integer.parseInt(request.get("code").toString()) != 100) {
				System.out.println(request);
				return false;
			} 
		}
		if (!certainData.equals("")) {
			System.out.println(JSONObject.fromObject(certainData));
			JSONObject certainObj = JSONObject.fromObject(certainData).getJSONObject("obj");
			JSONObject requestObj = request.getJSONObject("obj");
			Iterator<String> keys = certainObj.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				if (certainObj.get(key).toString().trim().equals("![]")) {
					System.out.println(requestObj.getString(key));
					try {
						Assert.assertEquals(requestObj.getString(key), "[]");
						return false;
					} catch (Error e) {
						return true;
					}
				}
				
				//预防数组 深入判断
				Iterator<String> certainKeys = certainObj.keys();
				while (certainKeys.hasNext()) {
					String certainKey = certainKeys.next();
					if (certainObj.get(key).getClass().getName().equals("net.sf.json.JSONArray")) {
						JSONArray requestArray = requestObj.getJSONArray(certainKey);
						JSONArray certainDataArray = certainObj.getJSONArray(certainKey);
						if(certainDataArray.size()>0){
						  for(int j=0;j<certainDataArray.size();j++){
							  JSONObject requestobj = requestArray.getJSONObject(j);
							  JSONObject certainDataobj = certainDataArray.getJSONObject(j);
							  Iterator<String> certainDataobjkeys = certainDataobj.keys();
								while (certainDataobjkeys.hasNext()) {
									String certainDataobjkey = certainDataobjkeys.next();
									try {
										Assert.assertEquals(requestobj.get(certainDataobjkey), certainDataobj.get(certainDataobjkey));
									} catch (Error e) {
										return false;
									}
								}
						  	}
						}
					}else if (certainObj.get(key).getClass().getName().equals("net.sf.json.JSONObject")) {
						JSONObject requestDataObj = requestObj.getJSONObject(certainKey);
						JSONObject certainDataObj = certainObj.getJSONObject(certainKey);
						Iterator<String> certainDataobjkeys = certainDataObj.keys();
						while (certainDataobjkeys.hasNext()) {
							String certainDataobjkey = certainDataobjkeys.next();//拿到rider下面的属性
							try {
								Assert.assertEquals(requestDataObj.get(certainDataobjkey), certainDataObj.get(certainDataobjkey));
							} catch (Error e) {
								return false;
							}
						}
					}
					else {
						
						try {
							Assert.assertEquals(requestObj.get(key), certainObj.get(key));
						} catch (Error e) {
							return false;
						}
					}
				}
				
				
			}
			return true;
		}else {
			try {
				if (expected.get("obj").toString().trim().equals("!null")) {
					String reobj = request.get("obj").toString().trim();
					System.out.println(reobj);
					try {
						Assert.assertEquals(reobj, "null");
						return false;
					} catch (Error e) {
						return true;
					}
				}else if (expected.get("obj").toString().trim().equals("![]")) {
					String reobj = request.get("obj").toString().trim();
					System.out.println(reobj);
					try {
						Assert.assertEquals(reobj, "[]");
						return false;
					} catch (Error e) {
						return true;
					}
				}else {
					try {
						Assert.assertEquals(request.toString(), expected.toString());
						return true;
					} catch (Error e) {
						return false;
					}
				}
			} catch (Exception e) {
				try {
					Assert.assertEquals(request.toString(), expected.toString());
					return true;
				} catch (Error j) {
					return false;
				}
			}
			
		}
	}
}
