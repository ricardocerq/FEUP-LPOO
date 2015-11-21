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
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("ClickableViewAccessibility")
public class AnalogStick extends View {
	
	private double topX, topY;
	private double lastX, lastY;
	private boolean active;
	private final double BASE_RADIUS_AMOUNT = .4;
	private final double TOP_RADIUS_AMOUNT= .2;
	private int baseRadius;
	private int topRadius;
	private int centerX, centerY;
	private List<AnalogStickListener> listeners = new LinkedList<AnalogStickListener>();
	private Bitmap base;
	private Bitmap top;
	private Bitmap stick;
	private Rect drawDst = new Rect();
	private double forceX = 0;
	private double forceY = 0;
	private double vX = 0;
	private double vY = 0;
	private double k = .001;
	private double c = .01;
	private Paint shadowPaint;
	private Handler animationHandler = new Handler();
	private Matrix rotationMatrix = new Matrix();
	private void resetSimValues(){
		forceX = 0;
		forceY = 0;
		vX = 0;
		vY = 0;
		if(!active){
			topX = 0;
			topY = 0;
		}
	}
	private void cancelAnimation(){
		animationHandler.removeCallbacksAndMessages(null);
		resetSimValues();
	}
	
	public AnalogStick(Context context) {
		this(context, null, 0);
	}

	public AnalogStick(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AnalogStick(Context context, AttributeSet attrs, int defStyle) {
		super (context, attrs, defStyle);
		init();
	}
	
	
	private void init() {
		setFocusable(true);
		initBitmaps();
	}
	private void updateValues(int width, int height){
		int dim = Math.min(width, width);
		baseRadius = (int)(BASE_RADIUS_AMOUNT * dim);
		topRadius = (int) (TOP_RADIUS_AMOUNT * dim);
		centerX = width/2;
		centerY = height/2;
	}
	private void initBitmaps(){
		base = BitmapFactory.decodeResource(getResources(), R.drawable.base);
		top =  BitmapFactory.decodeResource(getResources(), R.drawable.top);
		stick =  BitmapFactory.decodeResource(getResources(), R.drawable.stick);
		shadowPaint = new Paint();
		shadowPaint.setColor(Color.DKGRAY);
		shadowPaint.setAntiAlias(true);
		shadowPaint.setAlpha(30);
	}

	public void addAnalogStickListener(AnalogStickListener listener) {
		listeners.add(listener);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
		updateValues(parentWidth, parentHeight);
		this.setMeasuredDimension(parentWidth, parentHeight);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		updateValues(getMeasuredWidth(), getMeasuredHeight());
		drawDst.set(centerX - baseRadius, centerY - baseRadius,centerX + baseRadius,  centerY + baseRadius);
		canvas.drawBitmap(base, null, drawDst, null);

		rotationMatrix.reset();
		rotationMatrix.postTranslate(-stick.getWidth() / 2, -stick.getHeight() / 2);
		rotationMatrix.postScale(1.425f, 1.425f);
		double angle =  Math.acos(topX / Math.sqrt(topX * topX + topY * topY));
		if(topY < 0)
			angle = (2* Math.PI - angle);
		angle = (angle+Math.PI/2)*180/Math.PI ;
		rotationMatrix.postRotate((float)angle);
		rotationMatrix.postTranslate(drawDst.exactCenterX(), drawDst.exactCenterY());
		canvas.drawBitmap(stick, rotationMatrix, null);

		float offset = (float)(topRadius *.25);
		canvas.drawCircle((float)(centerX + topX + offset), (float)(centerY + topY + offset*1.5), topRadius * .95f, shadowPaint);

		drawDst.set((int)(centerX + topX - topRadius),(int)(centerY + topY - topRadius),(int)(centerX + topX + topRadius),(int)( centerY + topY + topRadius));
		canvas.drawBitmap(top, null, drawDst, null);
		canvas.save();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if( event.getAction() == MotionEvent.ACTION_DOWN){
			if(event.getX() >= centerX-topRadius && event.getX() <= centerX + topRadius && event.getY() >= centerY-topRadius && event.getY() <= centerY + topRadius){
				active = true;
				lastX = event.getX();
				lastY = event.getY();
				cancelAnimation();
			}
		}
		else if ( event.getAction() == MotionEvent.ACTION_MOVE && active) {
			double tempX = topX + event.getX() - lastX;
			double tempY = topY + event.getY() - lastY;
			double dist = Math.sqrt((tempX)*(tempX)+(tempY)*(tempY));
			if( dist <= (baseRadius - topRadius)){
				topX = tempX;
				topY = tempY;
			}else{
				topX = tempX * (baseRadius - topRadius)/dist;
				topY = tempY * (baseRadius - topRadius)/dist;
			}

			lastX = event.getX();
			lastY = event.getY();
			invalidate();
			notifyMoved(topX/(baseRadius - topRadius), topY/(baseRadius - topRadius));
		} else if ( event.getAction() == MotionEvent.ACTION_UP && active) {
			active = false;
			notifyReleased();
			returnTop();
		}
		return true;
	}

	private void returnTop() {

		int timeInterval = 1000;
		int framesPerSecond = 60;
		final double millisecondsPerFrame = 1000/(double)framesPerSecond;
		int numberOfFrames = framesPerSecond*timeInterval/1000;
		for (int i = 0; i < numberOfFrames-1; i++) {
			animationHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if(!active){
						forceX = -k * topX - c * vX;
						forceY = -k * topY - c * vY;
						vX += millisecondsPerFrame * forceX;
						vY += millisecondsPerFrame * forceY;
						topX += vX * millisecondsPerFrame;
						topY += vY * millisecondsPerFrame;
						invalidate();
					}
				}
			}, timeInterval*i/numberOfFrames);
		}
		animationHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if(!active){
					resetSimValues();
					invalidate();
				}
			}
		}, timeInterval);

	}

	private void notifyMoved(double x, double y){
		for(AnalogStickListener l : listeners)
			l.onMoved(this,x, y);
	}

	private void notifyReleased(){
		for(AnalogStickListener l : listeners)
			l.onReleased(this);
	}
}