package com.sunilos.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Format Input data.
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 * 
 */

public class Util {

	public static String DATE_FORMAT_IND = "dd/MM/yyyy";
	public static String DATE_FORMAT = "MM/dd/yyyy";
	public static String DATE_FORMAT_MYSQL = "yyyy-MM-dd";

	/**
	 * Parse date from given formatted string
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date getDate(String date, String format) {
		Date convertedDate = null;
		if (date != null)
			try {
				SimpleDateFormat targetFormat = new SimpleDateFormat(format);
				convertedDate = targetFormat.parse(date);
			} catch (ParseException localParseException) {
			}
		return convertedDate;
	}

	/**
	 * Converts Date to string in given format
	 * 
	 * @param indate
	 * @param format
	 * @return
	 */
	public static String getDate(Date indate, String format) {
		String dateString = null;

		SimpleDateFormat sdfr = new SimpleDateFormat(format);
		try {
			dateString = sdfr.format(indate);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return dateString;
	}

	public static Date getDate(String date) {
		return getDate(date, DATE_FORMAT);
	}

	public static String getDate(Date d) {
		return getDate(d, "MM/dd/yyyy");
	}

	public static String convertStringToDate(Date d) {
		return getDate(d, DATE_FORMAT_IND);
	}

	public static Date convertDateFormat(String date) {
		return getDate(date, "dd-MM-yyyy");
	}

}
