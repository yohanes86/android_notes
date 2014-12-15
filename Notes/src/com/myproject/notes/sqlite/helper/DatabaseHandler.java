package com.myproject.notes.sqlite.helper;

import com.myproject.notes.mapper.NotesMapper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{
	private static final String TAG = "DatabaseHandler";
	
	private static DatabaseHandler helper = null;
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "MyNotes.db";
	
	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i(TAG, "---------- DatabaseHandler---------------");
	}
	
	public static DatabaseHandler getInstance(Context context){
        if(helper == null){
            helper = new DatabaseHandler(context);
//            Log.i(TAG, "---------- DatabaseHandler---------------");
        }
        return helper;
    }
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(NotesMapper.CREATE_TABLE_NOTES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + UserDataMapper.TABLE_USER_DATA);
 
        // Create tables again
//        onCreate(db);
	}
	
}
