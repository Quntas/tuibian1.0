package com.tuibian.dialog;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

public class MyDatePickerDialog extends DatePickerDialog {

	public MyDatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,int dayOfMonth) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
}
