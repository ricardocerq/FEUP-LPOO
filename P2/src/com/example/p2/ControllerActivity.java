package com.example.p2;

import java.io.IOException;

import network.CommunicationManager;
import ui.AnalogButton;
import ui.AnalogSender;
import ui.AnalogStick;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ControllerActivity extends Activity{
	
	private EditText receivedTextField;
	private Button sendButton;
	private AnalogStick analog;
	private Vibrator myVib;
	private AnalogSender analogSender;
	@SuppressLint("InlinedApi")
	private void hideSystemUI() {
		Log.d("main", "" + android.os.Build.VERSION.SDK_INT);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		if(android.os.Build.VERSION.SDK_INT >= 19) 
		getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.INVISIBLE);
		//else getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

	}
	private void showSystemUI() {
		if(android.os.Build.VERSION.SDK_INT >= 16) 
		getWindow().getDecorView().setSystemUiVisibility(
	            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		//else getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("...");
		System.out.println("...");
		System.out.println("...");
		System.out.println("...");
		System.out.println("Starting...");
		System.out.println("...");
		System.out.println("...");
		System.out.println("...");
		System.out.println("...");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


		setContentView(R.layout.activity_controller);
		myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		
		receivedTextField = (EditText) findViewById(R.id.receivedText);
		receivedTextField.setVisibility(0);
		initController();
		sendButton = (Button) findViewById(R.id.Send);
		
		sendButton.setVisibility(View.INVISIBLE);
		receivedTextField.setVisibility(View.INVISIBLE);
		CommunicationManager.setIsServer(false);
	}
	private void initController() {
		analogSender = AnalogSender.getInstance();
		analogSender.setVib(myVib);
		
		analog = (AnalogStick) findViewById(R.id.analogStick1);
		analog.setHapticFeedbackEnabled(true);
		analog.addAnalogStickListener(analogSender);
		
		
		AnalogButton buttonA = (AnalogButton) findViewById(R.id.analogButtonA);
		buttonA.setHapticFeedbackEnabled(true);
		buttonA.setDisplayText("A");
		buttonA.addAnalogButtonListener(analogSender);
		
		
		AnalogButton buttonB = (AnalogButton) findViewById(R.id.analogButtonB);
		buttonB.setHapticFeedbackEnabled(true);
		buttonB.setDisplayText("B");
		buttonB.addAnalogButtonListener(analogSender);
		
		AnalogButton buttonX = (AnalogButton) findViewById(R.id.analogButtonX);
		buttonX.setHapticFeedbackEnabled(true);
		buttonX.setDisplayText("X");
		buttonX.addAnalogButtonListener(analogSender);
		
		AnalogButton buttonY = (AnalogButton) findViewById(R.id.analogButtonY);
		buttonY.setHapticFeedbackEnabled(true);
		buttonY.setDisplayText("Y");
		buttonY.addAnalogButtonListener(analogSender);
	}
	@Override
	protected void onStop(){
		super.onStop();
		showSystemUI();
		CommunicationManager man = CommunicationManager.getInstance();
		if(man != null)
			try {
				man.closeAll();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	WakeLock wakeLock;
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume(){
		super.onResume();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		int num = 1;
		if(!sharedPrefs.getBoolean("enable", true))
			num = 0;
		else num = Integer.parseInt(sharedPrefs.getString("hap_time", "1"));
		analogSender.setVibTime(num);
		hideSystemUI();
		CommunicationManager man = CommunicationManager.getInstance();
		if(man != null)
			try {
				man.openAll();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		PowerManager powerManager = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
		wakeLock.acquire();
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		wakeLock.release();
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
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
	
}