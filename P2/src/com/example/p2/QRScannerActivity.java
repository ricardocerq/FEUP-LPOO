package com.example.p2;
import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import network.CommunicationManager;

import com.dm.zbar.android.scanner.CameraPreview;
import com.dm.zbar.android.scanner.ZBarConstants;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class QRScannerActivity extends Activity{
	static {
	    System.loadLibrary("iconv");
	}
	
	private Camera mCamera;  
	private CameraPreview mPreview;  
	private Handler autoFocusHandler; 
	FrameLayout preview;

	ImageScanner scanner;    
	private boolean previewing = true;  

	public void onCreate(Bundle savedInstanceState)   
	{  
		setContentView(R.layout.activity_scanner); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		previewCb = new PreviewCallback()   
			{  
				public void onPreviewFrame(byte[] data, Camera camera)   
				{  
					Camera.Parameters parameters = camera.getParameters();  
					Size size = parameters.getPreviewSize();  
					Image barcode = new Image(size.width, size.height, "Y800");  
					barcode.setData(data);  
					int result = scanner.scanImage(barcode);
					//Log.d("received","yay1");
					if (result != 0 )   
					{  
						Log.d("received","yay1");
						if(scanner.getResults().size() != 0){
							previewing = false;  
							mCamera.setPreviewCallback(null);  
							mCamera.stopPreview();  
							SymbolSet syms = scanner.getResults();  
							for (Symbol sym : syms)   
							{  
								Log.d("received", sym.getData());
								Intent returnIntent = new Intent();  
								returnIntent.putExtra("CODE", sym.getData());  
								setResult(Activity.RESULT_OK,returnIntent);
								releaseCamera();  
								finish();
								
								CommunicationManager.getInstance().receiveConnectionInfo(sym.getData());
								Toast toast = Toast.makeText(QRScannerActivity.this, "Scanned : " + sym.getData(), Toast.LENGTH_SHORT);
								toast.show();
								
								Intent i = new Intent(QRScannerActivity.this, ControllerActivity.class);
								QRScannerActivity.this.startActivity(i);
								MainActivity.enabled = true;
								Log.d("received", ": " + sym.getData());
								
							}  
						}
						
					}
				}  
			};  
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		autoFocusHandler = new Handler();  
		//mCamera = getCameraInstance();  

		
		scanner = new ImageScanner();  
		scanner.setConfig(0, Config.X_DENSITY, 1);  
		scanner.setConfig(0, Config.Y_DENSITY, 1);  
		scanner.setConfig(0, Config.ENABLE, 0);  
		scanner.setConfig(Symbol.QRCODE, Config.ENABLE,1); 

		mPreview = new CameraPreview(this, previewCb, autoFocusCB);  
		//mPreview.setCamera(mCamera);
		preview = (FrameLayout)findViewById(R.id.cameraPreview1);  
		preview.addView(mPreview);  

		super.onCreate(savedInstanceState);  
	}  

	public static Camera getCameraInstance()  
	{  
		Camera c = null;  
		try   
		{  
			c = Camera.open();  
		} catch (Exception e)  {  
		}  
		return c;  
	}  
	
	/*private void releaseCamera()   
	{  
		if (mCamera != null)   
		{  
			previewing = false;  
			mCamera.setPreviewCallback(null);  
			mCamera.release();  
			mCamera = null;  
		}  
	} */

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			preview.removeView(mPreview);
			mPreview.getHolder().removeCallback(mPreview);
			mPreview.hideSurfaceView();
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}
	PreviewCallback previewCb;

	// Mimic continuous auto-focusing  
	AutoFocusCallback autoFocusCB = new AutoFocusCallback()   
	{  
		public void onAutoFocus(boolean success, Camera camera)   
		{  
			autoFocusHandler.postDelayed(doAutoFocus, 1000);  
		}  
	};  

	private Runnable doAutoFocus = new Runnable()   
	{  
		public void run()   
		{  
			if (previewing)  
				mCamera.autoFocus(autoFocusCB);  
		}  
	};  
	@Override
    protected void onResume() {
        super.onResume();

        // Open the default i.e. the first rear facing camera.
        mCamera = Camera.open();
        if(mCamera == null) {
            // Cancel request if mCamera is null.
            cancelRequest();
            return;
        }

        mPreview.setCamera(mCamera);
        mPreview.showSurfaceView();
        previewing = true;
    }
	@Override
	protected void onPause() {
		super.onPause();

		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		/*if (mCamera != null) {
			previewing = false;
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
		
			//FrameLayout preview2 = (FrameLayout)findViewById(R.id.cameraPreview1);  
			preview.removeView(mPreview); 
			mPreview.getHolder().removeCallback(mPreview);
			
			
			//mPreview.setCamera(null);
			//mCamera.cancelAutoFocus();
			//mCamera.setPreviewCallback(null);

			// According to Jason Kuang on http://stackoverflow.com/questions/6519120/how-to-recover-camera-preview-from-sleep,
			// there might be surface recreation problems when the device goes to sleep. So lets just hide it and
			// recreate on resume
			

			mCamera.release();
			mCamera = null;
		}*/
		releaseCamera();
	}
	public void cancelRequest() {
		Intent dataIntent = new Intent();
		dataIntent.putExtra(ZBarConstants.ERROR_INFO, "Camera unavailable");
		setResult(Activity.RESULT_CANCELED, dataIntent);
		finish();
	}
	@Override  
	public void onBackPressed() { 
		releaseCamera();  
		Intent intent = new Intent();  
		intent.putExtra("BARCODE","NULL");  
		setResult(RESULT_OK, intent);  
		super.onBackPressed();  
	} 
}
