package com.ismart.zephyr.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Vitals {

	public Vitals() {
		// TODO Auto-generated constructor stub
	}

	 public static final class Vital implements BaseColumns {
	        private Vital() {
	        }
	 
	        public static final Uri CONTENT_URI = Uri.parse("content://"
	                + VitalContentProvider.AUTHORITY + "/notes");
	 
	        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.jwei512.notes";
	 
	        public static final String VITAL_ID = "_id";
	 
	        public static final String HR = "heartRate";
	 
	        public static final String RR = "respRate";
	        
	        public static final String TEMP = "temp";
	        
	        public static final String POST = "posture";
	        
	        public static final String ACC = "acceleration";
	    }
}
