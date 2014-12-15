package com.myproject.notes.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class DateUtils {
	private static final String TAG = "DateUtils";
	
	public static Date getDateTime(String dates, String format) {
		Date date;
		try {
			date = new SimpleDateFormat(format, Locale.getDefault()).parse(dates);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Error ParseException: ", e);
			e.printStackTrace();
		}
        return null;
    }
}
