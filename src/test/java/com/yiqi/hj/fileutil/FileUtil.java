package com.yiqi.hj.fileutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil implements Log{
	public static String filePath = "";
	public static String gainTime(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd--HH-mm");
		return format.format(date);
	}
	/**
	 * 判断目录是否存在，如果不存在则创建一个
	 * @param fileName
	 */
	public static void mkDurIrFuke(String fileName){
		File file = new File(fileName);
		if (!file.exists()) {
			if (fileName.lastIndexOf(".") == -1) {
				//先转为文件夹格式,然后判断文件夹是否存在
				if (!file.exists()) {
					file.mkdirs();
					filePath = fileName;
				}
			}else {
				fileName = fileName.substring(0,fileName.lastIndexOf("."));
				mkDurIrFuke(fileName);
				filePath = fileName;
			}
		}
	}
	public static void main(String[] args) {
		//mkDurIrFuke("C:/Util/apache-tomcat/webapps/InterfaceTest/"+FileUtil.gainTime());
		//FileUtil.copyFile("C:/Users/it_ce/workspace/LifePlus_InterfaceTest/test-output/html","C:/Util/apache-tomcat/webapps/InterfaceTest/"+FileUtil.gainTime());
		FileUtil.readFile("src/test/resources/bless.txt");
	}
	public static void copyFile(String from,String to){
		if (to.indexOf(".") == -1) {
			mkDurIrFuke(to);	
		}
		//C:\Test\商家端\imges\登录提示为空.png
		File fileFrom = new File(from);
	
		File fileTo = new File(to);
		//判断是不是文件夹
		if (fileFrom.isDirectory() && fileTo.isDirectory()) {
			//获取所有文件夹里面的文件
			File[] files = fileFrom.listFiles();
			for(File file : files){
				//获取每个文件名字
				String fileName = file.getName();
				copyFile(from+"/"+fileName,to+"/"+fileName);
			}
			return;
		}
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			is = new FileInputStream(fileFrom);
			os = new FileOutputStream(fileTo);
			int readSize = 0;
			while ((readSize = is.read()) != -1) {
				os.write(readSize);
				os.flush();
			}
		} catch (Exception e) {
			log.error("Copy文件[-----失败-----]",e);
		}finally {
			if (os != null && is != null) {
				try {
					os.close();
					is.close();
				} catch (IOException e) {
					log.error("关闭流[-----失败-----]",e);
				}
			}
		}
	}
	/**
	 * 读文件
	 * @param path 文件路径
	 */
	public static void readFile(String path){
		File file = new File(path);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String readLine = null;
			while ((readLine=reader.readLine()) != null) {
				Thread.sleep(100);
				System.err.println(readLine);
			}
		} catch (Exception e) {
			log.error("文件未找到");
		}
		
	}
}
