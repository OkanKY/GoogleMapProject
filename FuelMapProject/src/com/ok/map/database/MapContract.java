package com.ok.map.database;

import android.provider.BaseColumns;

public final class MapContract {
	
	public MapContract() {}
	
	public static abstract class Map implements BaseColumns {
		public static final String TABLE_NAME = "map";
		public static final String COLUMN_NAME_MAP_NAME = "name";
		public static final String COLUMN_NAME_MAP_START_LATITUDE = "startLatitude";
		public static final String COLUMN_NAME_MAP_START_LOGITUDE = "startLongitude";
		public static final String COLUMN_NAME_MAP_TARGET_LATITUDE = "targetLatitude";
		public static final String COLUMN_NAME_MAP_TARGET_LOGITUDE = "targetLongitude";
		public static final String COLUMN_NAME_MAP_COST = "cost";
		public static final String COLUMN_NAME_MAP_DISTANCE = "distance";
		public static final String COLUMN_NAME_MAP_DURATION = "duration";
	}
	
}
