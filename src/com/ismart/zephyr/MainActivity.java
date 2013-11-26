package com.ismart.zephyr;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ismart.zephyr.RestClient.RequestMethod;
import com.ismart.zephyr.auth.ISmartAuthenticator;

public class MainActivity extends AccountAuthenticatorActivity implements OnClickListener {

	public static final String PARAM_AUTHTOKEN_TYPE = "com.ismart.zephyr";
	public static final String PARAM_CREATE = "create";
	public static final int REQ_CODE_CREATE = 1;
	public static final int REQ_CODE_UPDATE = 2;
	public static final String EXTRA_REQUEST_CODE = "req.code";
	public static final int RESP_CODE_SUCCESS = 0;
	public static final int RESP_CODE_ERROR = 1;
	public static final int RESP_CODE_CANCEL = 2;
	public ISmartAuthenticator iSmartAuthenticator;
	public AccountAuthenticatorResponse response;
	public AccountManager accMgr;
	EditText etusername, etpassword;
	Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		accMgr = AccountManager.get(this);
		String accNames[] = getAccounts();
		
		etusername = (EditText) findViewById(R.id.username);
		etpassword = (EditText) findViewById(R.id.password);
		submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
	}

	private String[] getAccounts() {
		// TODO Auto-generated method stub
		Account[] accounts = accMgr.getAccountsByType("com.ismart.zephyr");
		String[] names = null;
		if(accounts.length > 0){
	    names = new String[accounts.length];
	    for (int i = 0; i < names.length; i++) {
	        names[i] = accounts[i].name;
	    }
		}
	    return names;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.submit)
		{
			Boolean hasErrors = false;
			String error;
			String username, password;
			username = etusername.getText().toString();
			password = etpassword.getText().toString();
			String accountType = this.getIntent().getStringExtra(PARAM_AUTHTOKEN_TYPE);
			if (accountType == null)
			{
			accountType = ISmartAuthenticator.ACCOUNT_TYPE;
			}
			try{
				RestClient client = new RestClient("http://"+Singleton.ip+"/zephyr/index.php/service/user/authenticate");
				client.AddParam("user", username);
				client.AddParam("pass", password);
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		        StrictMode.setThreadPolicy(policy);
	    		try {
	    		    client.Execute(RequestMethod.POST);
	    		} catch (Exception e) {
	    		    e.printStackTrace();
	    		}

	    		String response = client.getResponse();
    			error = client.getErrorMessage();
	    		if(response.equals("Success"))
	    		{
	    			Toast.makeText(getApplicationContext(), "Username and Password are Correct", Toast.LENGTH_SHORT).show();
	    		}
	    		else {
	    			Toast.makeText(getApplicationContext(), "Username or Password is incorrect"+response, Toast.LENGTH_SHORT).show();
	    			hasErrors = false;
	    		}
			}
			catch(Exception e){
				Toast.makeText(getApplicationContext(), "Network Errors", Toast.LENGTH_SHORT).show();
				hasErrors = true;
			}
			if(hasErrors){
				
			}
			else{			
				final Account account = new Account(username, accountType);
				accMgr.addAccountExplicitly(account, password, null);
				final Intent intent = new Intent();
				intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
				intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
				intent.putExtra(AccountManager.KEY_AUTHTOKEN, accountType);
				this.setAccountAuthenticatorResult(intent.getExtras());
				this.setResult(RESULT_OK, intent);
				this.finish();
			}
		}
	}

}
