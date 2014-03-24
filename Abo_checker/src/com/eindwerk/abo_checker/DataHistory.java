package com.eindwerk.abo_checker;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHistory {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_DAY = "day";
	public static final String KEY_MONTH = "month";
	public static final String KEY_YEAR = "year";
	public static final String KEY_DATA_FROM_BOOT = "data_from_boot";
	public static final String KEY_DATA_PER_HOUR = "data_per_hour";
	private static final String DATABASE_NAME = "ABOdb";
	private static final String DATABASE_TABLE = "DataHistoryTable";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			//aanmaken van de tabel
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
			KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DAY + " INTEGER NOT NULL, "+ KEY_MONTH + " INTEGER NOT NULL, "
			+ KEY_YEAR + " INTEGER NOT NULL, "+ KEY_DATA_FROM_BOOT + " INTEGER NOT NULL, "+ KEY_DATA_PER_HOUR + " INTEGER NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
			
		}
	
		
	}
	public DataHistory(Context c){
		ourContext = c;
	}
	
	public DataHistory open(){
		
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
		
		
	}
	public void close(){
		ourHelper.close();
	}


	public long insertData(int dayNow, int monthNow, int yearNow,
			long totalMB_from_boot, long mb_per_hour) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_DAY, dayNow);
		cv.put(KEY_MONTH, monthNow);
		cv.put(KEY_YEAR, yearNow);
		cv.put(KEY_DATA_FROM_BOOT, totalMB_from_boot);
		cv.put(KEY_DATA_PER_HOUR, mb_per_hour);
		
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	public long getLastData() {
		//methode om de laatste data_from_boot te halen
				
		String[] columns = new String[]{KEY_ROWID, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_DATA_FROM_BOOT, KEY_DATA_PER_HOUR};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null,null, null, null, null,null);
		long result = 0;
		
				int iUsed_from_boot = c.getColumnIndex(KEY_DATA_FROM_BOOT);
			
		for(c.moveToLast(); !c.isAfterLast(); c.moveToNext()){
			
			result = c.getLong(iUsed_from_boot);
			
			
		}
	return result;
}
	
public String getData(int month, int year) {
		
		// methode om het verbruik per maand te berekenen en terug te sturen
		String[] columns = new String[]{KEY_ROWID, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_DATA_FROM_BOOT, KEY_DATA_PER_HOUR};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null,null, null, null, null,null);
		String result = "";
		long data =0; 
		

		int iMonth = c.getColumnIndex(KEY_MONTH);
		int iYear = c.getColumnIndex(KEY_YEAR);
		int iUsed_per_hour= c.getColumnIndex(KEY_DATA_PER_HOUR);
		
		int sMonth = 0;
		int sYear = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			sMonth = c.getInt(iMonth);
			sYear = c.getInt(iYear);
			if (sMonth == month && sYear == year){
				data += c.getLong(iUsed_per_hour);
	}
			
			
			
		}
			result+= data;
	return result;
}
}
