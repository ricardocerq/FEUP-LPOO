package ui;

import java.util.LinkedList;
import java.util.List;
import com.example.p2.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ClickableViewAccessibility")
public class AnalogButton extends View {
	 	private String text = new String();
	    private boolean pressed;
	    private final double BASE_RADIUS_AMOUNT = .35;
	    private final double MIN_TOP_RESIZE = .95;
	    private final double MIN_TOP_POS = 0;
	    private final double MAX_TOP_POS = 1;
	    private double topPos = MAX_TOP_POS;
	    private int baseRadius;
	    private int centerX, centerY;
	    private List<AnalogButtonListener> listeners = new LinkedList<AnalogButtonListener>();
	    private Bitmap base;
	    //private Bitmap top;
	    private Rect drawDst = new Rect();
	    private double force = 0;
	    private double v = 0;
	    private double k = .001;
	    private double c = .01;
	    private double c2 = .0125;
	    private Paint shadowPaint;
	    private Paint topPaint;
	    private Paint textPaint;
	    private Handler animationHandler = new Handler();
	    private void resetSimValues(){
	    	//force = 0;
	        //v = 0;
	        //topPos = pressed ? MIN_TOP_POS : MAX_TOP_POS;
	    }
	    private void cancelAnimation(){
	    	animationHandler.removeCallbacksAndMessages(null);
	    	resetSimValues();
	    }
	    public AnalogButton(Context context) {
	        this(context, null, 0);
	    }

	    public AnalogButton(Context context, AttributeSet attrs) {
	        this(context, attrs, 0);
	    }
	    
	    public AnalogButton(Context context, AttributeSet attrs, int defStyle) {
	        super (context, attrs, defStyle);
	        initButtonView();
	    }
	    private void initButtonView() {
	        setFocusable(true);
	        initBitmaps();
	    }
	    public void setDisplayText(String t){
			text = t;
		}
	    private void updateValues(int width, int height){
	    	int dim = Math.min(width, width);
	    	baseRadius = (int)(BASE_RADIUS_AMOUNT * dim);
	        centerX = width/2;
	    	centerY = height/2;
	    }
	    private void initBitmaps(){
	    	base = BitmapFactory.decodeResource(getResources(), R.drawable.buttonbase);
	    	//top =  BitmapFactory.decodeResource(getResources(), R.drawable.top);
	    	shadowPaint = new Paint();
	    	shadowPaint.setColor(Color.DKGRAY);
	    	shadowPaint.setAntiAlias(true);
	    	shadowPaint.setAlpha(30);
	    	
	    	topPaint = new Paint();
	    	topPaint.setColor(Color.WHITE);
	    	topPaint.setAntiAlias(true);
	    	
	    	textPaint = new Paint();
	    	textPaint.setColor(Color.BLACK);
	    	textPaint.setAntiAlias(true);
	    	textPaint.setAlpha(150);
	    }
	    
	    

	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    	int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
	    	int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
	    	//int dim = Math.min(parentWidth, parentHeight);
	    	//baseRadius = (int)(BASE_RADIUS_AMOUNT * dim);
	        //topRadius = (int) (TOP_RADIUS_AMOUNT * dim);
	    	updateValues(parentWidth, parentHeight);
	    	this.setMeasuredDimension(parentWidth, parentHeight);
	    }

	    
	    @Override
	    protected void onDraw(Canvas canvas) {
	    	super.onDraw(canvas);
	    	updateValues(getMeasuredWidth(), getMeasuredHeight());
	    	
	    	float offset = (float)(baseRadius *.075);
			canvas.drawCircle((float)(centerX + offset), (float)(centerY + offset*1.5), baseRadius * .95f, shadowPaint);
	    	
	    	drawDst.set(centerX - baseRadius, centerY - baseRadius,centerX + baseRadius,  centerY + baseRadius);
	    	canvas.drawBitmap(base, null, drawDst, null);
	    	
	    	double topRadiusResize = (1-MIN_TOP_RESIZE)/(MAX_TOP_POS - MIN_TOP_POS)*topPos  + 1-(1-MIN_TOP_RESIZE)/(MAX_TOP_POS - MIN_TOP_POS)*MAX_TOP_POS;
	    	if(topRadiusResize > 1)
	    		topRadiusResize = 1;
	    	double topRadius = baseRadius * topRadiusResize * .83;
	    	
	    	//int topColor = (int)((MAX_COLOR-MIN_COLOR)*topPos/(MAX_TOP_POS - MIN_TOP_POS) + MAX_COLOR -(MAX_COLOR-MIN_COLOR)*MAX_TOP_POS/(MAX_TOP_POS - MIN_TOP_POS));
	    	//topPaint.setARGB(255, topColor, topColor, topColor);
	    	//if(pressed)
	    		//topPaint.setColor(Color.GRAY);
	    	//else topPaint.setColor(Color.WHITE);
	    	//if(topPos < MAX_TOP_POS)
	    	if(topPos <= (MAX_TOP_POS + MIN_TOP_POS)/2)
	    		offset = 0;
	    	else offset = (float) topRadius * .1f;
	    	//else offset = (float) MAX_TOP_POS * 20;
	    	
	    	if(offset < 0)
	    		offset = 0;
	    	canvas.drawCircle((float)(centerX + offset), (float)(centerY + offset*1.5), (float) (topRadius * .95f), shadowPaint);
	    	
	    	if(pressed)
	    		topPaint.setARGB(255, 225, 225, 225);
	    	else topPaint.setARGB(255, 255, 255, 255);
	    	
	    	canvas.drawCircle(centerX, centerY, (float)topRadius, topPaint);
	    	Typeface tf = Typeface.create("Helvetica",Typeface.BOLD);
	    	textPaint.setTypeface(tf);
	    	textPaint.setTextAlign(Align.CENTER);
	    	textPaint.setTextSize((float)topRadius);
	    	canvas.drawText(text, centerX, centerY + ((float)textPaint.getTextSize())/3, textPaint);
	    	canvas.save();
	    }

	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	    	if( event.getAction() == MotionEvent.ACTION_DOWN){
	    		if(event.getX() >= centerX - baseRadius && event.getX() <= centerX + baseRadius&& event.getY() >= centerY - baseRadius && event.getY() <= centerY + baseRadius){
	    			pressed = true;
	    			notifyButtonDown();
	    			buttonAnimation();
	    		}
	    		
	    	}else if(event.getAction() == MotionEvent.ACTION_UP){
	    		if(pressed){
	    			pressed = false;
	    			notifyButtonUp();
	    			buttonAnimation();
	    		}
	    		
	    	}
	    	return true;
	    }

	    private void buttonAnimation() {
	    	cancelAnimation();
	    	int timeInterval = 1000;
	    	int framesPerSecond = 60;
	    	final double millisecondsPerFrame = 1000/(double)framesPerSecond;
	    	int numberOfFrames = framesPerSecond*timeInterval/1000;
	    	final double oscCenter = pressed? MIN_TOP_POS : MAX_TOP_POS;
	    	final double localC = pressed? c2: c;
	    	for (int i = 0; i < numberOfFrames-1; i++) {
	    		animationHandler.postDelayed(new Runnable() {
	    			@Override
	    			public void run() {
	    				force = - k * (topPos - oscCenter) - localC * v;
	    				v += millisecondsPerFrame * force;
	    				topPos += v * millisecondsPerFrame;
	    				invalidate();

	    			}
	    		}, timeInterval*i/numberOfFrames);
	    	}
	    	animationHandler.postDelayed(new Runnable() {
	    		@Override
	    		public void run() {
	    			resetSimValues();
	    			invalidate();
	    		}
	    	}, timeInterval);
	    }
	    public void addAnalogButtonListener(AnalogButtonListener listener) {
		       listeners.add(listener);
		}
	    
	    private void notifyButtonDown(){
	    	for(AnalogButtonListener l : listeners)
	    		l.onButtonDown(this);
	    }
	    
	    private void notifyButtonUp(){
	    	for(AnalogButtonListener l : listeners)
	    		l.onButtonUp(this);
	    }
	    public String getText(){
	    	return text;
	    }
}
