package com.example.p2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	static boolean enabled;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//addPreferencesFromResource(R.xml.preferences);    
		
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Button scanButton = (Button) findViewById(R.id.scanButton);
		
		Button helpButton = (Button) findViewById(R.id.helpButton);
		Button quitButton = (Button) findViewById(R.id.quitButton);
		
		scanButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, QRScannerActivity.class);
				i.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(i, 1);
			}
		});
		helpButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(enabled){
				Intent i = new Intent(MainActivity.this, ControllerActivity.class);
				MainActivity.this.startActivity(i);
				}
				else{
					Toast toast = Toast.makeText(MainActivity.this, "You have to scan the code first" , Toast.LENGTH_SHORT);
				toast.show();
				}
			}
		});
		quitButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	@Override 
	protected void onDestroy(){
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*menu.add(Menu.NONE, 0, 0, "Show current settings");
		return super.onCreateOptionsMenu(menu);
		*/
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*switch (item.getItemId()) {
		case 0:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return false;*/
		
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		/*boolean success = false;
		Log.d("received", "some");
		if(requestCode == 1){
			Log.d("received", "some1");
			if(resultCode == Activity.RESULT_OK){
				Log.d("received", "some2");
				String code = data.getStringExtra("CODE");
				if(code == null || code.equals("NULL")){
					Log.d("received", "nothing");
				}else{
					try{
						Log.d("received", "some3");
						success = true;
						CommunicationManager.getInstance().receiveConnectionInfo(code);
						Toast toast = Toast.makeText(this, "Scanned : " + code, Toast.LENGTH_SHORT);
						toast.show();
						
						//SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
						//int num = 1;
						//if(!sharedPrefs.getBoolean("enable", false)){
						//	num = 0;
						//}
						//else num = sharedPrefs.getInt("hap_time", 1);
						//AnalogSender.getInstance().setVibTime(num);
						
						
						Intent i = new Intent(MainActivity.this, ControllerActivity.class);
						this.startActivity(i);
						Log.d("received", ": " + code);
						
					}catch(Exception e)
					{
						Log.d("received", "excep");
					}
				}
			}
		}
		if(!success){
			Toast toast = Toast.makeText(this, "Invalid Code!", Toast.LENGTH_SHORT);
			toast.show();
		}*/
	}
	
}
