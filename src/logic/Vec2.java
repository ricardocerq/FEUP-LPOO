package logic;

import java.io.Serializable;
import java.util.Random;

/**
 *	represents a 2D vector
 */
public class Vec2 implements Serializable{

	private static Random r= new Random();
	private static final long serialVersionUID = 5461L;
	/**
	 * get the euclidean distance between two points
	 * @param x1 x coordinate of 1st point
	 * @param y1 y coordinate of 1st point
	 * @param x2 x coordinate of 2nd point
	 * @param y2 y coordinate of 2nd point
	 * @return distance between the points
	 */
	public static float getDist(float x1, float y1, float x2, float y2){
		return (float)Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	
	/**
	 * get the euclidean distance between two points
	 * @param v1 first point
	 * @param v2 second point
	 * @return distance between the points
	 */
	public static float getDist(Vec2 v1, Vec2 v2){
		return getDist(v1.getX(), v1.getY(), v2.getX(), v2.getY());
	}
	/**
	 * get a random vector
	 * x : [-1,1]
	 * y : [-1,1]
	 * @return random vector
	 */
	public static Vec2 randomVec() {
		return new Vec2(r.nextFloat()*2 - 1, r.nextFloat() * 2 - 1);
	}
	private float x, y;
	/**
	 * default constructor, initializes to (0,0)
	 */
	public Vec2(){
		this(0,0);
	}
	/**
	 * constructor
	 * @param x x coordinate of vector
	 * @param y y coordinate of vector
	 */
	public Vec2(float x, float y){
		this.x = x;
		this.y = y;
	}
	/**
	 * add two vectors
	 * @param v2 second vector
	 * @return resulting vector
	 */
	public Vec2 add(Vec2 v2){
		return new Vec2(x+ v2.x , y + v2.y);
	}
	/**
	 * set this vector to the addition of it and another
	 * @param v2 second vector
	 * @return this vector
	 */
	public Vec2 addSet(Vec2 v2){
		x = x+ v2.x ;
		y=  y+ v2.y;
		return this;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Vec2 clone(){
		return new Vec2(x, y);
	}
	/**
	 * return the dot product between two vectors
	 * @param v2 second vector
	 * @return result of dot product
	 */
	public float dot(Vec2 v2){
		return x *v2.x +  y * v2.y;
	}
	/**
	 * get a vector with the same direction as this, but with a magnitude of 1
	 * @return normalized vector
	 */
	public Vec2 getNormalized(){
		Vec2 out = clone();
		out.normalize();
		return out;
	}
	/**
	 * get the x coordinate of this vector
	 * @return x coordinate of vector
	 */
	public float getX() {
		return x;
	}
	/**
	 * get the y coordinate of this vector
	 * @return y coordinate of vector
	 */
	public float getY() {
		return y;
	}
	/**
	 * calculate the magnitude of this vector
	 * @return magnitude of vector
	 */
	public float mag(){
		return (float)Math.sqrt(x*x + y*y);
	}
	
	/**
	 * calculate the square of the magnitude of this vector
	 * @return square of the magnitude of the vector
	 */
	public float magSquared(){
		return x*x + y*y;
	}
	
	/**
	 * set the magnitude of this vector to 1, mantaining its direction
	 * @return this vector
	 */
	public Vec2 normalize(){
		float mag = mag();
		x /= mag;
		y /= mag;
		return this;
	}
	/**
	 * multiply this vector by a scalar value
	 * @param scalar value to multiply
	 * @return resulting vector
	 */
	public Vec2 scalarMul(float scalar){
		return new Vec2(x*scalar , y * scalar);
	}
	/**
	 * set this vector to the result of multiplying it by a scalar value
	 * @param scalar value to multiply
	 * @return this vector
	 */
	public Vec2 scalarMulSet(float scalar){
		x = x*scalar;
		y = y*scalar;
		return this;
	}
	/**
	 * change the x coordinate of the vector
	 * @param x new x coordinate
	 */
	public void setX(float x) {
		this.x = x;
	}
	/**
	 * change the x and y coordinates of the vector
	 * @param x new x coordinate
	 * @param y new y coordinate
	 */
	public void setXY(float x, float y){
		this.x = x;
		this.y = y;
	}
	/**
	 * change the y coordinate of the vector
	 * @param y new y coordinate
	 */
	public void setY(float y) {
		this.y = y;
	}
	/**
	 * subtract this vector from another one
	 * @param v2 second vector
	 * @return resulting vector
	 */
	public Vec2 sub(Vec2 v2){
		return new Vec2(x - v2.x , y - v2.y);
	}
	/**
	 * set this vector to be the result of subtracting it from another one
	 * @param v2 second vector
	 * @return this vector
	 */
	public Vec2 subSet(Vec2 v2){
		x =x - v2.x ;
		y =y - v2.y;
		return this;
	}
}
