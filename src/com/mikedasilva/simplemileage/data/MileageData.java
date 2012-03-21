package com.mikedasilva.simplemileage.data;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.mikedasilva.simplemileage.model.MileageRecord;

/**
 * Keep track of the mileage records.
 * 
 * @author mike
 *
 */
public class MileageData extends SQLiteOpenHelper {
	
	// DB info
	private static final String DATABASE_NAME = "mileage.db";
	private static final int DATABASE_VERSION = 2;
	
	// data info
	public static final String TABLE_NAME = "mileage";
	public static final String _ID = BaseColumns._ID;
	public static final String DISTANCE = "distance";
	public static final String UNIT = "unit";
	public static final String DATE = "date";
	

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
		+ DISTANCE + " INTEGER, "
		+ DATE + " INTEGER NOT NULL "
		+ ");";
		
		// execute the sql
		db.execSQL(sql);

	}

	/**
	 * Handle an upgrade
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO handle upgrading the DB and remove the DROP statement
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
		onCreate(db);
	}
	
	/**
	 * Insert a record into the DB
	 * 
	 * @param mileageRecord
	 */
	public void insert(MileageRecord mileageRecord) {
		
		SQLiteDatabase db = getWritableDatabase();
		
		// gather the values needed
		ContentValues values = new ContentValues();
		values.put(DISTANCE, mileageRecord.getDistance());
		values.put(DATE, mileageRecord.getDate().getTime());
		
		// do the insert
		db.insertOrThrow(TABLE_NAME, null, values);
	}
	
	public Map getRecords() {
		HashMap<String,String> records = new HashMap();
		
		SQLiteDatabase db = getReadableDatabase();
		String sql = "SELECT "+DATE+", "+DISTANCE+" FROM "+TABLE_NAME;
		sql = "SELECT * FROM MILEAGE";
		
		Cursor cursor = db.rawQuery(sql, null);
		 cursor.moveToFirst();
	        while (cursor.isAfterLast() == false) {
	            records.put(cursor.getString(0), Long.valueOf(cursor.getInt(1)).toString());
	       	    cursor.moveToNext();
	        }
	        cursor.close();
	        
	        return records;
	}

}
