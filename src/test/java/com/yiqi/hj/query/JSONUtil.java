package com.yiqi.hj.query;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * Json
 * @author it_ce
 *
 */
public class JSONUtil {
	/**
	 * 解析Json字符串，返回Map格式
	 * @param json
	 * @return
	 *{"sellInfoId":1,"pageNow":1,"pageSize":2}
	 *[ {"key":"file","value":{上传头像文件} {"key":"riderId","value":"9"} ]
	 *[ {"key":"file","value":{上传头像文件} {"key":"riderId","value":"9"} ]
	 */
	public static Map<String,Object> analysisMap(String json){
		/**
		 * 存放读取的内容
		 */
		Map<String,Object> InterfaceFiledata = new HashMap<String, Object>();
		String key="";
		String value = "";
		if (json.indexOf("{") != -1) {
			String[] jsons2 = json.trim().substring(json.indexOf("{")+1,json.indexOf("}")).split(",");
		}
		String[] jsons = json.trim().replace("{","").replace("}","").replace("[","").replace("]","").split(",");
		if (json.indexOf("key") != -1 && json.indexOf("value") != -1) {
			//解析第二种json
			/*存在key,和value
			 */
			for (int i = 0; i < jsons.length; i++) {
				//拿到键值对
				key = jsons[i].split("\\:")[1].replace("\"","");
				i++;
				value  = jsons[i].split("\\:")[1];
				if (value.indexOf("\"") != -1) {
					value = value.replace("\"","").toString();
				}else{
					InterfaceFiledata.put(key,Integer.valueOf(value));
					continue;
				}
				if (value.endsWith(".jpg") || value.endsWith(".png")) {
					InterfaceFiledata.put(key,(File)new File(value));
					continue;
				}
				InterfaceFiledata.put(key,value);
			}
		}else {
			//解析第一种json
			/*
			 * 思路:
			 * 根据逗号，把所有键值对得到
			 * 再根据键值对，根据冒号:拆分，存入Map中
			 */
			for (int i = 0; i < jsons.length; i++) {
				//拿到键值对
				String[] keyANDvalue = jsons[i].split("\\:");
				InterfaceFiledata.put(keyANDvalue[1],keyANDvalue[1]);
			}
		}
		return InterfaceFiledata;
	}
	public static void main(String[] args) {
		Map<String,Object> json = analysisMap("\"key\":\"auditingName\",\"value\":\"张三\","
				+ "\"key\":\"auditingPhone\",\"value\":\"1324121545154\","
				+ "\"key\":\"auditingCardNo\",\"value\":\"51524515454515\","
				+ "{\"key\":\"auditingCardPictureA\",\"value\":\"src/test/resources/test.jpg\","
				+ "\"key\":\"auditingCardPictureB\",\"value\":\"src/test/resources/test.jpg\","
				+ "\"key\":\"auditingYxzzPicture\",\"value\":\"src/test/resources/test.jpg\","
				+ "\"key\":\"auditingWsxkPicture\",\"value\":\"src/test/resources/test.jpg\"}");
		
		Set<String> sets = json.keySet();
		for(String set:sets){
			System.out.println("key:"+set+",value:"+json.get(set));
		}
	}
}
