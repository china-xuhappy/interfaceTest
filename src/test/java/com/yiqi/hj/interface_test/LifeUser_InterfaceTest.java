package com.yiqi.hj.interface_test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.yiqi.hj.data.Api_info;
import com.yiqi.hj.fileutil.Log;
import com.yiqi.hj.fileutil.ReadExcel;
import com.yiqi.hj.httputil.HttpUtil;
import com.yiqi.hj.httputil.RestConfig;
import com.yiqi.hj.jdbc.JDBCUtil;
import com.yiqi.hj.listener.TestListener;
import com.yiqi.hj.utils.Utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户端接口测试
 * @author it_ce
 *
 */
public class LifeUser_InterfaceTest implements Log{
	// 存放返回结果
	net.sf.json.JSONObject resultJSON = null;
	//true代表不是404可以请求成功
	boolean result = true;

	@Test(dataProvider = "read")
	public void testSimpleInterface(String ExclechinaName,String caseId, String apiId, String requestData,String ExpectedData,String certain,String operation) {
		// 存放写入数据
		Map<Integer, String> cellValueMap = new HashMap<Integer, String>();
		// 获取接口url
		String url = RestConfig.getRestUrlByApi(apiId);
		TestListener.url = url;
		// 获取接口请求类型
		String type = RestConfig.getRestTypeByApi(apiId);
		// 获取接口中文名
		String chinaName = RestConfig.getRestChinaNameByApi(apiId);
		TestListener.chinaName = chinaName;
		// 获取接口中文名
		String httpid = RestConfig.getRestHttpUrlByApi(apiId);
			
		// 准备参数
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 判断预期数据为空
		if ("".equals(requestData) || requestData == null) {
			JSONObject otn = JSONObject.fromObject(operation);
			if (otn.get("status").equals(0)) {
				JSONArray otnes = otn.getJSONArray("set");
				for(int i=0;i<otnes.size();i++) {
					JSONObject setObj = otnes.getJSONObject(i);
					System.out.println(setObj);
					Iterator<String> keys = setObj.keys();
					while (keys.hasNext()) {
						String key = keys.next();
						HttpUtil.testDatas.put(key, setObj.get(key).toString());
					}
				}
			}
			return;
		}
		if (!operation.equals("")) {
			JSONObject otn = JSONObject.fromObject(operation);
			if (otn.get("status").equals(2)||otn.get("status").equals(1)) {
				JSONArray otnes = otn.getJSONArray("get");//获取需要获取的值
				for(int i=0;i<otnes.size();i++) {
					JSONObject getObj = otnes.getJSONObject(i);
					Iterator<String> keys = getObj.keys();
					while (keys.hasNext()) {
						String key = keys.next();
						JSONObject request = JSONObject.fromObject(requestData);
						if (getObj.getString(key).equals("user_id") || getObj.getString(key).equals("voucherid")) {
							request.put(key,Integer.parseInt(HttpUtil.testDatas.get(getObj.getString(key))));
						}else {
							request.put(key,HttpUtil.testDatas.get(getObj.getString(key)));
						}
						requestData = request.toString();
					}
				}
			}
		}
		this.resultJSON = HttpUtil.HttpPostWithJson(url,requestData,httpid);
		
		if (!operation.equals("")) {
			JSONObject otn = JSONObject.fromObject(operation);
			if (otn.get("status").equals(0)||otn.get("status").equals(2)) {
				JSONArray otnes = otn.getJSONArray("set");
				for(int i=0;i<otnes.size();i++) {
					JSONObject setObj = otnes.getJSONObject(i);
					Iterator<String> keys = setObj.keys();
					while (keys.hasNext()) {
						String key = keys.next();
						String setValue = "";
						JSONObject expecte = JSONObject.fromObject(ExpectedData);
						if (this.resultJSON.getString("obj").startsWith("[")) {
							JSONArray requestList = this.resultJSON.getJSONArray("obj");
							setValue = requestList.getJSONObject(0).getString(setObj.getString(key));
							requestList.getJSONObject(0).put(setObj.getString(key), setValue);
							expecte.put("obj",requestList);
						}else {
							System.out.println(this.resultJSON);
							String setkey = setObj.getString(key);
							System.out.println(setkey);
							if (!setkey.startsWith("sql")) {
								if (!this.resultJSON.get("obj").equals("")) {
									JSONObject request = this.resultJSON.getJSONObject("obj");
									try {
										setValue = request.getString(setkey);
									} catch (Exception e) {
										//深入查找 需要的数据
										Iterator<String> requestKeys = request.keys();
										while (requestKeys.hasNext()) {
											String requestKey = requestKeys.next();
											if (request.get(requestKey).getClass().getName().equals("net.sf.json.JSONArray")) {
												JSONArray requestArray = request.getJSONArray(requestKey);
												if(requestArray.size()>0){
												  for(int j=0;j<requestArray.size();j++){
													  JSONObject requestobj = requestArray.getJSONObject(j);
													  setValue = requestobj.getString(setkey);
													  System.out.println(setValue);
													  if (!setValue.equals("")) {
														break;
													  }
												  	}
												}
											}else {
												setValue = request.getJSONObject(requestKey).getString(setkey);
											}
										}
									}
									if (!expecte.get("obj").equals("!null")) {
										expecte.getJSONObject("obj").put(setkey, setValue);
									}
								}
							}else {
								String setsql = setkey.substring(setkey.indexOf(":")+1);
								try {
									ResultSet resultset = JDBCUtil.executeQuery(setsql);
									while (resultset.next()) {
										setValue = resultset.getString("id");
									}
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						}
						ExpectedData = expecte.toString();
						HttpUtil.testDatas.put(key, setValue);
					}
				}
			}
		}
		// 查看接口是否请求成功
		if (resultJSON != null) {
			boolean isEquals = Utils.assertEquals(resultJSON.toString().trim(),ExpectedData.trim(),certain);
			Assert.assertEquals(isEquals, true,"接口返回参数有误");
		} else {// 访问不到
			Assert.assertEquals(1,-1,"请求失败");
		}
	}
	@BeforeClass
	public void load() {
		//读取接口配置
		ReadExcel excel = new ReadExcel("src/test/resources/LifeUser_InterfaceCase.xlsx", 1, 2,39, 1,6);
		RestConfig.api_infos_List = (List<Api_info>) excel.readList(Api_info.class);
	}
	@DataProvider
	public Object[][] read() {
		//读取参数
		ReadExcel excel = new ReadExcel("src/test/resources/LifeUser_InterfaceCase.xlsx", 2,2,55, 1, 7);
		return excel.read();
	}
	
	//执行前，需要把之前测试的数据 删除一下
	@BeforeTest
	public void testLoad() {
		try {
			JDBCUtil.executeUpdate("DELETE FROM tb_shop_order WHERE userAccountId = (SELECT id FROM tb_user_account WHERE userPhone = '17607777776')","COMMIT");
		} catch (SQLException e) {
			System.out.println(e);
		}
		log.info("删除用户完毕");
	}
	//执行后，判断是否有问题，进行邮件发送
	@AfterSuite
	public void writer() {
		log.info("执行完毕");
	}
}
