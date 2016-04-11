package com.tuibian.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;

/**
 * activity 栈管理器
 * 
 * @author 行者
 * 
 */
public class ActivityTack {

	public static List<Activity> activityList = new ArrayList<Activity>();

	public static ActivityTack tack = new ActivityTack();

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static ActivityTack getInstanse() {
		return tack;
	}

	private ActivityTack() {

	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void removeActivity(Activity activity) {
		activityList.remove(activity);
	}

	/**
	 * 完全退出
	 */
	public void exit() {
		for (Activity ac : activityList) {
			if (!ac.isFinishing()) {
				ac.finish();
			}
		}
	}

	/**
	 * 根据class name获取activity
	 * 
	 * @param name
	 * @return
	 */
	public Activity getActivityByClassName(String name) {
		for (Activity ac : activityList) {
			if (ac.getClass().getName().indexOf(name) >= 0) {
				return ac;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Activity getActivityByClass(Class cs) {
		for (Activity ac : activityList) {
			if (ac.getClass().equals(cs)) {
				return ac;
			}
		}
		return null;
	}

	/**
	 * 弹出activity
	 * 
	 * @param activity
	 */
	public void popActivity(Activity activity) {
		removeActivity(activity);
		activity.finish();
	}

	/**
	 * 弹出activity�?
	 * 
	 * @param cs
	 */
	@SuppressWarnings("rawtypes")
	public void popUntilActivity(Class... cs) {
		List<Activity> list = new ArrayList<Activity>();
		for (int i = activityList.size() - 1; i >= 0; i--) {
			Activity ac = activityList.get(i);
			boolean isTop = false;
			for (int j = 0; j < cs.length; j++) {
				if (ac.getClass().equals(cs[j])) {
					isTop = true;
					break;
				}
			}
			if (!isTop) {
				list.add(ac);
			} else
				break;
		}
		for (Iterator<Activity> iterator = list.iterator(); iterator.hasNext();) {
			Activity activity = iterator.next();
			popActivity(activity);
		}
	}
}
