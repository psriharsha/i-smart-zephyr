package com.ismart.zephyr.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.ismart.zephyr.provider.Vitals.Vital;

public class VitalContentProvider extends ContentProvider{

	 private static final String TAG = "VitalContentProvider";	 
	 private static final String DATABASE_NAME = "ismart.db";	 
	 private static final int DATABASE_VERSION = 1;	 
	 private static final String NOTES_TABLE_NAME = "vitals";	 
	 public static final String AUTHORITY = "com.ismart.zephyr.provider.VitalContentProvider";	 
	 private static UriMatcher sUriMatcher = null;	 
	 private static HashMap<String, String> notesProjectionMap; 
	 private static final int VITALS = 1;	 
	 private static final int VITALS_ID = 2;
	 private static final int VITALS_HR = 3;
	 private static final int VITALS_RR = 4;
	 private static final int VITALS_TEMP = 5;
	 private static final int VITALS_POST = 6;
	 private static final int VITALS_ACC = 7;
	 
	 private static class DatabaseHelper extends SQLiteOpenHelper {
		 
	        DatabaseHelper(Context context) {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        }
	 
	        @Override
	        public void onCreate(SQLiteDatabase db) {
	            db.execSQL("CREATE TABLE " + NOTES_TABLE_NAME + " (" + Vital._ID
	                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + Vital.HR + " INTEGER," + Vital.RR + " INTEGER," + Vital.TEMP
	                    + " INTEGER," + Vital.POST + " VARCHAR(255)," + Vital.ACC + " VARCHAR(255));");
	        }
	 
	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
	            db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
	            onCreate(db);
	        }
	    }
	
	 private DatabaseHelper dbHelper;
	 
	public VitalContentProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case VITALS:
                break;
            case VITALS_ID:
                where = where + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
 
        int count = db.delete(NOTES_TABLE_NAME, where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (sUriMatcher.match(uri)) {
        case VITALS:
            return Vital.CONTENT_TYPE;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
    }
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		 if (sUriMatcher.match(uri) != VITALS) {
	            throw new IllegalArgumentException("Unknown URI " + uri);
	        }
	        ContentValues values;
	        if (initialValues != null) {
	            values = new ContentValues(initialValues);
	        } else {
	            values = new ContentValues();
	        }
	 
	        SQLiteDatabase db = dbHelper.getWritableDatabase();
	        long rowId = db.insert(NOTES_TABLE_NAME, null, values);
	        if (rowId > 0) {
	            Uri noteUri = ContentUris.withAppendedId(Vital.CONTENT_URI, rowId);
	            getContext().getContentResolver().notifyChange(noteUri, null);
	            return noteUri;
	        }
	 
	        throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		 dbHelper = new DatabaseHelper(getContext());
	     return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(NOTES_TABLE_NAME);
        qb.setProjectionMap(notesProjectionMap);
 
        switch (sUriMatcher.match(uri)) {    
            case VITALS:
                break;
            case VITALS_ID:
                selection = selection + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
 
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
 
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case VITALS:
                count = db.update(NOTES_TABLE_NAME, values, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
 
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, NOTES_TABLE_NAME, VITALS);
        sUriMatcher.addURI(AUTHORITY, NOTES_TABLE_NAME + "/#", VITALS_ID);
        sUriMatcher.addURI(AUTHORITY, NOTES_TABLE_NAME + "/#", VITALS_HR);
        sUriMatcher.addURI(AUTHORITY, NOTES_TABLE_NAME + "/#", VITALS_RR);
        sUriMatcher.addURI(AUTHORITY, NOTES_TABLE_NAME + "/#", VITALS_TEMP);
        sUriMatcher.addURI(AUTHORITY, NOTES_TABLE_NAME + "/#", VITALS_POST);
        sUriMatcher.addURI(AUTHORITY, NOTES_TABLE_NAME + "/#", VITALS_ACC);
 
        notesProjectionMap = new HashMap<String, String>();
        notesProjectionMap.put(Vital._ID, Vital._ID);
        notesProjectionMap.put(Vital.HR, Vital.HR);
        notesProjectionMap.put(Vital.RR, Vital.RR);
        notesProjectionMap.put(Vital.TEMP, Vital.TEMP);
        notesProjectionMap.put(Vital.POST, Vital.POST);
        notesProjectionMap.put(Vital.ACC, Vital.ACC);
    }
}
