package com.yiqi.hj.fileutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;



import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

public class ReadExcel implements Log {
	// 存所有读到的数据
		public List<String> values;
	// 存储要写入的数据
	public static Map<String, Map<Integer, String>> caseResultMap = new HashMap<String, Map<Integer, String>>();
	// 获取有值的所有行
	int rowsNum;
	// 文件名
	String path;
	int sheetNmber;
	int startRow;
	int endRow;
	int startCell;
	int endCell;
	/**
	 * @param path
	 *            文件路径+文件名
	 * @param sheetNmber
	 *            读取页
	 * @param startRow
	 *            其实行
	 * @param endRow
	 *            结束行
	 * @param startCell
	 *            其实列
	 * @param endCell
	 *            结束列
	 * @return 返回Object双维数组
	 */
	public ReadExcel(String path, int sheetNmber, int startRow, int endRow, int startCell, int endCell) {
		String logValue = this.getClass().getName()+":初始化构造方法,内容:文件名:"+path+"页数:"+sheetNmber+"起始行:"+startRow+"结束行:"+endRow+"起始列:"+startCell+"结束列:"+endCell;
		log.info(logValue);
		this.path = path;
		this.sheetNmber = sheetNmber;
		this.startRow = startRow;
		this.endRow = endRow;
		this.startCell = startCell;
		this.endCell = endCell;
		this.rowsNum = endRow - (startRow - 1);
	}

	/**
	 * 根据Excel表和Object对象的特征一致。保存为集合对象格式
	 * @param clazz
	 *            传入对象字节码
	 * @param values
	 *            传入值
	 * @return
	 */
	public List<?> readList(Class<?> clazz) {
		String logValue = clazz.getName() + ":对象初始化,并返回List集合";
		// 存储对象值
		String[] values = new String[(endCell - startCell) + 1];
		// 获取有多少特征
		int classL = clazz.getDeclaredFields().length;
		int valueL = values.length;
		if (valueL > classL) {
			throw new RuntimeException("初始化值(>)构造方法值");
		}
		// 创建list输入，存储对象
		List<Object> lists = new ArrayList<Object>();
		Object object = null;
		try {
			// 获取所有特征
			Field[] fields = clazz.getDeclaredFields();
			// 保存所有特征类型
			Class[] classes = new Class[classL];
			// 获取特征类型
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				classes[i] = field.getType();
			}
			// 创建一个有参数构造方法
			Constructor<Object> operation = (Constructor<Object>) clazz.getDeclaredConstructor(classes);
			Object[][] objects = read();
			for (Object[] object1 : objects) {
				for (int i = 0; i < object1.length; i++) {
					values[i] = (String) object1[i];
				}
				object = operation.newInstance(values);
				lists.add(object);
			}
		} catch (Exception e) {
			log.error(logValue+"[-----失败-----]", e);
			return null;
		}
		log.info(logValue+"[成功]");
		return lists;
	}

	/**
	 * 根据自己指定的(行和列)写入数据到Excel表
	 * 
	 * @param path
	 *            文件名
	 * @param sheetNumber
	 *            写入页
	 * @param rowNum
	 *            写入行
	 * @param cellNum
	 *            写入列
	 * @param result
	 *            写入数据
	 */
	public static void write(String path, int sheetNumber, int rowNum, int cellNum, String result) {
		String logValue = path+":文件正在写入内容:写入页"+sheetNumber+"写入行:"+rowNum+"写入列"+cellNum;
		OutputStream os = null;
		InputStream is = null;
		try {
			is = new FileInputStream(new File(path));
			Workbook workbook = WorkbookFactory.create(is);
			Sheet sheet = workbook.getSheetAt(sheetNumber - 1);
			int lastRowNum = sheet.getLastRowNum();
			for (int i = 0; i <= lastRowNum; i++) {
				Row row = sheet.getRow(i);
				Cell cell = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellType(CellType.STRING);
				String value = cell.getStringCellValue();
				if (value.equals(String.valueOf(rowNum))) {
					Cell cellToBeWirte = row.getCell(cellNum - 1, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cellToBeWirte.setCellType(CellType.STRING);
					cellToBeWirte.setCellValue(result);
				}
			}
			os = new FileOutputStream(new File(path));
			workbook.write(os);
		} catch (Exception e) {
			log.error(logValue+="[-----失败-----]",e);
			return;
		} finally {
			try {
				if (os != null) {
					os.close();
					is.close();
				}
			} catch (IOException e) {
				log.error(path+"文件关闭[-----失败-----]",e);
			}
		}
		log.info(logValue+="[成功]");
	}

	/**
	 * 读取Excel表，保存Object双维数组格式
	 */
	public Object[][] read() {
		values = new ArrayList<String>();
		// 创建Object双维数组保存数据
		Object[][] datas = new Object[(endRow - startRow) + 1][(endCell - startCell) + 1];
		if (".xlsx".equals(path) || ".xls".equals(path)) {
			log.error("该文件" + path + "不是Excel[-----失败-----]");
			// throw new RuntimeException("该文件不是Excel");
		}
		InputStream is = null;
		try {
			Workbook workbook = WorkbookFactory.create(new File(path));
			// 获取页数
			Sheet sheet = workbook.getSheetAt(sheetNmber - 1);
			// 遍历Excel表
			for (int i = startRow; i <= endRow; i++) {
				Row row = sheet.getRow(i - 1);
				for (int j = startCell; j <= endCell; j++) {
					Cell cell = row.getCell(j - 1, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellType(CellType.STRING);
					String value = cell.getStringCellValue();
					values.add(value);
					datas[i - startRow][j - startCell] = value;
				}
			}
		} catch (Exception e) {
			log.error("读取"+this.path+"文件加载[-----失败-----]", e);
			return null;
		}
		log.info("读取"+this.path+"文件加载[成功],行数:"+datas.length);
		return datas;
	}
	/**
	 * 根据caseResultMap向Excel写入数据
	 * 
	 * @param sheetNum
	 *            写入页数
	 * @param path
	 *            写入文件
	 */
	public static void batchWriteCaseResult(int sheetNum, String path) {
		String logValue = path+":文件正在写入内容:写入页"+sheetNum+"数据Number"+caseResultMap.size();
		OutputStream os = null;
		InputStream is = null;
		try {
			is = new FileInputStream(new File(path));
			Workbook workbook = WorkbookFactory.create(is);
			Sheet sheet = workbook.getSheetAt(sheetNum - 1);
			// 拿到用例编号
			Set<String> caseIds = caseResultMap.keySet();
			// 获取所有行
			int tatalRowNum = sheet.getLastRowNum();
			for (String caseId : caseIds) {
				for (int i = 0; i <= tatalRowNum; i++) {
					Row row = sheet.getRow(i);
					Cell cell = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					cell.setCellType(CellType.STRING);
					String firstCellValue = cell.getStringCellValue();
					if (caseId.equals(firstCellValue)) {
						Map<Integer, String> cellValueMap = caseResultMap.get(caseId);
						Set<Integer> cellNums = cellValueMap.keySet();
						for (Integer cellNum : cellNums) {
							Cell cellToBeWrite = row.getCell(cellNum.intValue() - 1,
									MissingCellPolicy.CREATE_NULL_AS_BLANK);
							cellToBeWrite.setCellType(CellType.STRING);
							cellToBeWrite.setCellValue(cellValueMap.get(cellNum));
						}
						break;
					}
				}
			}
			os = new FileOutputStream(new File(path));
			workbook.write(os);
		} catch (Exception e) {
			log.error(logValue+"[-----失败-----]");
			return;
		} finally {
			try {
				if (os != null) {
					os.close();
					is.close();
				}
			} catch (IOException e) {
				log.error(path+"文件关闭[-----失败-----]",e);
			}
		}
		log.info(logValue+"[成功]");
	}

	/**
	 * 读取Excel表，返回List<List<String>> 集合
	 * 
	 * @param sheetNum
	 * @param startRow
	 * @param endRow
	 * @param startCell
	 * @param endCell
	 * @return
	 */
	public List<List<String>> read(int sheetNum) {
		List<List<String>> readString = new ArrayList<List<String>>();
		InputStream is = null;
		Workbook workbook = null;
		try {
			is = new FileInputStream(new File(path));
			workbook = WorkbookFactory.create(is);
		} catch (Exception e) {

		}
		Sheet sheet = workbook.getSheetAt(sheetNum - 1);
		// 获取所有行
		int rowsNum = sheet.getLastRowNum();
		// 获取所有列
		int cellsNum = sheet.getRow(0).getLastCellNum();
		if (startRow <= 0 || startCell <= 0) {
			System.out.println("小与");
		}
		if (endRow - 1 > (rowsNum) || endCell > cellsNum) {
			System.out.println("大于");
		}
		/*
		 * 遍历所有值，并把值存在values集合中
		 */
		for (int i = startRow - 1; i < endRow; i++) {
			// 获取每一行
			Row rows = sheet.getRow(i);
			// 创建集合，存储每一行
			List<String> rowValue = new ArrayList<String>();
			for (int j = startCell - 1; j < endCell; j++) {
				Cell cells = rows.getCell(j, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cells.setCellType(CellType.STRING);
				String cell = cells.getStringCellValue();
				rowValue.add(cell);
			}
			readString.add(rowValue);
		}
		return readString;
	}

	/**
	 * 去别的页拿数据
	 * 
	 * @param yaona
	 *            要拿的页数
	 * @param beina
	 *            被拿的页数
	 * @param yntiaojianrow
	 *            要拿的行
	 * @param yntiaojiancell
	 *            要拿的列
	 * @param bntiaojianrow
	 *            被拿的行
	 * @param bntiaojiancell
	 *            被拿的列
	 * @param shujuhang
	 *            要拿的数据
	 */
	public List<List<String>> readSheet(int yaona, int beina, int yntiaojianrow, int yntiaojiancell, int bntiaojianrow,
			int bntiaojiancell, int shujuhang) {
		FileInputStream in;
		Workbook workbook = null;
		try {
			in = new FileInputStream(new File("src/test/resources/InterfaceTestCase.xlsx"));
			workbook = WorkbookFactory.create(in);
		} catch (Exception e) {
			System.out.println("没拿到文件");
		}
		// 拿到要拿的
		List<List<String>> yaonaList = read(1);
		System.out.println(yaonaList.size());
		// 拿到被拿的
		List<List<String>> beinaList = read(2);
		System.out.println(beinaList.size());
		String string = null;
		// 遍历要拿的所有行
		for (int i = 0; i < yntiaojianrow - 1; i++) {
			String yncell = yaonaList.get(i).get(yntiaojiancell - 1);
			// 遍历被拿的所有行
			for (int j = 0; j < bntiaojianrow - 1; j++) {
				String bncell = beinaList.get(j).get(bntiaojiancell - 1);
				// 比较
				if (yncell.equals(bncell)) {
					string = beinaList.get(j).get(shujuhang - 1);
					break;
				}
			}
			// 把被拿到的 设置到要拿的数组中
			yaonaList.get(i).set(1, string);
		}
		return yaonaList;
	}

	

	/**
	 * 根据Excel表和Object对象的特征一致。读取数据，把数据转成map集合
	 * @param constructor
	 *            构造方法方法对象
	 * @param keyInt
	 *            第几个作为key值
	 * @return 返回map集合
	 */
	public Map<String,?> readMap(Constructor<?> constructor,int keyInt) {
		List<String> valuesData = new ArrayList<String>();
		valuesData.addAll(values);
		// 定义一个map数组，存字符串和对象
		Map<String, Object> map = new HashMap<String, Object>();
		// 遍历值
		int paramenterLenght = constructor.getParameters().length;
		for (int j = 0; j < rowsNum; j++) {
			// 存储值
			String[] value = new String[paramenterLenght];
			// 把值加到数组中
			for (int i = 0; i < paramenterLenght; i++) {
				value[i] = valuesData.get(0);
				valuesData.remove(0);
			}
			try {
				// 给对象初始化
				map.put(value[keyInt - 1], constructor.newInstance(value));
			} catch (Exception e) {
				System.out.println("给构造方法传值失败");
			}
		}
		return map;
	}

}
