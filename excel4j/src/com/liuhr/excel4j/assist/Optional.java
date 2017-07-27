package com.liuhr.excel4j.assist;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * @author nc-wl001
 *
 */
public class Optional{
	
	/**
	 * 
	 */
	private Hashtable<Integer, Set<String>> _options=new Hashtable<Integer, Set<String>>();
	
	/**
	 * @param columnIndex
	 * @param explicitListValues
	 */
	public void add(int columnIndex,String...explicitListValues){
		_options.put(columnIndex,  new HashSet<String>(Arrays.asList(explicitListValues)));
	}
	
	/**
	 * @param columnIndex
	 * @param explicitListValues
	 */
	public void add(int columnIndex,Set<String> explicitListValues){
		_options.put(columnIndex, explicitListValues);
	}
	
	/**
	 * @param sheet
	 * @param firstRow
	 * @param lastRow
	 */
	public  void applyValidation(Sheet sheet,int firstRow, int lastRow){
		// reset firstRow,lastRow
		if(firstRow<0){
			firstRow=sheet.getFirstRowNum();
		}
		if(lastRow<0){
			lastRow=sheet.getLastRowNum();
		}
		
		// get DataValidationHelper 
		DataValidationHelper help=sheet.getDataValidationHelper();
		
		Enumeration<Integer> e=this._options.keys();
		//foreach _options to add validationData
		while(e.hasMoreElements()){
			
			int columnIndex=e.nextElement();
			CellRangeAddressList regions = new CellRangeAddressList(firstRow,lastRow, columnIndex, columnIndex);
			
			String[] values=this._options.get(columnIndex).toArray(new String[]{});
			DataValidationConstraint constraint;
			if(sheet instanceof HSSFSheet){
				constraint = DVConstraint.createExplicitListConstraint(values);
			}else if(sheet instanceof XSSFSheet){
				constraint = new XSSFDataValidationConstraint(values);
			}else{
				return;
			}

			sheet.addValidationData(help.createValidation(constraint,regions));		
			
		}
		
	}
	
	/**
	 * @param columnIndex
	 * @return
	 */
	public Set<String> getExplicitListValues(int columnIndex){
		return this._options.get(columnIndex);
	}

}