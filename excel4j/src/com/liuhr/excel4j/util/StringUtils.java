package com.liuhr.excel4j.util;

import java.util.Locale;

public class StringUtils {

	/**
	 * @param args
	 * @return
	 */
	public static String merge(String[] args){
		if(null==args){
			return null;
		}
		StringBuilder sb=new StringBuilder();
		for (String arg : args) {
			sb.append(arg+"\n");
		}
		return sb.toString();
	}
	
	/**
	 * @param arg
	 * @return
	 */
	public static boolean isEmpty(String arg){
		return null==arg||"".equals(arg);
	}
	
	/**
	 * Returns a String which capitalizes the first letter of the string.
	 *  
	 * @param name
	 * @return
	 */
	public static String capitalize(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		return name.substring(0, 1).toUpperCase(Locale.ENGLISH)
				+ name.substring(1);
	}
}
