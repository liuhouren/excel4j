package com.liuhr.excel4j.excel.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.liuhr.excel4j.assist.Optional;
import com.liuhr.excel4j.excel.Importer;
import com.liuhr.excel4j.exceptions.ColumnNameMismatchedException;
import com.liuhr.excel4j.exceptions.InvocationTargetMethodException;
import com.liuhr.excel4j.exceptions.SheetIndexOutOfBoundsException;
import com.liuhr.excel4j.exceptions.ValidationNotThroughException;
import com.liuhr.excel4j.exceptions.runtime.InstantiationObjectException;
import com.liuhr.excel4j.processors.ExcelProcessor;
import com.liuhr.excel4j.util.ExcelUtils;
import com.liuhr.excel4j.util.StringUtils;

public abstract class AbstractImporter implements Importer {

	// the default headerRowIndex
	private static final int HEADER_ROW_INDEX = 0;

	// headerRowIndex
	private int headerRowIndex = HEADER_ROW_INDEX;

	// workbook
	private final Workbook workbook;

	public AbstractImporter(Workbook workbook){
		this.workbook=workbook;
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


	/* （非 Javadoc）
	 * @see com.liuhr.excel4j.excel.Importer#doImport(java.lang.Class, int, java.lang.String, com.liuhr.excel4j.assist.Optional[])
	 */
	@Override
	public final <T> List<T> doImport(Class<T> entityClass, int sheetIndex, String code, Optional optional,Class<?>...validateGroups) throws SheetIndexOutOfBoundsException, ColumnNameMismatchedException, ValidationNotThroughException, InvocationTargetMethodException{

		//check sheetIndex
		int endIndex=this.workbook.getNumberOfSheets();
		if(sheetIndex>=endIndex){
			throw new SheetIndexOutOfBoundsException(sheetIndex,endIndex);
		}
		// get Excel Annotation、
		List<ExcelProcessor> excelProcessors = ExcelProcessor.getExcelProcessors(entityClass, code);
		//set sheetIndex and endIndex
		if(sheetIndex<0){
			sheetIndex=0;
		}else{
			endIndex=sheetIndex+1;
		}

		//init return value
		List<T> result=new ArrayList<T>();
		//foreach sheet
		while (sheetIndex < endIndex){
			// get sheet;
			Sheet sheet = this.workbook.getSheetAt(sheetIndex);
			// validate header title
			validateHeaderTitle(sheet,excelProcessors);
			// get rows
			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				//get row
				Row rowbody = rows.next();
				//get row index
				int rowIndex = rowbody.getRowNum();
				//detect whether the row is empty
				if (ExcelUtils.isEmptyRow(rowbody)||rowIndex<=this.headerRowIndex) {
					continue;
				}

				//new instance
				T entity = null;
				try {
					entity = entityClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					//throw exception
					throw new InstantiationObjectException(e.getMessage(), e.getCause());
				}
				for (ExcelProcessor excelProcessor : excelProcessors) {
					//get cell
					Cell cell=rowbody.getCell(excelProcessor.columnIndex());

					//get cell value
					Object cellValue=excelProcessor.getCellValue(cell);
					//before setter validate
					String errorMsg=beforeSetterValidate(cellValue,excelProcessor,optional);
					if(!StringUtils.isEmpty(errorMsg)){//error
						//throw exception
						throw new ValidationNotThroughException(errorMsg,sheet.getSheetName(),ExcelUtils.getCellLocation(excelProcessor.columnIndex(),rowIndex));
					}

					//set value
					try {
						excelProcessor.applySetter(entity, cellValue);
					} catch (InvocationTargetException e) {
						// throw exception
						throw new InvocationTargetMethodException(e.getMessage(),e.getCause(),sheet.getSheetName(),ExcelUtils.getCellLocation(excelProcessor.columnIndex(), rowIndex));
					}
					//after setter validate
					errorMsg=afterSetterValidate(entity,excelProcessor,validateGroups);
					if(!StringUtils.isEmpty(errorMsg)){//error
						//throw exception
						throw new ValidationNotThroughException(errorMsg,sheet.getSheetName(),ExcelUtils.getCellLocation(excelProcessor.columnIndex(),rowIndex));
					}

				}
				//
				result.add(entity);
			}
			//
			sheetIndex++;
		}

		return result;
	}

	protected String beforeSetterValidate(Object cellValue, ExcelProcessor excelProcessor, Optional optional) {
		Set<String> opt=optional.getExplicitListValues(excelProcessor.columnIndex());
		if(null==opt){
			return "";
		}
		return opt.contains(cellValue)?"":String.format("出现意外的值(%s)",cellValue);
	}

	protected String afterSetterValidate(Object entity,ExcelProcessor excelProcessor,Class<?>...validateGroups) {
		if(!excelProcessor.ignoreValidate()){
			return this.validate(entity, excelProcessor.propertyName(), validateGroups);
		}
		return "";
	}

	/* （非 Javadoc）
	 * @see com.liuhr.excel4j.excel.Importer#doImport(java.lang.Class, java.lang.String, com.liuhr.excel4j.assist.Optional[])
	 */
	@Override
	public final <T> List<T> doImport(Class<T> entityClass, String code,Optional optional,Class<?>...validateGroups) throws ColumnNameMismatchedException, ValidationNotThroughException, SheetIndexOutOfBoundsException, InvocationTargetMethodException{
		return this.doImport(entityClass, -1, code, optional,validateGroups);
	}

	/**
	 * @param sheet
	 * @param excelProcessors
	 * @throws ColumnNameMismatchedException
	 */
	private void validateHeaderTitle(Sheet sheet,List<ExcelProcessor> excelProcessors) throws ColumnNameMismatchedException{
		Row headerRow=sheet.getRow(this.headerRowIndex);
		for (ExcelProcessor excelProcessor : excelProcessors) {
			Cell cell=headerRow.getCell(excelProcessor.columnIndex());
			String cellValue=cell==null?"":cell.getStringCellValue();
			if(!excelProcessor.columnName().equals(cellValue)){
				//列名cellValue与目标名称columnName不匹配  (表名称:,单元格:A1)
				throw new ColumnNameMismatchedException(sheet.getSheetName(), ExcelUtils.getCellLocation(excelProcessor.columnIndex(),this.headerRowIndex),cellValue,excelProcessor.columnName());
			}
		}
	}

	/**
	 * @param entity
	 * @param propertyName
	 * @param validateGroups
	 * @return
	 */
	protected abstract String validate(Object entity, String propertyName, Class<?>...validateGroups);


}
