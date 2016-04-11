package com.tuibian.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtils {

	public static String getTime(String mTime) {
		if (mTime == null || mTime.equals("")) {
			return null;
		}

		try {
			switch (isYeaterday(new java.sql.Date(Long.valueOf(mTime)))) {
			case -1:// 今天返回时:分

				String currtData = new SimpleDateFormat("HH:mm")
						.format(new java.sql.Date(Long.valueOf(mTime)));
				return currtData;

			case 0:// 昨天 返回 昨天

				return "昨天";

			case 1:// 至少是前天
				String qiantian = new SimpleDateFormat("MM-dd")
						.format(new java.sql.Date(Long.valueOf(mTime)));
				return qiantian;

			}
		} catch (ParseException e) {

			e.printStackTrace();
		}

		return null;
	};

	/**
	 * @author
	 * @param oldTime
	 *            较小的时间
	 * @param newTime
	 *            较大的时间 (如果为空 默认当前时间 ,表示和当前时间相比)
	 * @return -1 ：同一天. 0：昨天 . 1 ：至少是前天.
	 * @throws ParseException
	 *             转换异常
	 */
	private static int isYeaterday(Date oldTime) throws ParseException {
		// 将下面的 理解成 yyyy-MM-dd 00：00：00 更好理解点
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = format.format(new Date());
		Date today = format.parse(todayStr);
		// 昨天 86400000=24*60*60*1000 一天
		if ((today.getTime() - oldTime.getTime()) > 0
				&& (today.getTime() - oldTime.getTime()) <= 86400000) {
			return 0;
		} else if ((today.getTime() - oldTime.getTime()) <= 0) { // 至少是今天
			return -1;
		} else { // 至少是前天
			return 1;
		}

	}

	/**
	 * @author 判断两个时间差是否大于零。
	 * @param front
	 *            前面的时间
	 * 
	 * @param behind
	 *            后面的时间
	 * 
	 * @return true 是大于一分钟 false反之。
	 * 
	 */

	public static boolean IsShowTime(String front, String behind) {
		java.sql.Date mFrontDate = new java.sql.Date(Long.valueOf(front));
		java.sql.Date mBehindDate = new java.sql.Date(Long.valueOf(behind));

		// Long time = (mBehindDate.getTime() - mFrontDate.getTime());

		if ((mBehindDate.getTime() - mFrontDate.getTime()) > 0
				&& (mBehindDate.getTime() - mFrontDate.getTime()) >= 60 * 1000) {// 一分钟
			return true;
		} else {
			return false;
		}

	}

	/**
	 * @author 时间格式化 月-日 时-分
	 * @param Time
	 *            long时间
	 * 
	 * 
	 * @return 返回格式化好的时间字符串
	 * 
	 */

	public static String getFormatTime(Date Time) {

		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm ");
		String todayStr = format.format(Time);

		return todayStr;

	}

	/**
	 * 获取当前时间
	 * 
	 */

	public static String getCurrentDate() {
		Date data = new Date(System.currentTimeMillis());

		return String.valueOf(data.getTime());

	}

	/**
	 * 比较两个日期之间的大小
	 * 
	 * @param d1
	 * @param d2
	 * @return 前者大于后者返回true 反之false
	 */
	public static boolean compareDate(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);

		int result = c1.compareTo(c2);
		if (result >= 0)
			return true;
		else
			return false;
	}

	/**
	 * 从现在开始获取过去周的开始时间：比如 weeks weeks 获取过去多少周的开始时期与结束日期 数据是前闭后开
	 */
	public static List<DateQueryBean> getWeekDatesFromNow(int weeks) {

		DateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Calendar cal = Calendar.getInstance();

		List<DateQueryBean> records = new ArrayList<DateQueryBean>();
		for (int i = 0; i < weeks; i++) {
			DateQueryBean query = new DateQueryBean();
			query.index = i + 1;
			query.end = sf.format(cal.getTime());
			cal.add(Calendar.DAY_OF_YEAR, -7);
			query.start = sf.format(cal.getTime());
			System.out.println(query.toString());
			records.add(query);
		}

		// Collections.reverse(records);
		return records;
	}

	/**
	 * 从现在开始获取过去月的开始时间与结束时间：比如 weeks
	 * 
	 * @param months
	 *            :过去多少月,暂时按一个月30天来算 返回的数据都是前闭后开
	 * @return
	 */
	public static List<DateQueryBean> getMonthDatesFromNow(int months) {

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();

		List<DateQueryBean> records = new ArrayList<DateQueryBean>();
		for (int i = 0; i < months; i++) {
			DateQueryBean query = new DateQueryBean();
			query.index = i + 1;
			query.end = sf.format(cal.getTime());
			cal.add(Calendar.DAY_OF_YEAR, -31);
			query.start = sf.format(cal.getTime());
			System.out.println(query.toString());
			records.add(query);
		}
		Collections.reverse(records);
		return records;
	}

	public static long getDateTime(String datetime) {

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sf.parse(datetime);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return -1;

	}

	public static Date getDateTimeFromString(String datetime) {

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sf.parse(datetime);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void main(String[] args) {

		List<DateQueryBean> lists = getWeekDatesFromNow(5);

		for (int i = 0; i < lists.size(); i++) {
			System.out.println(lists.get(i));
		}

	}

}
