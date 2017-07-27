package com.liuhr.excel4j.enums;

public enum CellType {
	
	/**
	 * Cell.CELL_TYPE_NUMERIC
	 */
	NUMERIC(0),
	
	/**
	 * Cell.CELL_TYPE_STRING
	 */
	STRING(1),
	
	/**
	 * Cell.CELL_TYPE_FORMULA
	 */
	FORMULA(2),
	
	/**
	 * Cell.CELL_TYPE_BLANK
	 */
	BLANK(3),
	
	/**
	 * Cell.CELL_TYPE_BOOLEAN
	 */
	BOOLEAN(4),
	
	/**
	 * Cell.CELL_TYPE_ERROR
	 */
	ERROR(5),
	
	/**
	 * the Default
	 */
	DEFAULT(6);
	
	/**
	 * 
	 */
	public final int index;

	/**
	 * @param index
	 */
	private CellType(int index) {
		this.index = index;
	}

	/**
	 * @return
	 */
	public int getIndex() {
		return this.index;
	}
	
}
