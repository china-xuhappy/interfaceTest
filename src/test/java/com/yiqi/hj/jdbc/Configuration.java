package com.yiqi.hj.jdbc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;



/**
 * 插入数据库 配置信息
 * 
 * @author: Happy
 * @date: 2018年6月27日 下午4:35:44
 * 31.2077788081,121.5942846402
 */
public class Configuration{
	public static Logger log = Logger.getLogger(Configuration.class);
	static Properties properties;
	static{
		properties = new Properties();
		try {
			InputStream is = new BufferedInputStream(Configuration.class.getResourceAsStream("/Configuration.properties"));
            BufferedReader bf = new BufferedReader(new  InputStreamReader(is,"UTF-8"));
			// 加载文件
			properties.load(bf);
		} catch (IOException e) {
			log.error("db文件，加载[失败]", e);
		}
	}
	/**
	 * 根据条件，增加数据
	 * @param name 表名
	 * @param values 字段AND内容
	 */
	public static void addAddress(String name,String value){
		String[] values = value.split(",");
		//字段
		String field = "";
		//内容
		String price = "";
		/**
		 * 获取配置信息
		 * 字段名，值
		 */
		int i = 0;
		for(int index = 0;index < values.length/2;index++){
			if (values.length/2 == index+1) {
				field+=values[index+i];
				price+=values[index+i+1];
			}else {
				field+=values[index+i]+",";
				price+=values[index+i+1]+",";
			}
			System.out.println(field);
			System.out.println(price);
			i+=1;
		}
		String dml = "INSERT INTO "+name+"("+field+")"
				+ "VALUES("+price+") ";
		System.out.println(dml);
		try {
			JDBCUtil.executeUpdate(dml,"COMMIT");
		} catch (SQLException e) {
			log.error("添加失败",e);
		}
	}
	/**
	 * 根据条件，删除对应的数据
	 * @param name 表名
	 * @param values 条件
	 */
	public static void delAddress(String name,String value){
		String dml = "DELETE FROM "+name+" "+value+"";
		System.out.println(dml);
		try {
			JDBCUtil.executeUpdate(dml,"COMMIT");
		} catch (SQLException e) {
			log.error("删除[失败],{条件:"+value+",表:"+name+"}");
		}
	}
	/**
	 * 根据条件，查询数据
	 * @param name 表名
	 * @param values 条件
	 */
	public static int sqlAddress(String name,String value){
		String sql = "SELECT * FROM "+name+" "+value+"";
		System.out.println(sql);
		try {
			ResultSet data = JDBCUtil.executeQuery(sql);
			while (data.next()) {
				return data.getRow();
			}
		} catch (SQLException e) {
			log.error("查询[失败],{条件:"+value+",表:"+name+"}");
		}
		return 0;
	}
}










