package com.yiqi.hj.httputil;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.yiqi.hj.fileutil.Log;
import com.yiqi.hj.query.JSONUtil;
import net.sf.json.JSONObject;

public class HttpUtil implements Log {
	public static HashMap<String, String> testDatas = new HashMap<String,String>();//存储测试中需要用到的动态数据
	private static HttpPost post;
	private static HttpGet get;
//	private static final String HTTPURL = "http://yghn.yangguanghaina.com";

	private static final String HTTPURL = "http://ceshi.yangguanghaina.com";
	private static final String DJJHTTPURL = "http://39.108.146.220";
//	private static final String HTTPURL = "http://fhgl.yangguanghaina.com";
//	private static final String HTTPURL = "http://lifeplus.yangguanghaina.com";
	
//	private static final String HTTPURL = "http://47.112.30.250";
//	private static final String DJJHTTPURL = "http://47.107.56.127";
	//存放不同接口，例如项目中用到 多个域名。请按照Excel用例的isUrl(指定服务器url)
	private static final String[] https = new String[]{HTTPURL,DJJHTTPURL};
	
	/**
	 * post请求
	 * 
	 * @param uri      
	 * @param params from-data表单形式提交
	 * @return 返回请求接口
	 */
	public static String getRequestStringByPost(String uri, List<NameValuePair> params) {
		post = new HttpPost(uri);
		String request = null;
		try {
			// 把值设置到post请求中
			post.setEntity(new UrlEncodedFormEntity(params));
			// 创建客户端
			CloseableHttpClient client = HttpClients.createDefault();
			// 发送请求
			CloseableHttpResponse ponse = client.execute(post);
			// 获取请求数据
			request = EntityUtils.toString(ponse.getEntity());
		} catch (Exception e) {
			log.error("post请求[失败]");
		}
		return request;
	}
	
	/**
	 * get 请求
	 * 
	 * @param uri
	 * @param params from-data表单形式提交
	 * @return 返回请求接口
	 */
	public static String getRequestStringByGet(String uri, List<NameValuePair> params) {
		String request = null;
		if (params != null) {
			for (int i = 0; i < params.size(); i++) {
				if (i == 0) {
					uri += "?" + params.get(i);
				} else {
					uri += "&" + params.get(i);
				}
			}
		}
		get = new HttpGet(uri);
		try {
			// 创建客户端
			CloseableHttpClient client = HttpClients.createDefault();
			// 发送请求
			CloseableHttpResponse response = client.execute(get);
			// 获取值
			request = EntityUtils.toString(response.getEntity());
			System.out.println("状态码"+response.getStatusLine().getStatusCode());
		} catch (Exception e) {
			log.error("get请求[失败]");
		}
		return request;
	}

	public static String process(String type, String url, List<NameValuePair> params) {
		if ("post".equalsIgnoreCase(type)) {
			return HttpUtil.getRequestStringByPost(url, params);
		} else if ("get".equalsIgnoreCase(type)) {
			return HttpUtil.getRequestStringByGet(url, params);
		}
		return "";
	}
	/**
     * 生成32位md5码
     * @param password
     * @return
     */
    public static String md5Password(String value) {
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(value.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
	/**
	 * 传入json参数POST请求提交
	 * @param url
	 *            接口地址
	 * @param json
	 *            提交参数
	 * @param httpid 判断是否是需要加token等验证信息
	 * @return
	 * 返回请求结果
	 */
	public static JSONObject HttpPostWithJson(String url,String parmesStr,String httpid) {
		long timestamp = new Date().getTime();
		String logValue;
		JSONObject json;
		//判断是生活plus 后台，还是代金券后台
		if (httpid.equals("0")) {
			//骑手，用户，商家都需要token，需要区分
			HashMap<String, String> parmes = new HashMap<String,String>();
			parmes.put("version", "3.0");
			parmes.put("signatureMethod", "md5");
			parmes.put("format", "json");
			parmes.put("params", parmesStr);
			parmes.put("appKey", "10990665afc94531a3121a0b374d4e78");
			parmes.put("timestamp", String.valueOf(timestamp));
			parmes.put("token", testDatas.get("user_token"));
			String paramsMd5 = parmesStr + "appKey=10990665afc94531a3121a0b374d4e78format=jsontimestamp=" + timestamp + "version=3.0signatureMethod=md589bc8adea10a4bbc8730782305a9adff";
			String signature = md5Password(paramsMd5);
			parmes.put("signature", signature);
			json = JSONObject.fromObject(parmes);
			logValue = "Url:"+https[Integer.parseInt(httpid)]+url+",参数:"+json;
		}else {
			System.out.println(parmesStr);
			json = JSONObject.fromObject(parmesStr);
			logValue = "Url:"+url+",参数:"+json;
		}
		// 返回结果
		String returnValue = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = null;
			if (httpid.equals("2")) {
				httpPost = new HttpPost(https[0]+url);
			}else {
				httpPost = new HttpPost(https[Integer.parseInt(httpid)]+url);
			}
			StringEntity requestEntity = new StringEntity(json.toString(),"utf-8");
			requestEntity.setContentEncoding("UTF-8");
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setEntity(requestEntity);
			returnValue = httpClient.execute(httpPost,responseHandler);
		} catch (Exception e) {
			log.error(logValue+"请求[-----失败-----]",e);
			return null;
		}
		finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		log.info(logValue+"请求[成功],返回值"+returnValue);
		// 处理返回值
		return JSONObject.fromObject(returnValue);
	}
	/**
	 * 上传文件接口keyAndvalue
	 * @param json
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String HttpPostWithJsonFile(String url,String json){
		CloseableHttpResponse response = null;
		String returnValue = "";
		String logValue = "Url:"+url+",参数:"+json;
		try{
		//创建HttpPost对象，用于包含信息发送post消息
		HttpPost httpPost = new HttpPost(url);
		//获取所有key和value
		Map<String,Object> InterfaceFiledata = JSONUtil.analysisMap(json);
		Set<String> keys = InterfaceFiledata.keySet();
		//存放String 类型数值
		StringBody name = null;
		//存放文件类型数值
		FileBody imageFileBody = null;
		//将所有需要上传元素打包成HttpEntity对象
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		for(String key : keys){
			String value =InterfaceFiledata.get(key).toString();
			if (value.endsWith(".jpg") || value.endsWith(".png")) {
				//获取需要上传的文件
				File imageFile = new File(value);
				imageFileBody = new FileBody(imageFile);
				//装载经过base64编码的图片的数据
				String imageBase64Data = "vnweovinsldkjosdfgvndlgkdfgjsdfdfg";
				ByteArrayBody byteArrayBody = null;
				if(StringUtils.isNotEmpty(imageBase64Data)){
					byte[] byteImage = Base64.decodeBase64(imageBase64Data);
					byteArrayBody = new ByteArrayBody(byteImage,"image_name");
				}
				builder.addPart(key,imageFileBody);
			}else {
				//装载上传字符串的对象
				//name = new StringBody(value,ContentType.TEXT_PLAIN);
				//将所有需要上传元素打包成HttpEntity对象
				builder.addTextBody(key,value,ContentType.TEXT_PLAIN.withCharset("UTF-8"));
			}
		}
			HttpEntity aEntity = (HttpEntity)builder.build();
			httpPost.setEntity(aEntity);
			//创建HttpClient对象，传入httpPost执行发送网络请求的动作
			CloseableHttpClient httpClient = HttpClients.createDefault();
			response = httpClient.execute(httpPost);
			//获取返回的实体内容对象并解析内容
			returnValue = EntityUtils.toString(response.getEntity());
		}catch(Exception e){
			log.error(logValue+"请求[-----失败-----]",e);
			return "";
		}finally{
			if (null != response){
                try {
					response.close();
				} catch (IOException e) {
					
				}
            }
		}
		log.info(logValue+"请求[成功],返回值"+returnValue);
		return returnValue;
	}
	//public static void main(String[] args) {
		/*
		 * HttpPostWithJson("http://10.102.24.122:8080/lifeplus/riderlogin/rider_login.shtml",
				"{\"riderPhone\":\"17601245834\",\"riderPassWord\":\"123456\"}");
		 */
		//String resultString = HttpPostWithJsonList("http://10.102.24.122:8080/lifeplus/riderlogin/to_upload.shtml");
		//System.out.println(resultString);
		//Map<String, Object> map = (Map<String, Object>) JSONObject.parse(resultString);
		//int info = (Integer) map.get("info");
		//Assert.assertEquals(info,100,"服务器请求超时");
//		 List<NameValuePair> params = new ArrayList<NameValuePair>();
//		 params.add(new
//		 BasicNameValuePair("riderPhone","17601245834"));
//		 params.add(new BasicNameValuePair("riderPassWord","123456"));
//		 String requestData =
//		 HttpUtil.getRequestStringByGet("http://10.102.24.122:8080/lifeplus/riderlogin/rider_login.shtml",params);
//		 System.out.println(requestData);
	//}
}
