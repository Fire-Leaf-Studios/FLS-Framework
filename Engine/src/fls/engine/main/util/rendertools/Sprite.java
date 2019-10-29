package fls.engine.main.util.rendertools;

import fls.engine.main.util.Point;
import fls.engine.main.util.Renderer;

/**
 * A class used to store position and graphical data for easy rendering
 * @author h2n0
 *
 */
public class Sprite {
	
	private Point pos;
	private int w, h;
	private int[] bitmapData;
	
	/**
	 * Create a Sprite that's easy to move and render
	 * @param x initial x position
	 * @param y initial y position
	 */
	public Sprite(int x, int y) {
		this.pos = new Point(x, y);
		this.w = 0;
		this.h = 0;
	}
	
	/**
	 * Set the graphic data for this sprite
	 * @param data - from {@link ImageParser} or {@link SpriteParser}
	 */
	public void setBitmapData(int[] data) {
		this.bitmapData = data;
	}
	
	/**
	 * Set the width and height of the sprite
	 * @param w new width
	 * @param h new height
	 */
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
