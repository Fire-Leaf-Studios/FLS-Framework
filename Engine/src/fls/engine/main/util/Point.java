package fls.engine.main.util;

public class Point{

	public float x,y;
	
	public static Point zero = new Point(0,0);
	
	public Point(float x,float y){
		this.x = x;
		this.y = y;
	}
	
	public int getIX(){
		return (int)x;
	}
	
	public int getIY(){
		return (int)y;
	}
	
	/**
	 * Calculate the squared distance to another Point
	 * @param p - Point we want to measure against
	 * @return distance squared
	 */
	public int dist(Point p){
		int xx = p.getIX() - this.getIX();
		int yy = p.getIY() - this.getIY();
		int dist = (xx * xx) + (yy * yy);
		return dist;
	}
	
	/**
	 * Returns the angle between a point in radians
	 * @param p - Point we want the angle to
	 * @return the angle to P in radians
	 */
	
	public float getAngle(Point p) {
		int xx = p.getIX() - this.getIX();
		int yy = p.getIY() - this.getIY();
		float rad = (float)Math.atan2(yy, xx);
		return rad;
	}
	
	/**
	 * Returns an offset angle in degrees
	 * @param p - Point we want the angle to
	 * @return the angle to P in degrees (0deg is north and 180deg is south)
	 */
	public float getAngleDegrees(Point p) {
		float deg = (float)Math.toDegrees(this.getAngle(p)) + 90;
		deg %= 360;
		while(deg < 0)deg += 360;
		return deg;
	}
	
	public void setPos(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "X: " + this.x + ", Y: " + this.y;
	}
}
