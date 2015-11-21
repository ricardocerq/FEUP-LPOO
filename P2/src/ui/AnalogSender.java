package ui;

import logic.Vec2;
import network.CommunicationManager;
import android.annotation.SuppressLint;
import android.os.Vibrator;
import android.util.Log;

public class AnalogSender implements AnalogStickListener, AnalogButtonListener{
	private static AnalogSender instance = null;
	private AnalogSender(){};
	public static AnalogSender getInstance(){
		if(instance == null)
			instance = new AnalogSender();
		return instance;
	}
	
	private Vibrator vib;
	private Vec2 vec = new Vec2();
	private int vibTime = 1;
	private final String TAG = "AnalogSender";
	
	public void setVib(Vibrator vib){
		this.vib = vib;
	}
	
	@Override
	public void onReleased(AnalogStick stick) {
		//Log.d(TAG, "Stick: Released");
		vib.vibrate(vibTime);
		vec.setXY((float)0, (float)0);
		CommunicationManager.getInstance().transmit(-1, vec);
	}
	
	@Override
	public void onMoved(AnalogStick stick, double x, double y) {
		vec.setXY((float)x, (float)y);
		CommunicationManager.getInstance().transmit(-1,vec);
	}
	
	@SuppressLint("DefaultLocale")
	@Override
	public void onButtonDown(AnalogButton button) {
		Log.d(TAG, button.getText() + ": pressed");
		vib.vibrate(vibTime);
		CommunicationManager.getInstance().transmit(-1,button.getText().toLowerCase());
	}
	
	@Override
	public void onButtonUp(AnalogButton button) {
		Log.d(TAG, button.getText() + ": released");
		CommunicationManager.getInstance().transmit(-1,button.getText());
	}
	public void setVibTime(int time){
		vibTime = time;
	}
}

