package com.tuibian.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.tuibian.model.CBaseObject;

public class VerificationUtils {

	/** 判断集合是否为空 */

	public boolean lstIsNull(List<CBaseObject> lst) {

		if (lst == null || lst.size() == 0 || lst.isEmpty())

			return true;
		return false;

	}

	/** 得到TextView里面的Text */

	public String getText(TextView textView) {
		String Text = "";

		if (textView == null)
			return Text;
		return textView.getText().toString().trim();

	}

	/** 判断TextView里面是否有字符 */
	public boolean TextViewIsNull(TextView textView) {
		if (textView == null)
			throw new NullPointerException("TextView为空。不能判断啊。");
		if (textView.getText().toString().trim().equals(""))
			return true;
		else
			return false;

	}

	/**
	 * 判断一组TextView是否为空
	 * 
	 * @return 空的返回True。。非空返回false
	 */

	public static boolean isEmpty(TextView... texts) {
		if (texts == null)
			return false;
		for (int i = 0; i < texts.length; i++) {
			boolean empty = TextUtils.isEmpty(texts[i].getText().toString()
					.trim());
			if (empty) {
				return empty;
			}
		}
		return false;
	}

	/**
	 * 判断一组Edittext是否为空
	 * 
	 * @return 空的返回True。。非空返回false
	 */

	public static boolean isEmpty(EditText... texts) {
		if (texts == null)
			return false;
		for (int i = 0; i < texts.length; i++) {
			boolean empty = TextUtils.isEmpty(texts[i].getText().toString()
					.trim());
			if (empty) {
				return empty;
			}
		}
		return false;
	}

	/**
	 * 判断一组String是否为空
	 * 
	 * @return 空的返回false。。非空返回true
	 */

	public static boolean isEmpty(String... texts) {
		if (texts == null)
			return false;
		for (int i = 0; i < texts.length; i++) {
			boolean empty = TextUtils.isEmpty(texts[i]);
			if (empty)
				return false;
		}
		return true;
	}

	/**
	 * 验证手机号码是否正确。
	 * 
	 * @param mobileNumber
	 * @return
	 */
	public static boolean validateMobileNumber(String mobileNumber) {
		if (matchingText("^(13[0-9]|15[0-9]|18[7|8|9|6|5])\\d{4,8}$",
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

	private static boolean matchingText(String expression, String text) {
		Pattern p = Pattern.compile(expression);
		Matcher m = p.matcher(text);
		boolean b = m.matches();
		return b;
	}

}
