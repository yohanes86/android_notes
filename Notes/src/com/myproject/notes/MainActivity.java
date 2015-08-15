package com.myproject.notes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.myproject.notes.adapters.CustomBaseAdapter;
import com.myproject.notes.data.Constants;
import com.myproject.notes.entity.Notes;
import com.myproject.notes.entity.RowItem;
import com.myproject.notes.mapper.NotesMapper;
import com.myproject.notes.utils.DateUtils;
import com.myproject.notes.utils.StringUtils;

public class MainActivity extends Activity implements OnItemLongClickListener { // OnItemClickListener
																				// {
	private static final String TAG = "MainActivity";

	public static final int MENU_ADD 		= Menu.FIRST + 1;
	public static final int MENU_EDIT 		= Menu.FIRST + 2;
	public static final int MENU_SAVE 		= Menu.FIRST + 3;
	public static final int MENU_SETTINGS 	= Menu.FIRST + 4;

	ListView listView;
	List<RowItem> rowItems;
	List<Notes> listNotes;
	
	private int indexList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// getActionBar().setDisplayHomeAsUpEnabled(true); // for API-11

		// try to load from database android
		try {
			NotesMapper notesMapper = new NotesMapper(getApplicationContext());
			listNotes = notesMapper.getAllNotes();

		} catch (Exception e) {
			Log.e(TAG, "exception error: " + e, e);
		}

		rowItems = new ArrayList<RowItem>();
		for (int i = 0; i < listNotes.size(); i++) {
			Date lastUpd = DateUtils.getDateTime(listNotes.get(i).getUpdatedOn(), Constants.FORMAT_DATE_DB);
			String lastUpdated = "Last Updated: " + StringUtils.getDateTime(lastUpd, Constants.FORMAT_DATE_DISPLAY);

			RowItem item = new RowItem(R.drawable.ic_launcher, listNotes.get(i).getId(), listNotes.get(i).getTitle(), lastUpdated);
			rowItems.add(item);
			// RowItem item = new RowItem(images[i], titles[i],
			// descriptions[i]);
			// rowItems.add(item);
		}

		listView = (ListView) findViewById(R.id.listNotes);
		CustomBaseAdapter adapter = new CustomBaseAdapter(this, rowItems);
		listView.setAdapter(adapter);
		// listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// Toast toast = Toast.makeText(getApplicationContext(),
	// "Item " + (position + 1) + ": " + rowItems.get(position),
	// Toast.LENGTH_SHORT);
	// toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	// toast.show();
	//
	// String title [] = rowItems.get(position).toString().split("\n");
	// Intent intent = new Intent(getApplicationContext(), FillNotes.class);
	// // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// intent.putExtra("title", title[0]);
	// startActivity(intent);
	// }

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		indexList = position;
		final long idNotes = parent.getItemIdAtPosition(position);
		Toast toast = Toast.makeText(getApplicationContext(), "Item " + (position + 1) + ": " + rowItems.get(position) + " -> ItemId: " + parent.getItemIdAtPosition(position), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();

//		String title [] = rowItems.get(position).toString().split("\n");
//		Intent intent = new Intent(getApplicationContext(), FillNotes.class);
//		intent.putExtra("title", title[0]);
//		startActivity(intent);

		// popup for next action (edit or delete)
		final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Action");
		builder.setItems(R.array.menu, new DialogInterface.OnClickListener() {
			String[] menus = getResources().getStringArray(R.array.menu);

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// Toast toast = Toast.makeText(MainActivity.this,
				// "Item " + (which) + ": " + menus[which],
				// Toast.LENGTH_SHORT);
				// toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0,
				// 0);
				// toast.show();
				
				switch (which) {
				case 0: //edit
					 Toast.makeText(MainActivity.this, "Edit: "+ idNotes, Toast.LENGTH_SHORT).show();
					 promptsEnterPassword(idNotes, Constants.EDIT);
					 
//					 Toast.makeText(MainActivity.this, "Edit: "+ idNotes, Toast.LENGTH_SHORT).show();
//					 String title [] = rowItems.get(indexList).toString().split("\n");
//					 Intent intent = new Intent(getApplicationContext(),FillNotes.class);
//					 intent.putExtra("title", title[0]);
//					 startActivity(intent);
					 dialog.dismiss();
					break;
				case 1: //delete
					Toast.makeText(MainActivity.this, "Delete: " + idNotes, Toast.LENGTH_SHORT).show();
					promptsEnterPassword(idNotes, Constants.DELETE);
//					popUpConfirmDelete(idNotes);
					dialog.dismiss();
					break;
				default:
					break;
				}
			}
		});

		builder.show();

		return false;
	}
	
	public void popUpConfirmDelete(long idNotes){
		final long idNotess = idNotes;
		final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("Are you sure want to delete this note?");
		builder.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
			 	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "Delete >> " + idNotess, Toast.LENGTH_SHORT).show();
				// find by id notes then delete
				NotesMapper notesMapper = new NotesMapper(getApplicationContext());
				Notes notes = notesMapper.getNotesById(Integer.parseInt(String.valueOf(idNotess)));
				notesMapper.deleteNotes(notes);
				
				// back to main activity for refresh list
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "Cancel >> "+ idNotess, Toast.LENGTH_SHORT).show();
				
			}
		});
		builder.show();
	}

	// @Override
	// public Dialog onCreateDialog(Bundle savedInstanceState) {
	// AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	// builder.setTitle(R.string.pick_color)
	// .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// // The 'which' argument contains the index position
	// // of the selected item
	// }
	// });
	// return builder.create();
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// using layout.xml
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main_activity_bar, menu);

		menu.add(Menu.NONE, MENU_ADD, Menu.NONE, "Add Notes").setIcon(R.drawable.add_icon)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		// menu.add(Menu.NONE, MENU_EDIT, Menu.NONE,
		// "Edit Notes").setIcon(R.drawable.edit_icon)
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		// menu.add(Menu.NONE, MENU_SAVE, Menu.NONE, "Save Notes")
		// .setIcon(R.drawable.save_icon)
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		menu.add(Menu.NONE, MENU_SETTINGS, Menu.NONE, "Settings")
		// .setIcon(R.drawable.edit_icon)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		// case android.R.id.home:
		// // NavUtils.navigateUpFromSameTask(this);
		// return true;

		case MENU_ADD: // case R.id.add_item:
			// Toast.makeText(this, "add item", Toast.LENGTH_SHORT).show();
			promptsAddTitle();
			return true;

		case MENU_EDIT: // R.id.edit_item:
			Toast.makeText(this, "edit item", Toast.LENGTH_SHORT).show();
			return true;

		case MENU_SAVE: // R.id.save_item:
			Toast.makeText(this, "save item", Toast.LENGTH_SHORT).show();
			return true;

		case MENU_SETTINGS: // R.id.settings_item:
			Toast.makeText(this, "settings item", Toast.LENGTH_SHORT).show();
			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	public void promptsAddTitle() {
		// get prompts.xml view
		LayoutInflater li = LayoutInflater.from(MainActivity.this);
		View promptsView = li.inflate(R.layout.add_title_prompts, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// get input from alert dialog
				String title = userInput.getText().toString();
				if (!TextUtils.isEmpty(title)) {
					Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();

					Intent intent = new Intent(getApplicationContext(), FillNotes.class);
					// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("title", title);
					startActivity(intent);

				} else {
					Toast.makeText(getApplicationContext(), "Title Empty", Toast.LENGTH_SHORT).show();
				}
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
	
	public void promptsEnterPassword(long idNotes, int varState) {
		final long idNote = idNotes; 
		final int state = varState; // 0:edit, 1:delete
		// get prompts.xml view
		LayoutInflater li = LayoutInflater.from(MainActivity.this);
		View promptsView = li.inflate(R.layout.add_password_prompts, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogPassword);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// get input from alert dialog
				String userInputPassword = userInput.getText().toString();
				if (!TextUtils.isEmpty(userInputPassword)) {
//					Toast.makeText(getApplicationContext(), userInputPassword, Toast.LENGTH_SHORT).show();

					// support password
					NotesMapper notesMapper = new NotesMapper(getApplicationContext());
					Notes notes = notesMapper.getNotesById(Integer.parseInt(String.valueOf(idNote)));
					if(notes != null){
						if(!TextUtils.isEmpty(notes.getPassword())){
							// user type invalid password
							if(!notes.getPassword().equals(userInputPassword)){
								Toast.makeText(getApplicationContext(), "Sorry, Invalid Password!!", Toast.LENGTH_SHORT).show();
							}
							else{ // user type valid password
								if(state == Constants.EDIT){ // edit
									Intent intent = new Intent(getApplicationContext(),FillNotes.class);
									intent.putExtra("title", notes.getTitle());
									startActivity(intent);
								}
								else if(state == Constants.DELETE) { // delete
									popUpConfirmDelete(idNote);
								}
								
							}
						}
						else{ // if Notes don't have password
							Toast.makeText(getApplicationContext(), "Sorry, This Notes don't have Password!!", Toast.LENGTH_SHORT).show();
						}
					}
										
//					Intent intent = new Intent(getApplicationContext(), FillNotes.class);
//					intent.putExtra("title", password);
//					startActivity(intent);

				} else {
					Toast.makeText(getApplicationContext(), "Password Empty", Toast.LENGTH_SHORT).show();
				}
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
	
	@Override
	public void onBackPressed() {
		final Intent relaunch = new Intent(MainActivity.this, Exiter.class)
		.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK // CLEAR_TASK requires this
				| Intent.FLAG_ACTIVITY_CLEAR_TASK // finish everything else in the task
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); // hide (remove, in this case) task from recents
		startActivity(relaunch);
//		android.os.Process.killProcess(android.os.Process.myPid());
//		super.onBackPressed();
	}

}
