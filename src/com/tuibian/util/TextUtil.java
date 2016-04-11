package com.tuibian.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配工具类，包含电话号码，非法字符等匹配
 * 
 * @author MaJun
 * 
 */
public class TextUtil {

	/**
	 * 匹配是否含有非法字符(只含有数字，字母，汉字，下划线)
	 * 
	 * @param text
	 * @return
	 */
	public static boolean regexCheckCommon(String text) {
		boolean flag = false;
		Pattern pattern = Pattern
				.compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$");
		Matcher matcher = pattern.matcher(text);

		if (!matcher.find()) {
			flag = true;
		}

		return flag;
	}

	/**
	 * 检测是否为email地址
	 * 
	 * @param text
	 * @return
	 */
	public static boolean regexCheckEmail(String text) {
		boolean flag = false;
		Pattern pattern = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 检测是否为手机号码
	 * 
	 * @param text
	 * @return
	 */
	public static boolean regexCheckTel(String text) {
		boolean flag = false;
		Pattern pattern = Pattern
				.compile("^1\\d{10}$"/* "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$" */);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			flag = true;
		}
		return flag;
	}

	// ^([1-9]\d*)$

	/**
	 * 检测是否为数字
	 * 
	 * @param text
	 * @return
	 */
	public static boolean regexNum(String text) {
		boolean flag = false;
		Pattern pattern = Pattern.compile("^([0-9]\\d*)$");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 检测密码时否由数字和字母组成
	 * 
	 * @param password
	 * @return
	 */
	public static boolean regexPassword(String password) {
		boolean flag = false;
		Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher matcher = pattern.matcher(password);
		if (matcher.find()) {
			flag = true;
		}
		return flag;
	}
}
