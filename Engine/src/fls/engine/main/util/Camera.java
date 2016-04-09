package fls.engine.main.util;

import fls.engine.main.util.Point;

public class Camera {

	public int w, h;
	public int ww,wh;
	public Point pos;

	public Camera(int w, int h) {
		this.pos = new Point(0,0);
		this.w = w;
		this.h = h; 
	}

	public void center(int cx, int cy, int off) {
		this.pos.x = cx - (this.w / 2) + off;
		this.pos.y = cy - (this.h / 2) + off;
		
		if(this.pos.x < 0)this.pos.x = 0;
		if(this.pos.y < 0)this.pos.y = 0;
		if(this.pos.x + this.w > this.ww)this.pos.x = this.ww - this.w;
		if(this.pos.y + this.h > this.wh)this.pos.y = this.wh - this.h;
	}
	
	public void center(int cx,int cy){
		this.center(cx, cy,0);
	}
}
