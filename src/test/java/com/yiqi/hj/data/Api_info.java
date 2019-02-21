package com.yiqi.hj.data;

/**
 * 保存接口信息
 * @author Administrator
 * @function 2018年5月22日
 */
public class Api_info {
	// 接口ID
	String apiId;
	// 接口名字
	String apiName;
	// 接口请求方式
	String method;
	// 接口url
	String url;
	// 接口中文名
	String chinaName;
	// 判断接口地址 0.生活plus，1.代金券后台
	String isplus;
	/**
	 * 构造方法
	 * @return
	 */
	public Api_info(String apiId,String apiName,String method,String url,String chinaName,String isplus) {
		this.apiId = apiId;
		this.apiName = apiName;
		this.url = url;
		this.method = method;
		this.chinaName = chinaName;
		this.isplus = isplus;
	}
	public Api_info() {
		
	}
	@Override
	public String toString() {
		return this.apiId + ",method:"+this.method+",url:"+this.url+",name:"+this.apiName;
	}
	public String getApiId() {
		return apiId;
	}
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getChinaName() {
		return chinaName;
	}
	public void setChinaName(String chinaName) {
		this.chinaName = chinaName;
	}
	public String getIsplus() {
		return isplus;
	}
	public void setIsplus(String isplus) {
		this.isplus = isplus;
	}
}
