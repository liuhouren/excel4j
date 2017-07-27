package com.liuhr.excel4j.exceptions;

public class InvocationTargetMethodException extends BaseExcelException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 127151966534998685L;

	public InvocationTargetMethodException(String message, Throwable cause, String sheetName,
			String location) {
		super(message, cause, sheetName, location);
	}



}
