package com.liuhr.excel4j.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.liuhr.excel4j.assist.Optional;
import com.liuhr.excel4j.exceptions.InvocationTargetMethodException;

/**
 * @author nc-wl001
 *
 */
public interface IExporter {
	
	/**
	 * @param entityClass
	 * @param entities
	 * @param code
	 * @param optional
	 * @return
	 * @throws InvocationTargetMethodException 
	 */
	<T> Workbook doExport(Class<T> entityClass, List<T> entities, String code, Optional optional) throws InvocationTargetMethodException;

}
