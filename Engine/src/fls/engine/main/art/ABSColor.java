package fls.engine.main.art;

import java.awt.Color;

public class ABSColor {

	public final int r,g,b;
	public final int rgb;
	
	public static final ABSColor white = new ABSColor(255,255,255);
	public static final ABSColor black = new ABSColor(0, 0, 0);
	public static final ABSColor gray = new ABSColor(0xAA);
	public static final ABSColor lightGray = new ABSColor(0xDD);
	public static final ABSColor darkGray = new ABSColor(0x77);
	public static final ABSColor hotPink = new ABSColor(255,0,255);
	public static final ABSColor red = new ABSColor(255, 0, 0);
	public static final ABSColor green = new ABSColor(0, 255, 0);
	public static final ABSColor blue = new ABSColor(0, 0, 255);
	
	public ABSColor(int a,int r,int g,int b){
		this.r = r;
		this.g = g;
		this.b = b;
		
		this.rgb = (a << 24) + (r << 16) + (g << 8) + (b);
	}
	
	public ABSColor(int c){
		this(255,c, c, c);
	}
	
	public ABSColor(int r,int g,int b){
		this(255,r,g,b);
	}
	
	public int getRGB(){
		return this.rgb;
	}
	
	public int getR(){
		return this.r;
	}
	
	public int getG(){
		return this.g;
	}
	
	public int getB(){
		return this.b;
	}
	
	public Color getRegColor(){
		return new Color(this.r,this.g,this.b);
	}
	
	public boolean isTheSame(ABSColor comp){
		return false;
	}
}
