package com.tuibian.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.graphics.Bitmap;

import com.tuibian.common.CGlobal;

public class ByteUtil {
	/**
	 * 系统提供的数组拷贝方法arraycopy
	 * */
	public static byte[] sysCopy(List<byte[]> srcArrays) {
		int len = 0;
		for (byte[] srcArray : srcArrays) {
			len += srcArray.length;
		}
		byte[] destArray = new byte[len];
		int destLen = 0;
		for (byte[] srcArray : srcArrays) {
			System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
			destLen += srcArray.length;
		}
		return destArray;
	}

	public static byte[] subMsgBytes(byte[] src, int begin) {
		try {
			byte[] bs = new byte[src.length - begin];
			for (int i = begin; i < src.length; i++)
				bs[i - begin] = src[i];
			return bs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return src;

	}

	public static byte[] subBytes(byte[] src, int begin, int count) {
		try {
			byte[] bs = new byte[count];
			for (int i = begin; i < begin + count; i++)
				bs[i - begin] = src[i];
			return bs;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return src;
	}

	public static byte[] string2bytes(String str) {
		char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };

		Map<Character, Integer> rDigits = new HashMap<Character, Integer>(16);

		for (int i = 0; i < digits.length; ++i) {
			rDigits.put(digits[i], i);
		}

		if (null == str) {
			throw new NullPointerException("参数不能为空");
		}
		str = str.replaceAll("-", "");
		if (str.length() != 32) {
			throw new IllegalArgumentException("字符串长度必须是32");
		}
		byte[] data = new byte[16];
		char[] chs = str.toCharArray();
		for (int i = 0; i < 16; ++i) {
			int h = rDigits.get(chs[i * 2]).intValue();
			int l = rDigits.get(chs[i * 2 + 1]).intValue();
			data[i] = (byte) ((h & 0x0F) << 4 | (l & 0x0F));
		}
		return data;
	}

	public static void long2bytes(long value, byte[] bytes, int offset) {
		for (int i = 7; i > -1; i--) {
			bytes[offset++] = (byte) ((value >> 8 * i) & 0xFF);
		}
	}

	public static byte[] getIUUIDByte(UUID uuid) {
		byte[] byUuid = new byte[16];
		long least = uuid.getLeastSignificantBits();
		long most = uuid.getMostSignificantBits();
		long2bytes(most, byUuid, 0);
		long2bytes(least, byUuid, 8);
		return byUuid;
	}

	public static UUID getUUID(byte[] bytes) {
		long most = bytes2long(bytes, 0);
		long least = bytes2long(bytes, 8);
		UUID uuid = new UUID(most, least);
		return uuid;

	}

	protected static long bytes2long(byte[] bytes, int offset) {
		long value = 0;
		for (int i = 7; i > -1; i--) {
			value |= (((long) bytes[offset++]) & 0xFF) << 8 * i;
		}
		return value;
	}

	private static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
}
