package com.liuhr.excel4j.exceptions;

public class SheetIndexOutOfBoundsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5701835155846208714L;
	
	/**
	 * 
	 */
	private int sheetIndex;
	
	/**
	 * 
	 */
	private int numbers;

	public SheetIndexOutOfBoundsException(int sheetIndex,int numbers) {
        super(String.format("����ų���(�����:%s, �ܱ���:%s)", sheetIndex,numbers));
        this.sheetIndex=sheetIndex;
        this.numbers=numbers;
    }

	public int getSheetIndex() {
		return sheetIndex;
	}

	public int getNumbers() {
		return numbers;
	}



    
}
