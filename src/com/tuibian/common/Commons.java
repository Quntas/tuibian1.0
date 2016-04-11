package com.tuibian.common;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 */
public class Commons {

	/**
	 * 验证手机号码是否正确。
	 * 
	 * @param mobileNumber
	 * @return
	 */
	public static boolean validateMobileNumber(String mobileNumber) {
		if (matchingText("^(13[0-9]|14[5|7]|15[0-9]|18[0|7|8|9|6|5])\\d{4,8}$",
				mobileNumber)) {
			return true;
		}
		return false;
	}

	/**
	 * 验证Email是否正确。
	 * 
	 * @param email
	 * @return
	 */
	public static boolean validateEmail(String email) {
		if (matchingText("\\w+@(\\w+\\.){1,3}\\w+", email)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 验证中文输入是否正确。
	 * 
	 * @param name
	 * @return
	 */
	public static boolean validateChiness(String name) {
		int z;
		try {
			z = name.getBytes("GBK").length;
			if ((z%2)==0) {
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Log.i("转化出错","");
		}

		return false;
	}

	private static boolean matchingText(String expression, String text) {
		Pattern p = Pattern.compile(expression);
		Matcher m = p.matcher(text);
		boolean b = m.matches();
		return b;
	}

	// 检测String是否全是中文
	public static boolean checkNameChese(String name) {

		boolean res = true;
		char[] cTemp = name.toCharArray();
		for (int i = 0; i < name.length(); i++) {
			if (!isChinese(cTemp[i])) {
				res = false;
				break;
			}
		}
		return res;
	}

	// 判定输入汉字
	public static boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;
		}
		return false;
	}
	
}
