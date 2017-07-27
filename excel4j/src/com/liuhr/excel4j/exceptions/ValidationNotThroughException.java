package com.liuhr.excel4j.exceptions;

public class ValidationNotThroughException extends BaseExcelException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -58520878773146097L;
	

	public ValidationNotThroughException(String message, String sheetName, String location) {
		super(message, sheetName, location);
	}

}
