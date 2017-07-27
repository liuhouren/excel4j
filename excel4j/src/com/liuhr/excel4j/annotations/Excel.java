package com.liuhr.excel4j.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.poi.ss.usermodel.IndexedColors;

import com.liuhr.excel4j.assist.Default;
import com.liuhr.excel4j.enums.CellType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD})
public @interface Excel {

	/**
	 * this parameter use to export and import
	 * 
	 * @return
	 */
	String[] codes();
	
	/**
	 * this parameter use to export and import
	 * 
	 * @return
	 */
	int columnIndex();
	
	/**
	 * this parameter use to export and import
	 * 
	 * @return
	 */
	String columnName() default "";
	
	/**
	 * this parameter use to export and import
	 * 
	 * @return
	 */
	CellType cellType() default CellType.DEFAULT;
	
	/**
	 * this parameter use to export
	 * 
	 * @return
	 */
	short columnWidth() default 0;
	
	/**
	 *  this parameter use to export and import
	 *  
	 * @return
	 */
	String dataFormat() default Default.DATAFORMAT;
	
	/**
	 * this parameter use to export
	 * 
	 * @return
	 */
	String readMethodName() default "";
	
	/**
	 *  this parameter use to import
	 *  
	 * @return
	 */
	String writeMethodName() default "";
	
	/**
	 * this parameter use to import
	 * 
	 * @return
	 */
	Class<?> writeMethodParameterType() default Default.class;
	
	/**
	 * @return
	 */
	boolean ignoreValidate() default false;
	
	/**
	 *  this parameter use to export template
	 *  
	 * @return
	 */
	String[] comments() default {};
	
	/**
	 * this parameter use to export
	 * 
	 * @return
	 */
	IndexedColors headerFontColor() default IndexedColors.RED;
	
	/**
	 * this parameter use to export
	 * 
	 * @return
	 */
	IndexedColors headerFillForegroundColor() default IndexedColors.GREY_25_PERCENT;
	
	/**
	 * this parameter use to export
	 * 
	 * @return
	 */
	IndexedColors contentFontColor() default IndexedColors.BLACK;
	
	/**
	 * this parameter use to export
	 * 
	 * @return
	 */
	IndexedColors contentFillForegroundColor() default IndexedColors.WHITE;

	/**
	 * 
	 * 
	 * @author nc-wl001
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD})
	@interface RepeatableExcel {

		/**
		 * @return
		 */
		Excel[] value();
	}

	
}
