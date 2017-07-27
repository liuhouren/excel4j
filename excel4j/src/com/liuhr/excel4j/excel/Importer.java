package com.liuhr.excel4j.excel;

import java.util.List;

import com.liuhr.excel4j.assist.Optional;
import com.liuhr.excel4j.exceptions.ColumnNameMismatchedException;
import com.liuhr.excel4j.exceptions.InvocationTargetMethodException;
import com.liuhr.excel4j.exceptions.SheetIndexOutOfBoundsException;
import com.liuhr.excel4j.exceptions.ValidationNotThroughException;

public interface Importer {
	
	/**
	 * @param entityClass
	 * @param sheetIndex
	 * @param code
	 * @param optional
	 * @param validateGroups
	 * @return
	 * @throws SheetIndexOutOfBoundsException
	 * @throws ColumnNameMismatchedException
	 * @throws ValidationNotThroughException
	 * @throws InvocationTargetMethodException 
	 */
	<T> List<T> doImport(Class<T> entityClass,int sheetIndex,String code,Optional optional,Class<?>...validateGroups) throws SheetIndexOutOfBoundsException, ColumnNameMismatchedException, ValidationNotThroughException, InvocationTargetMethodException;

	/**
	 * @param entityClass
	 * @param code
	 * @param optional
	 * @param validateGroups
	 * @return
	 * @throws ColumnNameMismatchedException
	 * @throws ValidationNotThroughException
	 * @throws SheetIndexOutOfBoundsException
	 * @throws InvocationTargetMethodException 
	 */
	<T> List<T> doImport(Class<T> entityClass,String code,Optional optional,Class<?>...validateGroups) throws ColumnNameMismatchedException, ValidationNotThroughException, SheetIndexOutOfBoundsException, InvocationTargetMethodException;

}
