package com.myproject.notes.mapper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.myproject.notes.data.Constants;
import com.myproject.notes.entity.Notes;
import com.myproject.notes.sqlite.helper.DatabaseManager;
import com.myproject.notes.utils.CipherUtil;



public class NotesMapper {
	private static final String TAG = "NotesMapper";
	private Context context;
	
	/**
	 * Table Content
	 **/
	public static final String TABLE_NOTES 				= "notes";
	public static final String COLUMN_NOTES_ID 			= "id";
	public static final String COLUMN_NOTES_TITLE 		= "title";
	public static final String COLUMN_NOTES_CONTENT 	= "content";
	public static final String COLUMN_NOTES_CREATED_ON 	= "created_on";
	public static final String COLUMN_NOTES_UPDATED_ON 	= "updated_on";
	
	public NotesMapper(Context ctx) {
		this.context = ctx;
	}
	
	public static final String CREATE_TABLE_NOTES= "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NOTES 
			+ "(" 
			+ COLUMN_NOTES_ID 		  + " integer PRIMARY KEY , " 
			+ COLUMN_NOTES_TITLE   	  + " text , " 
			+ COLUMN_NOTES_CONTENT    + " text , " 
			+ COLUMN_NOTES_CREATED_ON + " DATETIME , "
			+ COLUMN_NOTES_UPDATED_ON + " DATETIME "
			+ " );";
	
	public ContentValues getComposeNotes(Notes notes){
		ContentValues values = new ContentValues();
		values.put(COLUMN_NOTES_TITLE, notes.getTitle());
		values.put(COLUMN_NOTES_CONTENT, notes.getContent());
		values.put(COLUMN_NOTES_CREATED_ON, notes.getCreatedOn()); //StringUtils.getDateTime(userData.getCreatedOn())
		values.put(COLUMN_NOTES_UPDATED_ON, notes.getUpdatedOn());
		return values;
	}
	
	public void insertNotes(Notes notes) {
		try {
			SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
			ContentValues values = getComposeNotes(notes);
			database.insert(TABLE_NOTES, null,values);
		} catch (Exception e) {
			Log.e(TAG, " insertNotes error : " + "  " + e.getMessage());
		} finally{
			DatabaseManager.getInstance().closeDatabase();
		}
		
	}
	
	public void updateNotes(Notes notes) {
		try {
			SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
			ContentValues values = getComposeNotes(notes);
			database.update(TABLE_NOTES, values, COLUMN_NOTES_ID + "='" + notes.getId() + "'", null);
		} catch (Exception e) {
			Log.e(TAG, " updateNotes error : " + "  " + e.getMessage());
		} finally{
			DatabaseManager.getInstance().closeDatabase();
		}
		
	}
	
	
	public Notes getNotesByTitle(String title) {
		Notes notes = null;
		try {
			SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
			Cursor cursor = database.query(TABLE_NOTES, allColumnNotes, 
					COLUMN_NOTES_TITLE + "='" + title + "'", null, null, null, COLUMN_NOTES_CREATED_ON + " desc");
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				notes = new Notes();
				notes = cursorToNotes(cursor);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			Log.e(TAG, " getNotesByTitle error : " + "  " + e.getMessage());
		} finally{
			DatabaseManager.getInstance().closeDatabase();
		}
		
		return notes;
	}
	
	
	public Notes getNotesById(int id) {
		Notes notes = null;
		try {
			SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
			Cursor cursor = database.query(TABLE_NOTES, allColumnNotes, 
					COLUMN_NOTES_ID + "='" + id + "'", null, null, null, COLUMN_NOTES_CREATED_ON + " desc");
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				notes = new Notes();
				notes = cursorToNotes(cursor);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			Log.e(TAG, " getNotesById error : " + "  " + e.getMessage());
		} finally{
			DatabaseManager.getInstance().closeDatabase();
		}
		
		return notes;
	}
	
	
	public List<Notes> getAllNotes() {
		List<Notes> listNotes = new ArrayList<Notes>();
		try {
			SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
			Cursor cursor = database.query(TABLE_NOTES, allColumnNotes,
					null, null, null, null, COLUMN_NOTES_UPDATED_ON + " desc");
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Notes notes = cursorToNotes(cursor);
				listNotes.add(notes);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			Log.e(TAG, " getAllNotes error : " + "  " + e.getMessage());
		} finally{
			DatabaseManager.getInstance().closeDatabase();
		}
		
		return listNotes;
	}
	
	public int deleteNotes(Notes notes) {
		int rowDeleted = 0;
		try {
			SQLiteDatabase database = DatabaseManager.getInstance().openDatabase();
			rowDeleted = database.delete(TABLE_NOTES, COLUMN_NOTES_ID + " = " + notes.getId(),null);
			Log.i(TAG, " Notes deleted " + "  " + notes.getId());
		} catch (Exception e) {
			Log.e(TAG, " delete notes error : " + "  " + e.getMessage());
		} finally{
			DatabaseManager.getInstance().closeDatabase();
		}
		
		return rowDeleted;
	}
	
	private Notes cursorToNotes(Cursor cursor) {
		Notes notes = new Notes();
		notes.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTES_ID)));
		notes.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NOTES_TITLE)));
		notes.setContent(CipherUtil.decryptDESede(cursor.getString(cursor.getColumnIndex(COLUMN_NOTES_CONTENT)), Constants.PASSWORD));
		notes.setCreatedOn(cursor.getString(cursor.getColumnIndex(COLUMN_NOTES_CREATED_ON)));
		notes.setUpdatedOn(cursor.getString(cursor.getColumnIndex(COLUMN_NOTES_UPDATED_ON)));
		return notes;
	}
	
	private String[] allColumnNotes = { 
			COLUMN_NOTES_ID,
			COLUMN_NOTES_TITLE,
			COLUMN_NOTES_CONTENT,
			COLUMN_NOTES_CREATED_ON,
			COLUMN_NOTES_UPDATED_ON
	};
}
