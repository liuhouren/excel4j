package com.liuhr.excel4j.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.liuhr.excel4j.excel.impl.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.liuhr.excel4j.assist.Optional;
import com.liuhr.excel4j.exceptions.ColumnNameMismatchedException;
import com.liuhr.excel4j.exceptions.InvocationTargetMethodException;
import com.liuhr.excel4j.exceptions.SheetIndexOutOfBoundsException;
import com.liuhr.excel4j.exceptions.ValidationNotThroughException;

public class Test {

	/**
	 * @param excelPath
	 * @param workbook
	 * @throws FileNotFoundException
	 */
	public static void createExcelFile(Workbook workbook, String excelPath,
									   String excelName) throws FileNotFoundException {
		if (workbook instanceof HSSFWorkbook) {
			excelName += ".xls";
		} else if (workbook instanceof XSSFWorkbook) {
			excelName += ".xlsx";
		}
		FileOutputStream outputStream = new FileOutputStream(excelPath + "/"
				+ excelName);
		try {
			workbook.write(outputStream);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void testExport() {
		List<EnvObject> list = new ArrayList<EnvObject>();

		EnvObject envObject1 = new EnvObject();
		envObject1.setName("ss");
		envObject1.setGprsTerminalNum("1234567890");
		envObject1.setMobile("18720990000");
		envObject1.setCreateTime(new Date());
		envObject1.setState("db");
		envObject1.setCode("ee");

		EnvObject envObject2 = new EnvObject();
		envObject2.setName("ks");
		envObject2.setGprsTerminalNum("1234567890");
		envObject2.setMobile("18720990000");
		envObject2.setCreateTime(new Date());
		envObject2.setState("fg");
		envObject2.setCode("ee");

		EnvObject envObject3 = new EnvObject();
		envObject3.setName("ss");
		envObject3.setGprsTerminalNum("1234567890");
		envObject3.setMobile("18720990000");
		envObject3.setCreateTime(new Date());
		envObject3.setState("asd");
		envObject3.setCode("ee");

		EnvObject envObject4 = new EnvObject();
		envObject4.setName("ss");
		envObject4.setGprsTerminalNum("1234567890");
		envObject4.setMobile("18720990000");
		envObject4.setCreateTime(new Date());
		envObject4.setState("grer");
		envObject4.setCode("ee");

		list.add(envObject1);
		list.add(envObject2);
		list.add(envObject3);
		list.add(envObject4);

		AbstractExporter abstractExporter = new DefaultExporter(
				new XSSFWorkbook());

		/*
		 * abstractExporter.setHeaderRowIndex(1);
		 * abstractExporter.export(EnvObject.class, list, "33");
		 */

		/* abstractExporter.doExport(EnvObject.class, list, "22"); */

		abstractExporter.setMaxRowNum(2);

		abstractExporter.setHeaderRowHeight((short) 20);
		abstractExporter.setContentRowHeight((short) 40);

		Optional optional = new Optional();
		optional.add(4, "18720996652", "18720996653");
		optional.add(3, "1234567890", "1234567890");
		Workbook workbook = null;

		try {
			workbook = abstractExporter.doExport(EnvObject.class, list, "11",
					optional);
		} catch (InvocationTargetMethodException e1) {
			// TODO 自动生成 catch 块
			e1.printStackTrace();
		}

		String excelPath = "E:/测试";

		try {
			createExcelFile(workbook, excelPath, "template");
		} catch (FileNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}

	public static void testImport(){
		Optional optional = new Optional();
		optional.add(4, "18720990000", "18720990000");
		optional.add(3, "1234567890", "1234567890");
		String excelPath = "E:/测试/template.xlsx";

		List<EnvObject> _list = null;

		Workbook workbook;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(excelPath));
			AbstractImporter abstractImporter = new DefaultImporter(workbook);

			_list = abstractImporter.doImport(EnvObject.class, "11", optional);
			for (EnvObject envObject : _list) {
				System.out.println(envObject);
			}
		} catch (IOException | ColumnNameMismatchedException
				| ValidationNotThroughException
				| InvocationTargetMethodException
				| SheetIndexOutOfBoundsException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}


	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testExport();
		testImport();
	}

}
