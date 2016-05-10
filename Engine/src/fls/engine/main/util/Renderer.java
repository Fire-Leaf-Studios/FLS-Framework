package fls.engine.main.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Renderer {

	
	private BufferedImage img;
	
	private int[] pixles;
	private boolean[] dirty;
	private int w,h;
	
	private int xOff, yOff;
	
	public Renderer(BufferedImage img){
		this.img = img;
		this.w = img.getWidth();
		this.h = img.getHeight();
		
		this.pixles = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		this.dirty = new boolean[h];
		
		setOffset(0, 0);
	}
	
	public void finaliseRender(){
		for(int y = 0; y < this.h; y++){
			if(this.dirty[y]){
				int[] row = getPixels(y);
				img.setRGB(0, y, w, 1, row, 0, w);
			}
		}
		
		reset();
	}
	
	private int[] getPixels(int y){
		int[] res = new int[this.w];
		for(int i = 0; i < this.w; i++){
			res[i] = this.pixles[i + y * this.w];
		}
		return res;
	}
	
	public void setPixel(int x,int y,int c){
		x += this.xOff;
		y += this.yOff;
		if(!isValid(x, y))return;
		if(this.pixles[x + y * this.w] == c || c < 0) return;
		this.pixles[x + y * this.w] = c;
		this.dirty[y] = true;
	}
	
	public void renderSection(int[] data,int x,int y, int xs){
		for(int i = 0; i < xs * xs; i++){
			int xx = i % xs;
			int yy = i / xs;
			setPixel(x + xx,y + yy, data[xx + yy * xs]);
		}
	}
	
	public void shade(int x,int y){
		x += this.xOff;
		y += this.yOff;
		if(!isValid(x, y))return;
		int c = this.pixles[x + y * this.w];
		int r = (c >> 16) & 0xFF;
		int g = (c >> 8) & 0xFF;
		int b = (c) & 0xFF;
		int rgb = getShadedColor(r, g, b, 0.7f);
		setPixel(x - this.xOff, y - this.yOff, rgb);
	}
	
	public void lighten(int x, int y){
		x += this.xOff;
		y += this.yOff;
		if(!isValid(x, y))return;
		int c = this.pixles[x + y * this.w];
		int r = (c >> 16) & 0xFF;
		int g = (c >> 8) & 0xFF;
		int b = (c) & 0xFF;
		int rgb = getShadedColor(r, g, b, 1/0.7f);
		setPixel(x - this.xOff, y - this.yOff, rgb);
	}
	
	public int getShadedColor(int r,int g,int b, float amt){
		r *= amt;
		g *= amt;
		b *= amt;
		
		if(r < 0)r = 0;
		if(g < 0)g = 0;
		if(b < 0)b = 0;
		return makeRGB(r,g,b);
	}
	
	private boolean isValid(int x, int y){
		if(x < 0 || y < 0 || x >= w || y >= h)return false;
		else return true;
	}
	
	public int makeRGB(int r, int g, int b){
		if(r > 255)r = 255;
		if(g > 255)g = 255;
		if(b > 255)b = 255;
		return (r << 16) | (g << 8) | b;
	}
	
	private void reset(){
		for(int i = 0; i < this.dirty.length; i++){
			this.dirty[i] = false;
		}
	}
	
	public void fill(int c){
		Arrays.fill(pixles, c);
	}
	
	public void checkerBoard(int x, int y, int w, int h){
		int r = 117;
		int g = 163;
		int b = 0;
		checkerBoard(x,y,w,h,r,g,b,0.7f);
	}
	
	public void checkerBoard(int x,int y,int w, int h, int r, int g, int b, float amt){
		int rgb = makeRGB(r,g,b);
		int shade = getShadedColor(r,g,b, amt);
		int size = 20;
		x = x/size;
		y = y/size*2;
		for(int xx = x; xx <= x + w + 1; xx ++){
			for(int yy = y; yy <= y + h/2 + 1; yy ++){
				boolean sh = (xx + yy) % 2 == 1;
				for(int i = 0; i < size * (size/2); i++){
					int dx = (xx * size) + (i % size);
					int dy = (yy * size/2) + (i / size);
					setPixel(dx, dy, sh?shade:rgb);
				}
			}
		}
	}
	
	public void shadeCircle(int x,int y,int r){
		
		int top = y - r;
		int left = x - r;
		
		for(int dx = 0; dx < r * 2; dx++){
			for(int dy = 0; dy < r * 2; dy++){
				
				int px = left + dx;
				int py = top + dy;
				
				int tx = px - x;
				int ty = py - y;
				
				if((tx * tx) + (ty * ty) < (r * r)-16)shade(px,py);
			}
		}
		
	}
	
	public void shadeElipse(int x,int y, int w, int h){
		w += 3;
		x ++;
		for(int yy = -h+1; yy < h; yy++){
			for(int xx = -w ; xx <= w; xx++){
				if(xx * xx * h * h + yy * yy * w * w <= h * h * w * w){
					int dx = (x) + xx;
					shade(dx, y + yy);
				}
			}
		}
	}
	
	public void lightenElipse(int x,int y, int w, int h){
		w += 3;
		x ++;
		for(int yy = -h+1; yy < h; yy++){
			for(int xx = -w ; xx <= w; xx++){
				if(xx * xx * h * h + yy * yy * w * w <= h * h * w * w){
					int dx = (x) + xx;
					lighten(dx, y + yy);
				}
			}
		}
	}
	
	public void setOffset(int xo,int yo){
		this.xOff = xo;
		this.yOff = yo;
	}
	
	public int getWidth(){
		return this.w;
	}
	
	public int getHeight(){
		return this.h;
	}
	
	public boolean isOnScreen(int x,int y){
		return isValid(x + xOff, y + yOff);
	}
	
	public void drawCircle(int x,int y,int r, int c){
		for(int xx = -r; xx < r; xx++){
			for(int yy = -r; yy < r; yy++){
				
				
				int x2 = xx * xx;
				int y2 = yy * yy;
				
				if(x2 + y2 < r * r){
					this.setPixel(x + xx, y + yy, c==-1?makeRGB(255,0,0):c);
				}
			}
		}
	}
	
	public int getColor(int x,int y){
		if(!isValid(x,y))return -1;
		return this.pixles[x + y * this.w];
	}
}
