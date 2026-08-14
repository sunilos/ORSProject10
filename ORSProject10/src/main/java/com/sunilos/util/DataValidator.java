package com.sunilos.util;

public class DataValidator {

	/**
	 * Check empty string
	 *
	 * @param val
	 * @return
	 */
	public static boolean isEmptyString(String val) {
		return val == null || val.trim().length() == 0;
	}

	/**
	 * Check zero number
	 *
	 * @param val
	 * @return
	 */
	public static boolean isZeroNumber(Double val) {
		return val == null || val == 0;
	}

	/**
	 * Check zero number
	 *
	 * @param val
	 * @return
	 */
	public static boolean isZeroNumber(Long val) {
		return val == null || val == 0;
	}

	/**
	 * Check zero number
	 *
	 * @param val
	 * @return
	 */
	public static boolean isZeroNumber(Integer val) {
		return val == null || val == 0;
	}

	public static boolean isNotNull(Object val) {
		return val != null;
	}

	/**
	 * Check if string is integer
	 *
	 * @param val
	 * @return
	 */
	public static boolean isInteger(String val) {
		if (isEmptyString(val)) {
			return false;
		}
		try {
			Integer.parseInt(val);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// isLong
	public static boolean isLong(String val) {
		if (isEmptyString(val)) {
			return false;
		}
		try {
			Long.parseLong(val);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
