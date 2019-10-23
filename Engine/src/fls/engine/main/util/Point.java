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
	
	public int dist(Point p){
		int xx = p.getIX() - this.getIX();
		int yy = p.getIY() - this.getIY();
		int dist = (xx * xx) + (yy * yy);
		return dist;
	}
	
	public float getAngle(Point p) {
		int xx = p.getIX() - this.getIX();
		int yy = p.getIY() - this.getIY();
		float rad = (float)Math.atan2(yy, xx);
		return rad;
	}
	
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
