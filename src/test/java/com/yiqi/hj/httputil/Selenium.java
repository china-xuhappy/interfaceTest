package com.yiqi.hj.httputil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.yiqi.hj.fileutil.Log;
import com.yiqi.hj.fileutil.ReadExcel;
/**
 * 拉去接口文档网页 数据
 * @author it_ce
 *
 */
public class Selenium implements Log{
	public static void main(String[] args) throws ClassNotFoundException, InterruptedException, FileNotFoundException {
		WebDriver driver = null;
		try {
			System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver.exe");
			driver = new FirefoxDriver();
			driver.get("http://10.102.24.105:4000");
		} catch (Exception e) {
			log.error("链接不到服务器[失败]",e);
		}
		//获取有多少li
		int number = driver.findElements(By.xpath("//ul[@class='summary']/li")).size();
		// 存储接口名字，和接口连接interface
		Map<String,HashMap<String, String>> interfaceValues = new HashMap<String,HashMap<String,String>>();
		List<String> riderChinanames = new ArrayList<String>();
		List<String> businessChinanames = new ArrayList<String>();
		List<String> userChinanames = new ArrayList<String>();
		List<List<String>> Chinames = new ArrayList<List<String>>();
		HashMap<String,String> riderValues = new HashMap<String, String>();
		HashMap<String,String> businessValues = new HashMap<String, String>();
		HashMap<String,String> userValues = new HashMap<String, String>();
		int[] liNumbers = new int[3];
		int liNumber = 0;
		for (int i = 1; i <= number; i++) {
			try {
				// 获取每个ul下有多少li
				liNumber = driver.findElements(By.xpath("//ul[@class='summary']/li[" + i + "]/ul[1]/li")).size();
				liNumbers[i] = liNumber;
			} catch (Exception e) {

			}
			String summary ="";
			if (liNumber != 0) {
				// 循环
				for (int j = 1; j <= liNumber; j++) {
					String nameChina = "";
					String url = "";
					String requestData = "";
					try {
						// 点击每个li
						driver.findElement(By.xpath("//ul[@class='summary']/li[" + i + "]/ul[1]/li[" + j + "]/a")).click();
						//后去每个端
						summary = driver.findElement(By.xpath("//ul[@class='summary']/li[" + i + "]/a")).getText();
					} catch (Exception e) {
						
					}
					try {
						// 获取URL
						url = driver.findElement(By.xpath("//.[@class='search-noresults']/section/ul[1]/li/code"))
								.getText();
					} catch (Exception e) {
						try {
							// 获取URL
							url = driver.findElement(By.xpath("//.[@class='search-noresults']/section/ul[2]/li/code"))
									.getText();
						} catch (Exception e1) {

						}
					}
					//获取参数
					try {
						requestData = driver.findElement(By.xpath("//.[@class='search-noresults']/section/pre[1]/code/")).getText();
						
					} catch (Exception e) {
						
					}
					try {
						// 获取接口名字
						nameChina = driver
								.findElement(By.xpath("//ul[@class='summary']/li[" + i + "]/ul[1]/li[" + j + "]/a"))
								.getText();
					} catch (Exception e) {

					}
					// 判断，把接口名字写入到Map中
					if (!"".equals(url) && url != null && !"".equals(nameChina) && nameChina != null) {
						
						//替换Url
						//String newUrl = "http://118.24.134.96";
						//String replaceUrl = url.replace(url.substring(0,url.indexOf("/h")),newUrl);
						if ("骑手端接口".trim().equals(summary)) {
							riderChinanames.add(nameChina);
							riderValues.put(nameChina, url);
						}else if ("商家端接口".trim().equals(summary)) {
							businessValues.put(nameChina, url);
							businessChinanames.add(nameChina);
						}else if ("用户端接口".trim().equals(summary)) {
							userValues.put(nameChina, url);
							userChinanames.add(nameChina);
						}
						//nameChina,nameChina
						Chinames.add(riderChinanames);
						System.out.println("Name:" + nameChina + "Url:" + url);
					}
				}
				if ("骑手端接口".trim().equals(summary)) {
					interfaceValues.put(summary,riderValues);
				}else if ("商家端接口".trim().equals(summary)) {
					interfaceValues.put(summary,businessValues);
				}else if ("用户端接口".trim().equals(summary)) {
					interfaceValues.put(summary,userValues);
				}
				Chinames.add(riderChinanames);
				Chinames.add(businessChinanames);
				Chinames.add(userChinanames);
			}

		}
		//读取Map数据，并写入到Excel中
		Set<String> interfaceValuesSets = interfaceValues.keySet();
		int index = 0;
		for (String interfaceValuesSet : interfaceValuesSets) {
			/*
			 * List<String> riderChinanames = new ArrayList<String>();
		List<String> businessChinanames = new ArrayList<String>();
		List<String> userChinanames = new ArrayList<String>();
			 */
			System.out.println(interfaceValues.get(interfaceValuesSet).size());
			for (int i = 1; i <= interfaceValues.get(interfaceValuesSet).size(); i++) {
				
				Map<Integer, String> cellValueMap = new HashMap<Integer, String>();
				// Url
				if ("骑手端接口".trim().equals(interfaceValuesSet)){
					System.out.println(riderChinanames.get(index));
					cellValueMap.put(4, interfaceValues.get(interfaceValuesSet).get(riderChinanames.get(index)));
					System.out.println(interfaceValuesSet+",URl:"+interfaceValues.get(interfaceValuesSet).get(riderChinanames.get(index)));
					// 写入中文名
					cellValueMap.put(5, riderChinanames.get(index));
					System.out.println(interfaceValuesSet+",中文名:"+riderChinanames.get(index));
					// 截取URL
					String[] urls =  interfaceValues.get(interfaceValuesSet).get(riderChinanames.get(index)).split("/");
					// 接口名字
					String name = urls[urls.length-1].substring(0, urls[urls.length - 1].lastIndexOf("."));
					// 写入请求方式
					cellValueMap.put(3,"POST");
					// 写入接口名字
					cellValueMap.put(2, name);
					ReadExcel.caseResultMap.put(String.valueOf(i), cellValueMap);
				}else if ("商家端接口".trim().equals(interfaceValuesSet)) {
					cellValueMap.put(4, interfaceValues.get(interfaceValuesSet).get(businessChinanames.get(index)));
					System.out.println(interfaceValuesSet+",URl:"+interfaceValues.get(interfaceValuesSet).get(businessChinanames.get(index)));
					// 写入中文名
					cellValueMap.put(5, businessChinanames.get(index));
					System.out.println(interfaceValuesSet+",中文名:"+businessChinanames.get(index));
					// 截取URL
					String[] urls =  interfaceValues.get(interfaceValuesSet).get(businessChinanames.get(index)).split("/");
					// 接口名字
					String name = urls[urls.length-1].substring(0, urls[urls.length - 1].lastIndexOf("."));
					// 写入请求方式
					cellValueMap.put(3,"POST");
					// 写入接口名字
					cellValueMap.put(2, name);
					ReadExcel.caseResultMap.put(String.valueOf(i), cellValueMap);
				}else if ("用户端接口".trim().equals(interfaceValuesSet)){
					cellValueMap.put(4, interfaceValues.get(interfaceValuesSet).get(userChinanames.get(index)));
					System.out.println(interfaceValuesSet+",URl:"+interfaceValues.get(interfaceValuesSet).get(userChinanames.get(index)));
					// 写入中文名
					cellValueMap.put(5, userChinanames.get(index));
					System.out.println(interfaceValuesSet+",中文名:"+userChinanames.get(index));
					// 截取URL
					String[] urls =  interfaceValues.get(interfaceValuesSet).get(userChinanames.get(index)).split("/");
					// 接口名字
					String name = urls[urls.length-1].substring(0, urls[urls.length - 1].lastIndexOf("."));
					// 写入请求方式
					cellValueMap.put(3,"POST");
					// 写入接口名字
					cellValueMap.put(2, name);
					ReadExcel.caseResultMap.put(String.valueOf(i), cellValueMap);
				}
				index+=1;
			}
			if ("骑手端接口".trim().equals(interfaceValuesSet)) {
				ReadExcel.batchWriteCaseResult(1,"src/test/resources/LifeRider_InterfaceCase.xlsx");
			}else if ("商家端接口".trim().equals(interfaceValuesSet)) {
				ReadExcel.batchWriteCaseResult(1, "src/test/resources/LifeBusiness_InterfaceCase.xlsx");
			}else if ("用户端接口".trim().equals(interfaceValuesSet)){
				ReadExcel.batchWriteCaseResult(1, "src/test/resources/LifeUser_InterfaceCase.xlsx");
			}
			index = 0;
		}

		driver.quit();

	}
}
