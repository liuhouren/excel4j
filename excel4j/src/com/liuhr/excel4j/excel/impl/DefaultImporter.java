package com.liuhr.excel4j.excel.impl;

import org.apache.poi.ss.usermodel.Workbook;

public class DefaultImporter extends AbstractImporter {

	public DefaultImporter(Workbook workbook) {
		super(workbook);
	}

	@Override
	protected String validate(Object entity, String propertyName,Class<?>... validateGroups) {
		return "";
	}

}
