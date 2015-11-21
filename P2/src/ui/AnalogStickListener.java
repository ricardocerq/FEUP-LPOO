package ui;

public interface AnalogStickListener{
	public void onReleased(AnalogStick stick);
	public void onMoved(AnalogStick stick, double x, double y);
}
