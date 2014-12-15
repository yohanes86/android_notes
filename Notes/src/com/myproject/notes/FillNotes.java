package com.myproject.notes;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.myproject.notes.data.Constants;
import com.myproject.notes.entity.Notes;
import com.myproject.notes.mapper.NotesMapper;
import com.myproject.notes.utils.CipherUtil;
import com.myproject.notes.utils.StringUtils;

public class FillNotes extends Activity{
	private static final String TAG = "FillNotes";
	
	private EditText title;
	private EditText content;
	
	private Notes notes;
	
	public static final int MENU_ADD = Menu.FIRST + 1;
	public static final int MENU_EDIT = Menu.FIRST + 2;
	public static final int MENU_SAVE = Menu.FIRST + 3;
	public static final int MENU_SETTINGS = Menu.FIRST + 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notes_layout);
		
		// get passed intent 
		Intent intent = getIntent();
		// get message value from intent
		String titleNotes = intent.getStringExtra("title");
		Log.d("TitleNotes: ", titleNotes);
		
		// try to load from database android
		try {
			 NotesMapper notesMapper = new NotesMapper(getApplicationContext());
			 notes = notesMapper.getNotesByTitle(titleNotes);
			
			 title = (EditText) findViewById(R.id.notesTitle);
			 title.setText(titleNotes);
			 
			 content = (EditText) findViewById(R.id.notesContent);
			 content.setText(notes.getContent());
			 
		} catch (Exception e) {
			Log.e(TAG, "exception error: " + e, e);
		}
		getActionBar().setDisplayHomeAsUpEnabled(true); // for API-11
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// using layout.xml
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main_activity_bar, menu);

//		menu.add(Menu.NONE, MENU_ADD, Menu.NONE, "Add Notes").setIcon(R.drawable.add_icon)
//				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

//		menu.add(Menu.NONE, MENU_EDIT, Menu.NONE, "Edit Notes").setIcon(R.drawable.edit_icon)
//				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		 menu.add(Menu.NONE, MENU_SAVE, Menu.NONE, "Save Notes")
		 .setIcon(R.drawable.save_icon)
		 .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, "Settings")
		// .setIcon(R.drawable.edit_icon)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			try {
				createOrUpdateNotes();
				
				Intent intent = new Intent(this, MainActivity.class);
			    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    startActivity(intent);
				return true;
			} catch (Exception e) {
				Log.e(TAG, "Error Exception: ", e);
			}
			return true;			
			
		case MENU_EDIT: // R.id.edit_item:
			Toast.makeText(this, "edit item", Toast.LENGTH_SHORT).show();
			return true;

		case MENU_SAVE: // R.id.save_item:
//			Toast.makeText(this, "save item", Toast.LENGTH_SHORT).show();
			try {
				createOrUpdateNotes();
				
				Intent intent = new Intent(this, MainActivity.class);
			    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    startActivity(intent);
				return true;
			} catch (Exception e) {
				Log.e(TAG, "Error Exception: ", e);
			}
			return true;

		case MENU_SETTINGS: // R.id.settings_item:
			Toast.makeText(this, "settings item", Toast.LENGTH_SHORT).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	public void createOrUpdateNotes(){
		// get passed intent 
		Intent intent = getIntent();
		String titleNotes = intent.getStringExtra("title");
		
		content = (EditText) findViewById(R.id.notesContent);
		
		 // try to insert database android
		 NotesMapper notesMapper = new NotesMapper(getApplicationContext());
         try {
        	 if(notes != null){
		         notes.setTitle(titleNotes);
		         notes.setContent(CipherUtil.encryptDESede(content.getText().toString(), Constants.PASSWORD));
//		         notes.setContent(content.getText().toString());
		         notes.setUpdatedOn(StringUtils.getDateTime(new Date(), Constants.FORMAT_DATE_DB));
		         notesMapper.updateNotes(notes);
        	 }
        	 else{	        		
		         Notes notes = new Notes();
		         notes.setTitle(titleNotes);
		         notes.setContent(CipherUtil.encryptDESede(content.getText().toString(), Constants.PASSWORD));
//		         notes.setContent(content.getText().toString());
		         notes.setCreatedOn(StringUtils.getDateTime(new Date(), Constants.FORMAT_DATE_DB));
		         notes.setUpdatedOn(StringUtils.getDateTime(new Date(), Constants.FORMAT_DATE_DB));
		         notesMapper.insertNotes(notes);
        	 }
		} catch (Exception e) {
			Log.e(TAG, "exception error: " + e, e);
		}
	}
}
