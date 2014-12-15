package com.myproject.notes.utils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.myproject.notes.data.Constants;

import android.util.Log;

public class StringUtils {
	private static final String TAG = "StringUtils";
	private static Random r = new Random();
	
	public static String urlEncode(String value) {
		String encodeResult = "";

		try {
			encodeResult = URLEncoder.encode(value, "utf-8");
		} catch (Exception e) {
			Log.v(TAG, "ERROR ENCODE URL UTILS: " + e.getMessage());
		}

		return encodeResult;
	}
	
	public static String generateAlphaNumeric(int length) {
		String C = "QWERTYUIOPLKJHGFDAZXCVBNM0987654321";
		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			int idx = r.nextInt(C.length());
			sb.append(C.substring(idx, idx + 1));
		}
		return sb.toString();
	}
	
	public static String getDateTime(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
//      Date date = new Date();
        return dateFormat.format(date);
    }
	
}
