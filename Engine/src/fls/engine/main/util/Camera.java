package fls.engine.main.util;

public class Camera {

	public int w, h;
	public Point pos;

	public Camera(int w, int h) {
		this.pos = new Point(0,0);
		this.w = w;
		this.h = h; 
	}

	public void center(int cx, int cy) {
		this.pos.x = cx - (this.w / 2);
		this.pos.y = cy - (this.h / 2);
	}
}
