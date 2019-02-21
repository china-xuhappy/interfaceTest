package com.yiqi.hj.data;
/**
 * 接口信息类
 * @author Administrator
 * @function
 * 2018/5/22
 */
public class RequestData {
	// 用例编号
	private String caseId;
	// 接口URL对应到ID
	private String apiId;
	// 测试数据
	private String requestData;
	// 预期结果
	private String expectedData;
	// 实际结果
	private String actualresults;
	// sql语句
	private String sql;
	public RequestData(String caseId,String apiId,String requestData,
			String expectedData,String sql,String actualresults) {
		this.caseId =caseId;
		this.apiId = apiId;
		this.requestData = requestData;
		this.expectedData = expectedData;
		this.sql = sql;
		this.actualresults = actualresults;
	}
	
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public String getExpectedData() {
		return expectedData;
	}

	public void setExpectedData(String expectedData) {
		this.expectedData = expectedData;
	}

	public String getActualresults() {
		return actualresults;
	}

	public void setActualresults(String actualresults) {
		this.actualresults = actualresults;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}
