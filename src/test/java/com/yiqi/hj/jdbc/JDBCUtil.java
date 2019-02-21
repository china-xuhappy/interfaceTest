package com.yiqi.hj.jdbc;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.css.sac.InputSource;

import com.gargoylesoftware.htmlunit.javascript.host.file.File;


public class JDBCUtil{
	public static Logger log = Logger.getLogger(JDBCUtil.class);
	private static Properties properties;
	private static Connection connection;
	static {
		try {
			getConnection();
		} catch (Exception e) {
			log.error("执行Connection[失败]");
		}
	}
	/**
	 * 链接数据库
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception{
		// 创建properties对象
		properties = new Properties();
		try {
			// 加载文件
			InputStream in = new BufferedInputStream(new FileInputStream("src/test/resources/CompanyJDBC.properties"));
			properties.load(in);
		} catch (IOException e) {
			log.error("db文件，加载[失败]", e);
		}
		// 获取数据库链接
		//String url = properties.getProperty("jdbc.url");
		// 获取用户名
		//String user = properties.getProperty("jdbc.user");
		// 获取密码
		//String password = properties.getProperty("jdbc.password");
		// 获取driver驱动
		//String driver = properties.getProperty("jdbc.driver");
		try {
			// 加载驱动
			Class.forName("com.mysql.jdbc.Driver");
			//正式服
//			connection = DriverManager.getConnection("jdbc:mysql://rm-wz9r5bwjnfh5pnp24uo.mysql.rds.aliyuncs.com:3306/lifeplus?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true", "root", "aLIYUN2017");
			//测试服
			connection = DriverManager.getConnection("jdbc:mysql://rm-wz9fw4q99ky07rh0p7o.mysql.rds.aliyuncs.com:3306/lifeplus_test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true", "root", "Aliyun2018");
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}finally {
			log.info("properties,db 文件,加载[成功]");
		}
	}

	/**
	 * DDL
	 * 对数据库对象进行操作
	 * @param ddl
	 * @return
	 * @throws SQLException
	 */
	public static boolean execute(String ddl) throws SQLException {
		Statement statement;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			log.error("取statement对象[失败]",e);
			return false;
		}
		try {
			boolean result = statement.execute(ddl);
			return result;
		} catch (SQLException e) {
			log.error("运行DDL语句:"+ddl+"[失败]",e);
			throw new SQLException();
		}
	}
	/**
	 * DML是对表中的数据进行的操作，DML伴随事务操作(TCL)
	 * @param dml 
	 * @param tcl
	 * @return
	 * @throws SQLException
	 *  COMMIT - 保存已完成的工作
	 *	SAVEPOINT - 在事务中设置保存点,可以回滚到此处
	 *	ROLLBACK - 回滚 
	 *	SET TRANSACTION - 改变事务选项
	 */
	public static int executeUpdate(String dml,String tcl) throws SQLException {
		Statement statement;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			log.error("取statement对象[失败]",e);
			return 0;
		}
		try {
			//执行dml
			int lenght = statement.executeUpdate(dml);
			//执行tcl
			statement.executeUpdate(tcl);
			return lenght;
		} catch (SQLException e) {
			log.error("运行DML语句:"+dml+"[失败]"+"TCL语句:"+tcl,e);
			throw new SQLException();
		}
	}
	/**
	 * SQL
	 * @throws SQLException
	 * SELECT 语句:用于查询表中数据 
	 */
	public static ResultSet executeQuery(String sql) throws SQLException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (Exception e) {
			log.error("取statement对象[失败]",e);
		}
		try {
			//执行SQL
			ResultSet result = statement.executeQuery(sql);
			return result;
		} catch (SQLException e) {
			log.error("运行SQL语句:"+sql+"[失败]",e);
			throw new SQLException();
		}
	}
	/**
	 * 关闭数据库
	 */
	public static void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			log.error("关闭数据库[失败]", e);
		}
	}
}
