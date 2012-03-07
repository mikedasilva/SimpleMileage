package com.mikedasilva.simplemileage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Keep track of the mileage records.
 * 
 * @author mike
 *
 */
public class MileageData extends SQLiteOpenHelper {
	
	// DB info
	private static final String DATABASE_NAME = "mileage.db";
	private static final int DATABASE_VERSION = 1;
	
	// data info
	public static final String TABLE_NAME = "mileage";
	public static final String _ID = BaseColumns._ID;
	public static final String DISTANCE = "distance";
	public static final String UNIT = "unit";
	public static final String DATE = "date";
	public static final String STATE = "state"; // for future feature
	

	/**
	 * Constructor
	 * @param context
	 */
	public MileageData(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE "+TABLE_NAME + " (" 
		+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
		+ UNIT + " TEXT NOT NULL, "
		+ DISTANCE + " INTEGER"
		+ DATE + " DATE NOT NULL, "
		+ STATE + " TEXT NULL, "
		+ ");";
		
		// execute the sql
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO handle upgrading the DB

	}
	
	/**
	 * Insert a record into the DB
	 * 
	 * @param distance
	 * @param date
	 * @param unit
	 */
	public void insert(int distance, String date, String unit) {
		SQLiteDatabase db = getWritableDatabase();
		
		// gather the values needed
		ContentValues values = new ContentValues();
		values.put(UNIT, unit);
		values.put(DISTANCE, distance);
		values.put(DATE, date);
		
		// do the insert
		db.insertOrThrow(TABLE_NAME, null, values);
	}

}
