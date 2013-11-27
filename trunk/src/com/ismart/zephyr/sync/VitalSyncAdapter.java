package com.ismart.zephyr.sync;

import java.io.IOException;

import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;

import com.ismart.zephyr.RestClient;
import com.ismart.zephyr.Singleton;
import com.ismart.zephyr.RestClient.RequestMethod;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.net.ParseException;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class VitalSyncAdapter extends AbstractThreadedSyncAdapter{
	private final AccountManager mAccountManager;
	private final Context mContext;
	private static final String TAG = "VitalSyncAdapter";
    private static final String SYNC_MARKER_KEY = "com.ismart.zephyr";
    private static final boolean NOTIFY_AUTH_FAILURE = true;
	public VitalSyncAdapter(Context context, boolean autoInitialize) {
		// TODO Auto-generated constructor stub
		super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
        mContext = context;
	}

	@SuppressLint("NewApi")
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		// TODO Auto-generated method stub
		// try {
	            // see if we already have a sync-state attached to this account. By handing
	            // This value to the server, we can just get the contacts that have
	            // been updated on the server-side since our last sync-up
	           // final String authtoken = mAccountManager.blockingGetAuthToken(account,
	            //		"com.ismart.zephyr", NOTIFY_AUTH_FAILURE);
	           // Toast.makeText(mContext, "In Sync", Toast.LENGTH_SHORT).show();
	           
	            /*} catch (final AuthenticatorException e) {
	            Log.e(TAG, "AuthenticatorException", e);
	            syncResult.stats.numParseExceptions++;
	        } catch (final OperationCanceledException e) {
	            Log.e(TAG, "OperationCanceledExcetpion", e);
	        } catch (final IOException e) {
	            Log.e(TAG, "IOException", e);
	            syncResult.stats.numIoExceptions++;
	        } catch (final ParseException e) {
	            Log.e(TAG, "ParseException", e);
	            syncResult.stats.numParseExceptions++;
	        }*/
		try{
			RestClient client = new RestClient("http://"+Singleton.ip+"/zephyr/index.php/service/user/test");
			client.AddParam("text", "ding");
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
    		try {
    		    client.Execute(RequestMethod.POST);
    		} catch (Exception e) {
    		    e.printStackTrace();
    		}

    		String response = client.getResponse();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


}