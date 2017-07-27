package com.liuhr.excel4j.processors;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.SheetUtil;

import com.liuhr.excel4j.annotations.Excel;
import com.liuhr.excel4j.annotations.Excel.RepeatableExcel;
import com.liuhr.excel4j.assist.Default;
import com.liuhr.excel4j.enums.CellType;
import com.liuhr.excel4j.exceptions.runtime.ColumnIndexRepeatException;
import com.liuhr.excel4j.exceptions.runtime.MethodNotFoundException;
import com.liuhr.excel4j.util.DateUtils;
import com.liuhr.excel4j.util.StringUtils;

public  class ExcelProcessor{
	
	/**
	 * 
	 */
	private static final String IS_PREFIX = "is";
	
	/**
	 * 
	 */
	private static final String GET_PREFIX = "get";

	/**
	 * 
	 */
	private static final String SET_PREFIX = "set";
	
	/**
	 * 
	 */
	private static final int AUTO_WIDTH_OFFSET=4;
	
	/**
	 * 
	 */
	private static final String DEFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";

	/**
	 * 
	 */
	private Excel excel;
	
	/**
	 * 
	 */
	private Field field;
	
	/**
	 * 
	 */
	private CellType cellType;
	
	/**
	 * 
	 */
	private Method readMethod;
	
	/**
	 * 
	 */
	private Method writeMethod;
	
	private ExcelProcessor(Excel excel,Field field){
		this.excel=excel;
		this.field=field;
	}

	/**
	 * @param entityClass
	 */
	private void init(Class<?> entityClass){
		//init readMethod、writeMethod
		String _capitalize=StringUtils.capitalize(field.getName());		
		Class<?> writeMethodParameterType=excel.writeMethodParameterType();
		if(Default.class==writeMethodParameterType){
			writeMethodParameterType=field.getType();
		}
		String methodName = null;
		try {
			methodName=excel.readMethodName();
			if("".equals(methodName)){
				methodName=(boolean.class==field.getType()? IS_PREFIX:GET_PREFIX)+_capitalize;
			}
			readMethod=entityClass.getMethod(methodName);
			
			methodName=excel.writeMethodName();
			if("".equals(methodName)){
				methodName=SET_PREFIX+_capitalize;
			}
			
			writeMethod=entityClass.getMethod(methodName,writeMethodParameterType);
			
		} catch (NoSuchMethodException | SecurityException e) {
			throw new MethodNotFoundException(String.format("not found this method : '%s(%s)'",methodName,writeMethodParameterType));
		}
		//init cellType
		cellType=this.excel.cellType();
		if(CellType.DEFAULT==cellType){
			cellType=getDefaultCellType(readMethod.getReturnType());
		}
		
	}

	/**
	 * @return
	 */
	public int columnIndex(){
		return this.excel.columnIndex();
	}
	
	/**
	 * @return
	 */
	public String columnName(){
		String name=excel.columnName();
		return "".equals(name)?field.getName():name;
	}
	
	/**
	 * @return
	 */
	public CellType cellType(){
		return this.cellType;
	}
	
	/**
	 * @return
	 */
	public String[] comments(){
		return this.excel.comments();
	}
	
	/**
	 * @return
	 */
	public boolean ignoreValidate(){
		return this.excel.ignoreValidate();
	}
	
	/**
	 * @return
	 */
	public String dataFormat(){
		String dataFormat=this.excel.dataFormat();
		return Date.class.isAssignableFrom(this.field.getType())&&Default.DATAFORMAT.equals(dataFormat)?DEFAULT_DATE_FORMAT:dataFormat;
	}
	
	/**
	 * @param sheet
	 */
	public void applySetColumnWidth(Sheet sheet){
		double width=this.excel.columnWidth();
		if(width<=0){
			width=SheetUtil.getColumnWidth(sheet, this.columnIndex(), false)+AUTO_WIDTH_OFFSET;
		}
		sheet.setColumnWidth(this.columnIndex(), (int)(width*256));
	}
	
	/**
	 * @return 
	 * @return
	 */
	public IndexedColors headerFontColor(){
		return this.excel.headerFontColor();
	}
	
	/**
	 * @return
	 */
	public IndexedColors headerFillForegroundColor(){
		return this.excel.headerFillForegroundColor();
	}
	
	/**
	 * @return
	 */
	public IndexedColors contentFontColor(){
		return this.excel.contentFontColor();
	}
	
	/**
	 * @return
	 */
	public IndexedColors contentFillForegroundColor(){
		return this.excel.contentFillForegroundColor();
	}
	
	/**
	 * @return
	 */
	public String propertyName(){
		return this.field.getName();
	}
	
	/**
	 * @param owner
	 * @return
	 * @throws InvocationTargetException 
	 */
	public Object applyGetter(Object owner) throws InvocationTargetException{
		try {
			return this.readMethod.invoke(owner);
		} catch (IllegalAccessException e) {
			throw new com.liuhr.excel4j.exceptions.runtime.IllegalAccessException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new com.liuhr.excel4j.exceptions.runtime.IllegalArgumentException(e.getMessage(),e.getCause());
		}
	}
	
	/**
	 * @param cell
	 * @param value
	 */
	public void setCellValue(Cell cell,Object value){
		if(null==value){
			return;
		}
		CellType cellType=this.cellType();
		switch (cellType) {
			case NUMERIC:
				if(value instanceof Date){
					cell.setCellValue((Date)value);
				}else{
					cell.setCellValue(Double.parseDouble(value.toString()));
				}
				break;
			case STRING:
				String stringValue;
				if(value instanceof Date){
					stringValue=DateUtils.format((Date)value,this.dataFormat());
				}else{
					stringValue=value.toString();
				}
				cell.setCellValue(stringValue);
				break;
			case FORMULA:
				cell.setCellFormula(value.toString());
				break;
			case BLANK:
				cell.setCellValue("");
				break;
			case BOOLEAN:
				cell.setCellValue(Boolean.parseBoolean(value.toString()));
				break;
			case ERROR:
				cell.setCellErrorValue(Byte.parseByte(value.toString()));
				break;
			default:
				break;
		}		
	}
	
	/**
	 * @param cell
	 * @return
	 */
	public Object getCellValue(Cell cell){
		
		if(cell==null){
			return null;
		}
		
		Object value;
		int cellType=cell.getCellType();
		String parameterType=this.writeMethod.getParameterTypes()[0].getName();
		switch (cellType) {
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					value = cell.getDateCellValue();//Date
				} else {
					double _value = cell.getNumericCellValue();//double
					value=_value;
					switch (parameterType) {
						case "java.lang.String":
							value=String.valueOf(_value);
							break;					
						case "int":
						case "java.lang.Integer":
							value=(int)_value;
							break;
						case "long":
						case "java.lang.Long":
							value=(long)_value;
							break;
						case "float":
						case "java.lang.Float":
							value=(float)_value;
							break;
						case "byte":
						case "java.lang.Byte":
							value=(byte)_value;
							break;
						case "short":
						case "java.lang.Short":
							value=(short)_value;
							break;
						default:
							break;
					}
				}
				break;
			case Cell.CELL_TYPE_STRING:
				value=cell.getStringCellValue();//String
				switch (parameterType) {
					case "java.lang.String":
						break;
					case "java.util.Date":
						value=DateUtils.parse(value.toString(), this.dataFormat());
						break;
					case "boolean":
					case "java.lang.Boolean":
						value=Boolean.parseBoolean(value.toString());
						break;					
					case "int":
					case "java.lang.Integer":
						value=Integer.parseInt(value.toString());
						break;
					case "double":
					case "java.lang.Double":
						value=Double.parseDouble(value.toString());
						break;
					case "long":
					case "java.lang.Long":
						value=Long.parseLong(value.toString());
						break;
					case "float":
					case "java.lang.Float":
						value=Float.parseFloat(value.toString());
						break;
					case "byte":
					case "java.lang.Byte":
						value=Byte.parseByte(value.toString());
						break;
					case "short":
					case "java.lang.Short":
						value=Short.parseShort(value.toString());
						break;
					default:
						break;
				}				
				break;
			case Cell.CELL_TYPE_FORMULA:
				value=cell.getCellFormula();//String
				break;
			case Cell.CELL_TYPE_BLANK:
				value="";//String
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value=cell.getBooleanCellValue();//Boolean
				if("java.lang.String".equals(parameterType)){
					value=value.toString();
				}
				break;
			case Cell.CELL_TYPE_ERROR:
				value=cell.getErrorCellValue();//byte
				if("java.lang.String".equals(parameterType)){
					value=value.toString();
				}
				break;
			default:
				value="";//String
				break;
		}		
		return value;
		
	}
	
	/**
	 * @param owner
	 * @param value
	 * @throws InvocationTargetException 
	 */
	public void applySetter(Object owner,Object value) throws InvocationTargetException{
		try {
			this.writeMethod.invoke(owner, value);
		}catch (IllegalAccessException e) {
			throw new com.liuhr.excel4j.exceptions.runtime.IllegalAccessException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new com.liuhr.excel4j.exceptions.runtime.IllegalArgumentException(e.getMessage(),e.getCause());
		}
	}
	
	/**
	 * @param type
	 * @return
	 */
	private static CellType getDefaultCellType(Class<?> type){
		if(boolean.class==type||Boolean.class.isAssignableFrom(type)){
			return CellType.BOOLEAN;
		}
		if(char.class==type||Character.class.isAssignableFrom(type)){
			return CellType.STRING;
		}
		// Byte、Short、Integer、Long、Float、Double
		if(type.isPrimitive()||Date.class.isAssignableFrom(type)||Number.class.isAssignableFrom(type)){
			return CellType.NUMERIC;
		}
		return CellType.STRING;
	}
	 
	/**
	 * @param entityClass
	 * @param code
	 * @return
	 */
	public final static List<ExcelProcessor> getExcelProcessors(Class<?> entityClass,String code){
		// init return value
		List<ExcelProcessor> excelProcessors=new ArrayList<ExcelProcessor>();
		// all columnIndex. (check columnIndex)
		List<Integer> columnIndexs = new ArrayList<Integer>();
		Class<?> clazz=entityClass;
		while(clazz!=Object.class){
			// foreach all field to get annotation
			Field[] _fields = clazz.getDeclaredFields();
			for (Field field : _fields) {
				//
				RepeatableExcel repeatableExcel = field.getAnnotation(RepeatableExcel.class);
				List<Excel> _list = repeatableExcel == null ? new ArrayList<Excel>(): Arrays.asList(repeatableExcel.value());
				Excel _excel = field.getAnnotation(Excel.class);
				if (_excel != null) {
					_list.add(_excel);
				}
				// foreach _list to get Excel Annotation
				for (Excel excel : _list) {
					if (Arrays.asList(excel.codes()).contains(code)) {
						int key = excel.columnIndex();
						if (columnIndexs.contains(key)) {
							throw new ColumnIndexRepeatException(String.format("the column index is repeat. (index:%s,field:%s)", key,field.getName()));
						}
						// add ExcelProcessor
						ExcelProcessor excelProcessor=new ExcelProcessor(excel, field);
						excelProcessor.init(entityClass);
						excelProcessors.add(excelProcessor);
						columnIndexs.add(key);
					}
				}
			}
			// super class
			clazz=clazz.getSuperclass();
		}		
		return excelProcessors;
	}	
	
	
	
}
