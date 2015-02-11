package com.ok.map.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ok.map.database.MapContract.Map;






public class MapDBHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "map.db";
	
	private static final String SQL_CREATE_MAP = "CREATE TABLE " + Map.TABLE_NAME + " (" +
			Map._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			Map.COLUMN_NAME_MAP_NAME + " TEXT, " +
			Map.COLUMN_NAME_MAP_START_LATITUDE + " REAL, " +
			Map.COLUMN_NAME_MAP_START_LOGITUDE + " REAL, " +
			Map.COLUMN_NAME_MAP_TARGET_LATITUDE + " REAL, " +
			Map.COLUMN_NAME_MAP_TARGET_LOGITUDE + " REAL, " +
			Map.COLUMN_NAME_MAP_COST + " REAL, " +
			Map.COLUMN_NAME_MAP_DISTANCE + " TEXT, " +
			Map.COLUMN_NAME_MAP_DURATION + " TEXT " +
		
	    " )";
	
	private static final String SQL_DELETE_ALARM =
		    "DROP TABLE IF EXISTS " + Map.TABLE_NAME;
    
	public MapDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_MAP);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ALARM);
        onCreate(db);
	}
	
	private MapModel populateModel(Cursor c) {
		MapModel model = new MapModel();
		model.setId(c.getLong(c.getColumnIndex(Map._ID)));
		model.setName(c.getString(c.getColumnIndex(Map.COLUMN_NAME_MAP_NAME)));
		model.setStartLatitude(c.getDouble(c.getColumnIndex(Map.COLUMN_NAME_MAP_START_LATITUDE)));
		model.setStartLongitude(c.getDouble(c.getColumnIndex(Map.COLUMN_NAME_MAP_START_LOGITUDE)));
	    model.setTargetLatitude(c.getDouble(c.getColumnIndex(Map.COLUMN_NAME_MAP_TARGET_LATITUDE)));
		model.setTargetLongitude(c.getDouble(c.getColumnIndex(Map.COLUMN_NAME_MAP_TARGET_LOGITUDE)));
		model.setCost(c.getDouble(c.getColumnIndex(Map.COLUMN_NAME_MAP_COST)));
		model.setDistance(c.getString(c.getColumnIndex(Map.COLUMN_NAME_MAP_DISTANCE)));
		model.setDuration(c.getString(c.getColumnIndex(Map.COLUMN_NAME_MAP_DURATION)));
		return model;
	}
	
	private ContentValues populateContent(MapModel model) {
		ContentValues values = new ContentValues();
        values.put(Map.COLUMN_NAME_MAP_NAME, model.getName());
        values.put(Map.COLUMN_NAME_MAP_START_LATITUDE, model.getStartLatitude());
        values.put(Map.COLUMN_NAME_MAP_START_LOGITUDE, model.getStartLongitude());
        values.put(Map.COLUMN_NAME_MAP_TARGET_LATITUDE, model.getTargetLatitude());
        values.put(Map.COLUMN_NAME_MAP_TARGET_LOGITUDE, model.getTargetLongitude());
        values.put(Map.COLUMN_NAME_MAP_COST, model.getCost());
        values.put(Map.COLUMN_NAME_MAP_DISTANCE, model.getDistance());
        values.put(Map.COLUMN_NAME_MAP_DURATION, model.getDuration());
   
        return values;
	}
	
	public long createAlarm(MapModel model) {
		ContentValues values = populateContent(model);
        return getWritableDatabase().insert(Map.TABLE_NAME, null, values);
	}
	
	public long updateAlarm(MapModel model) {
		ContentValues values = populateContent(model);
        return getWritableDatabase().update(Map.TABLE_NAME, values, Map._ID + " = ?", new String[] { String.valueOf(model.getId()) });
	}
	
	public MapModel getAlarm(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
        String select = "SELECT * FROM " + Map.TABLE_NAME + " WHERE " + Map._ID + " = " + id;
		
		Cursor c = db.rawQuery(select, null);
		
		if (c.moveToNext()) {
			return populateModel(c);
		}
		
		return null;
	}
	
	public List<MapModel> getAlarms() {
		SQLiteDatabase db = this.getReadableDatabase();
		
        String select = "SELECT * FROM " + Map.TABLE_NAME;
		
		Cursor c = db.rawQuery(select, null);
		
		List<MapModel> alarmList = new ArrayList<MapModel>();
		
		while (c.moveToNext()) {
			alarmList.add(populateModel(c));
		}
		
		if (!alarmList.isEmpty()) {
			return alarmList;
		}
		
		return null;
	}
	
	public int deleteAlarm(long id) {
		return getWritableDatabase().delete(Map.TABLE_NAME, Map._ID + " = ?", new String[] { String.valueOf(id) });
	}
}
