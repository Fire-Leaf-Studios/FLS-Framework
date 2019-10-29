package fls.engine.main.util.rendertools;

import fls.engine.main.util.Point;
import fls.engine.main.util.Renderer;

public class Sprite {
	
	private Point pos;
	private int w, h;
	private int[] bitmapData;
	
	
	
	
	public Sprite(int x, int y) {
		this.pos = new Point(x, y);
		this.w = 0;
		this.h = 0;
	}
	
	public void setBitmapData(int[] data) {
		this.bitmapData = data;
	}
	
	public void setWidthHeight(int w, int h) {
		this.w = w;
		this.h = h;
	}
	
	public void update() {} ;
	public void render(Renderer r) {} ;
	
	public Point getPos() {
		return this.pos;
	}
	
	public int getWidth() {
		return this.w;
	}
	
	public int getHeight() {
		return this.h;
	}
	
	public int[] getBitmapData() {
		return this.bitmapData;
	}
}
