package com.liuhr.excel4j.exceptions;

public class BaseExcelException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2097081585625290190L;

	/**
	 * 
	 */
	private String sheetName;
	
	/**
	 * 
	 */
	private String location;
	
	public BaseExcelException(String message, String sheetName, String location) {
		super(String.format("%s :  (������:%s,��Ԫ��:%s)", message, sheetName, location));
		this.sheetName = sheetName;
		this.location = location;
	}

	public BaseExcelException(String message, Throwable cause, String sheetName, String location) {
		super(message,cause);
		this.sheetName = sheetName;
		this.location = location;
	}

	public String getSheetName() {
		return sheetName;
	}

	public String getLocation() {
		return location;
	}
	
}
