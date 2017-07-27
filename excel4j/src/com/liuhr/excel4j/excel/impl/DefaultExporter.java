package com.liuhr.excel4j.excel.impl;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.liuhr.excel4j.processors.ExcelProcessor;

public class DefaultExporter extends AbstractExporter {

	public DefaultExporter() {
		super(new HSSFWorkbook());
	}
	
	public DefaultExporter(Workbook workbook) {
		super(workbook);
	}

	/* （非 Javadoc）
	 * @see com.liuhr.excel4j.excel.impl.AbstractExporter#createdHeaderCell(org.apache.poi.ss.usermodel.Cell, com.liuhr.excel4j.processors.ExcelProcessor)
	 */
	@Override
	protected void createdHeaderCell(Cell cell, ExcelProcessor excelProcessor) {

	}

	/* （非 Javadoc）
	 * @see com.liuhr.excel4j.excel.impl.AbstractExporter#createdHeaderRow(org.apache.poi.ss.usermodel.Row)
	 */
	@Override
	protected void createdHeaderRow(Row headerRow) {

	}

	/* （非 Javadoc）
	 * @see com.liuhr.excel4j.excel.impl.AbstractExporter#createdContentCell(org.apache.poi.ss.usermodel.Cell, com.liuhr.excel4j.processors.ExcelProcessor)
	 */
	@Override
	protected void createdContentCell(Cell cell, ExcelProcessor excelProcessor) {

	}

	/* （非 Javadoc）
	 * @see com.liuhr.excel4j.excel.impl.AbstractExporter#createdContentRow(org.apache.poi.ss.usermodel.Row)
	 */
	@Override
	protected void createdContentRow(Row contentRow) {
		
	}

	/* （非 Javadoc）
	 * @see com.liuhr.excel4j.excel.impl.AbstractExporter#exportCompletedOf(org.apache.poi.ss.usermodel.Sheet)
	 */
	@Override
	protected void exportCompletedOf(Sheet sheet) {

	}

	/* （非 Javadoc）
	 * @see com.liuhr.excel4j.excel.impl.AbstractExporter#exportCompleted(org.apache.poi.ss.usermodel.Workbook)
	 */
	@Override
	protected void exportCompleted(Workbook workbook) {

	}

}
