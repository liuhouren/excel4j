package com.liuhr.excel4j.excel.impl;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.liuhr.excel4j.assist.Optional;
import com.liuhr.excel4j.excel.IExporter;
import com.liuhr.excel4j.exceptions.InvocationTargetMethodException;
import com.liuhr.excel4j.processors.ExcelProcessor;
import com.liuhr.excel4j.util.ExcelUtils;

/**
 * abstract exporter
 *
 * @author nc-wl001
 *
 */
public abstract class AbstractExporter implements IExporter {

	// the default maxRowNum
	private static final int MAX_ROW_NUM = 50000;

	// the default headerRowIndex
	private static final int HEADER_ROW_INDEX = 0;

	//
	private static final short HEADER_ROW_HEIGHT=0;

	//
	private static final short CONTENT_ROW_HEIGHT=0;

	//
	private final Workbook workbook;

	//
	private int maxRowNum = MAX_ROW_NUM;

	//
	private int headerRowIndex = HEADER_ROW_INDEX;

	//
	private short headerRowHeight = HEADER_ROW_HEIGHT;

	//
	private short contentRowHeight = CONTENT_ROW_HEIGHT;

	//
	private Hashtable<Short,CellStyle> cellStyles;

	public AbstractExporter(Workbook workbook) {
		// init workbook
		this.workbook = workbook;
	}

	/**
	 * set maxRowNum
	 *
	 * @param maxRowNum
	 */
	public final void setMaxRowNum(int maxRowNum) {
		if (maxRowNum > 0) {
			this.maxRowNum = maxRowNum;
		}
	}

	/**
	 * set headerRow index
	 *
	 * @param headerRowIndex
	 */
	public final void setHeaderRowIndex(int headerRowIndex) {
		if (headerRowIndex >= 0) {
			this.headerRowIndex = headerRowIndex;
		}
	}

	/**
	 * @param headerRowHeight
	 */
	public final void setHeaderRowHeight(short headerRowHeight) {
		if (headerRowHeight >= 0) {
			this.headerRowHeight = headerRowHeight;
		}
	}

	/**
	 * @param contentRowHeight
	 */
	public final void setContentRowHeight(short contentRowHeight) {
		if (contentRowHeight >= 0) {
			this.contentRowHeight = contentRowHeight;
		}
	}

	/**
	 * @return
	 */
	protected int getMaxRowNum(){
		return this.maxRowNum;
	}

	/**
	 * @return
	 */
	protected int getHeaderRowIndex() {
		return this.headerRowIndex;
	}

	/**
	 * @return
	 */
	protected short getHeaderRowHeight() {
		return headerRowHeight;
	}

	/**
	 * @return
	 */
	protected short getContentRowHeight() {
		return contentRowHeight;
	}

	/* （非 Javadoc）
	 * @see com.liuhr.excel4j.Exporter#doExport(java.lang.Class, java.util.List, java.lang.String)
	 */
	@Override
	public final <T> Workbook doExport(Class<T> entityClass,List<T> entities, String code,Optional optional) throws InvocationTargetMethodException{

		// get Excel Annotation、
		List<ExcelProcessor> excelProcessors = ExcelProcessor.getExcelProcessors(entityClass, code);
		//createCellStyles
		this.createCellStyles(excelProcessors);
		//
		if(null==entities){
			entities=new ArrayList<T>();
		}
		//
		if(null==optional){
			optional=new Optional();
		}
		//
		int size = entities.size(), fromIndex = 0;

		do {
			int toIndex = fromIndex + maxRowNum;
			// create sheet;
			Sheet sheet = this.workbook.createSheet();
			// create header row and return sheet
			createHeaderRow(sheet,excelProcessors);
			// create content row
			createContentRow(sheet, excelProcessors,entities.subList(fromIndex, Math.min(size, toIndex)));
			//add Validation
			optional.applyValidation(sheet, headerRowIndex+1, sheet.getLastRowNum());
			//set column width
			for(ExcelProcessor excelProcessor:excelProcessors){
				excelProcessor.applySetColumnWidth(sheet);
			}
			// export completed of this sheet
			exportCompletedOf(sheet);
			fromIndex = toIndex;
		} while (fromIndex < size);

		// export completed
		exportCompleted(this.workbook);

		return this.workbook;

	}

	/**
	 * @param excelProcessors
	 */
	private void createCellStyles(List<ExcelProcessor> excelProcessors){
		this.cellStyles=new Hashtable<Short, CellStyle>();
		for (ExcelProcessor excelProcessor : excelProcessors) {
			Short key=(short) (2*excelProcessor.columnIndex());

			Font headerFont=ExcelUtils.createFont(this.workbook, excelProcessor.headerFontColor());
			CellStyle headerCellStyle=ExcelUtils.createCellStyle(this.workbook, excelProcessor.headerFillForegroundColor());
			this.createdHeaderCellStyle(excelProcessor.columnIndex(),headerCellStyle,headerFont);
			this.cellStyles.put(key, headerCellStyle);

			Font contentFont=ExcelUtils.createFont(this.workbook, excelProcessor.contentFontColor());
			CellStyle contentCellStyle=ExcelUtils.createCellStyle(this.workbook, excelProcessor.contentFillForegroundColor());
			contentCellStyle.setDataFormat(this.workbook.createDataFormat().getFormat(excelProcessor.dataFormat()));
			this.createdContentCellStyle(excelProcessor.columnIndex(),contentCellStyle,contentFont);
			this.cellStyles.put((short) (key+1), contentCellStyle);

		}
	}

	/**
	 * create header row and return sheet
	 *
	 * @param sheet
	 * @param excelProcessors
	 */
	private void createHeaderRow(Sheet sheet,List<ExcelProcessor> excelProcessors) {

		// create header row
		Row headerRow = sheet.createRow(headerRowIndex);
		// foreach all ExcelProcessor
		for (ExcelProcessor excelProcessor : excelProcessors) {
			// create header cell
			createHeaderCell(headerRow,excelProcessor);
		}
		//
		if(this.headerRowHeight>0){
			headerRow.setHeight((short) (this.headerRowHeight*20));
		}
		// created header row
		createdHeaderRow(headerRow);

	}

	/**
	 * createHeaderCell
	 *
	 * @param headerRow
	 * @param excelProcessor
	 */
	private void createHeaderCell(Row headerRow, ExcelProcessor excelProcessor) {
		// create header cell
		Cell cell = headerRow.createCell(excelProcessor.columnIndex());
		// set header cell type
		cell.setCellType(Cell.CELL_TYPE_STRING);
		//set cellStyle
		cell.setCellStyle(this.cellStyles.get((short)(2*excelProcessor.columnIndex())));
		// set header cell value
		cell.setCellValue(excelProcessor.columnName());
		// createdHeaderCell
		createdHeaderCell(cell, excelProcessor);
	}


	/**
	 * @param sheet
	 * @param excelProcessors
	 * @param entities
	 * @throws InvocationTargetMethodException
	 */
	private void createContentRow(Sheet sheet, List<ExcelProcessor> excelProcessors,List<?> entities) throws InvocationTargetMethodException{
		int rowIndex = headerRowIndex + 1;
		// foreach the list to setting content
		for (Object entity : entities) {

			// check null
			if (null == entity) {
				continue;
			}
			// create content row
			Row contentRow = sheet.createRow(rowIndex);
			// foreach all ExcelProcessor
			for (ExcelProcessor excelProcessor : excelProcessors) {
				try {
					createContentCell(contentRow,excelProcessor,entity);
				} catch (InvocationTargetException e) {
					// throw exception
					throw new InvocationTargetMethodException(e.getMessage(),e.getCause(),sheet.getSheetName(),ExcelUtils.getCellLocation(excelProcessor.columnIndex(), rowIndex));
				}
			}
			//
			if(this.contentRowHeight>0){
				contentRow.setHeight((short) (this.contentRowHeight*20));
			}
			// created content row
			createdContentRow(contentRow);

			rowIndex++;
		}

	}

	/**
	 * @param contentRow
	 * @param excelProcessor
	 * @param entity
	 * @throws InvocationTargetException
	 */
	private void createContentCell(Row contentRow, ExcelProcessor excelProcessor,Object entity) throws InvocationTargetException{

		// create content cell
		Cell cell = contentRow.createCell(excelProcessor.columnIndex());
		//setCellType
		cell.setCellType(excelProcessor.cellType().index);
		//set cellStyle
		cell.setCellStyle(this.cellStyles.get((short)(2*excelProcessor.columnIndex()+1)));
		//get readMethod result;
		Object value = excelProcessor.applyGetter(entity);
		// set content cell value
		excelProcessor.setCellValue(cell, value);
		//createdContentCell
		createdContentCell(cell, excelProcessor);

	}

	/**
	 * @param headerCellStyle
	 * @param headerFont
	 */
	protected  void createdHeaderCellStyle(int columnIndex,CellStyle headerCellStyle, Font headerFont){
		headerCellStyle.setFont(headerFont);
	}

	/**
	 * @param contentCellStyle
	 * @param contentFont
	 */
	protected void createdContentCellStyle(int columnIndex,CellStyle contentCellStyle,Font contentFont) {
		contentCellStyle.setFont(contentFont);
	}

	/**
	 * execute this function when this header Cell is created
	 *
	 * @param cell
	 */
	protected abstract void createdHeaderCell(Cell cell,ExcelProcessor excelProcessor);

	/**
	 * execute this function when the createHeaderRow function is executed
	 *
	 * @param headerRow
	 */
	protected abstract void createdHeaderRow(Row headerRow);

	/**
	 * execute this function when this content Cell is created
	 *
	 * @param cell
	 */
	protected abstract void createdContentCell(Cell cell,ExcelProcessor excelProcessor);

	/**
	 * execute this function when the createContentRow function is executed
	 *
	 * @param contentRow
	 */
	protected abstract void createdContentRow(Row contentRow);

	/**
	 * execute this function when this sheet completed export
	 *
	 * @param sheet
	 */
	protected abstract void exportCompletedOf(Sheet sheet);

	/**
	 * execute this function when the export function is executed
	 *
	 * @param workbook
	 */
	protected abstract void exportCompleted(Workbook workbook);

}
