package com.yiqi.hj.httputil;

import java.util.List;
import com.yiqi.hj.data.Api_info;
import com.yiqi.hj.fileutil.Log;

/**
 * 获取接口信息
 * @author it_ce
 *
 */
public class RestConfig implements Log {
	public static List<Api_info> api_infos_List;
	/**
	 * 根据Id 获取接口URL
	 * @param apiId
	 * @return
	 */
	public static String getRestUrlByApi(String apiId) {
		String url = "";
		String logValue = null;
		try {
			for (Api_info api_info : api_infos_List) {
				if (apiId.equals(api_info.getApiId())) {
					url = api_info.getUrl();
					break;
				}
			}
			logValue = "接口ID:" + apiId + "接口Url:" + url;
		} catch (Exception e) {
			log.error(logValue+"[-----失败-----]");
			return "";
		}
		log.info(logValue+"[成功]");
		return url;
	}

	/**
	 * 根据ID 获取接口请求方式
	 * 
	 * @param apiId
	 * @return
	 */
	public static String getRestTypeByApi(String apiId) {
		String type = null;
		String logValue = null;
		try {
			for (Api_info api_info : api_infos_List) {
				if (apiId.equals(api_info.getApiId())) {
					type = api_info.getMethod();
					break;
				}
			}
			logValue = "接口ID:" + apiId + "接口请求方式:" + type;
		} catch (Exception e) {
			log.error(logValue+"[-----失败-----]");
			return "";
		}
		log.info(logValue+"[成功]");
		return type;
	}

	/**
	 * 根据ID 获取接口中文名
	 * 
	 * @param apiId
	 * @return
	 */
	public static String getRestChinaNameByApi(String apiId) {
		String name = null;
		String logValue = null;
		try {
			for (Api_info api_info : api_infos_List) {
				if (apiId.equals(api_info.getApiId())) {
					name = api_info.getChinaName();
					break;
				}
			}
			logValue = "接口ID:" + apiId + "接口中文名:" + name;
		} catch (Exception e) {
			log.info(logValue+"[-----失败-----]");
			return "";
		}
		log.info(logValue+"[成功]");
		return name;
	}
	/**
	 * 根据ID 获取接口地址
	 * 
	 * @param apiId
	 * @return
	 */
	public static String getRestHttpUrlByApi(String apiId) {
		String name = null;
		String logValue = null;
		try {
			for (Api_info api_info : api_infos_List) {
				if (apiId.equals(api_info.getApiId())) {
					name = api_info.getIsplus();
					break;
				}
			}
		} catch (Exception e) {
			log.info(logValue+"[-----失败-----]");
			return "";
		}
		return name;
	}
}
