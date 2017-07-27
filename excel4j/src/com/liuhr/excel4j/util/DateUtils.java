package com.liuhr.excel4j.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	/**
	 * 
	 */
	private final static SimpleDateFormat DEFAULT_DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * @param date
	 * @return
	 */
	public static String format(Date date){
		return DEFAULT_DATE_FORMAT.format(date);
	}
	
	/**
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date,String pattern){
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static Date parse(String date){
		return parse(date,(Date)null);
	}
	
	/**
	 * @param date
	 * @param defaultDate
	 * @return
	 */
	public static Date parse(String date,Date defaultDate){
		try {
			return DEFAULT_DATE_FORMAT.parse(date);
		} catch (ParseException e) {
			return defaultDate;
		}
	}
	
	/**
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static Date parse(String date,String pattern){
		return parse(date,pattern,null);
	}
	
	/**
	 * @param date
	 * @param pattern
	 * @param defaultDate
	 * @return
	 */
	public static Date parse(String date,String pattern,Date defaultDate){
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			return defaultDate;
		}
	}
	
}
