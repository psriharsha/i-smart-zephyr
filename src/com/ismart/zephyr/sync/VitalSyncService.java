package com.ismart.zephyr.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class VitalSyncService extends Service{

	private static final Object sSyncAdapterLock = new Object();
    private static VitalSyncAdapter sSyncAdapter = null;
    
	public VitalSyncService() {
		// TODO Auto-generated constructor stub
	}

	public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null)
                sSyncAdapter = new VitalSyncAdapter(getApplicationContext(), true);
        }
    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return sSyncAdapter.getSyncAdapterBinder();
	}

}
