package com.ismart.zephyr.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ISmartAuthenticatorService extends Service{
	private static final String TAG = "AccountAuthenticatorService";
	private static ISmartAuthenticator iSmartAuthenticator = null;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		//return new ISmartAuthenticator(this).getIBinder();
		IBinder ret = null;
		if (intent.getAction().equals(android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT))
		{
		ret = new ISmartAuthenticator(this).getIBinder();
		}
		return ret;
		}

		private ISmartAuthenticator getAuthenticator()
		{
		if (iSmartAuthenticator == null)
		{
			iSmartAuthenticator = new ISmartAuthenticator(this);
		}
		return iSmartAuthenticator;
		}
	}
