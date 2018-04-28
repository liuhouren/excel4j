package com.liuhr.excel4j.exceptions;

public class ColumnNameMismatchedException extends BaseExcelException {

	/**
	 *
	 */
	private static final long serialVersionUID = -5610228103533667249L;

	/**
	 *
	 */
	private String cellValue;

	/**
	 *
	 */
	private String columnName;

	public ColumnNameMismatchedException(String sheetName, String location, String cellValue, String columnName) {
		super(String.format("列名'%s'与目标名'%s'不匹配 ", cellValue,columnName),sheetName,location);
		this.cellValue = cellValue;
		this.columnName = columnName;
	}

	public String getCellValue() {
		return cellValue;
	}

	public String getColumnName() {
		return columnName;
	}

}
