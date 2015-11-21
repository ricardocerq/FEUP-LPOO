package logic;

import java.io.Serializable;


public class Vec2 implements Serializable{
	private static final long serialVersionUID = 5461L;
	
	private float x, y;
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setXY(float x, float y){
		this.x = x;
		this.y = y;
	}
	public Vec2(float x, float y){
		this.x = x;
		this.y = y;
	}
	public Vec2(){
		this(0,0);
	}
	public Vec2 add(Vec2 v2){
		return new Vec2(x+ v2.x , y + v2.y);
	}
	public Vec2 sub(Vec2 v2){
		return new Vec2(x - v2.x , y - v2.y);
	}
	public Vec2 scalarMul(float scalar){
		return new Vec2(x*scalar , y * scalar);
	}
	public Vec2 addSet(Vec2 v2){
		x = x+ v2.x ;
		y=  y+ v2.y;
		return this;
	}
	public Vec2 subSet(Vec2 v2){
		x =x - v2.x ;
		y =y - v2.y;
		return this;
	}
	public Vec2 scalarMulSet(float scalar){
		x = x*scalar;
		y = y*scalar;
		return this;
	}
	
	public float dot(Vec2 v2){
		return x *v2.x +  y * v2.y;
	}
	
	public float mag(){
		return (float)Math.sqrt(x*x + y*y);
	}
	public float magSquared(){
		return x*x + y*y;
	}
	public Vec2 getNormalized(){
		Vec2 out = clone();
		out.normalize();
		return out;
	}
	public Vec2 clone(){
		return new Vec2(x, y);
	}
	public Vec2 normalize(){
		float mag = mag();
		x /= mag;
		y /= mag;
		return this;
	}
	public static float getDist(float x1, float y1, float x2, float y2){
		return (float)Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	public static float getDist(Vec2 v1, Vec2 v2){
		return getDist(v1.getX(), v1.getX(), v2.getX(), v2.getY());
	}
}
