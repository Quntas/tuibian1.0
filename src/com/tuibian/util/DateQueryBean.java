package com.tuibian.util;

import java.util.ArrayList;
import java.util.List;

import com.tuibian.model.CBaseObject;

public class DateQueryBean {

	public int index = 0;

	public String start = null;

	public String end = null;

	public List<CBaseObject> lists = new ArrayList<CBaseObject>();

	@Override
	public String toString() {
		return "DateQuery [index=" + index + ", start=" + start + ", end="
				+ end + "]";
	}

}
