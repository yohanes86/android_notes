package com.myproject.notes;

import android.app.Application;
import android.util.Log;

import com.myproject.notes.sqlite.helper.DatabaseHandler;
import com.myproject.notes.sqlite.helper.DatabaseManager;

public class InitialiazeApplication extends Application{
	private static final String TAG = "InitialiazeApplication";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		DatabaseHandler helper = DatabaseHandler.getInstance(getApplicationContext());
 		DatabaseManager.initializeInstance(helper);
 		Log.v( InitialiazeApplication.TAG, "Database Manager has beed initialized..." );
	}

}
