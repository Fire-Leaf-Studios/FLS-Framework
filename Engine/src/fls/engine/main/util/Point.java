package fls.engine.main.util;

public class Point {

	public float x,y;
	
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
}
