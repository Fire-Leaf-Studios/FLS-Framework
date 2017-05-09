package fls.engine.main.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import fls.engine.main.Init;

public class Renderer {

	
	private BufferedImage img;
	
	protected int[] pixles;
	private boolean[] dirty;
	protected int w,h;
	
	public int scale;
	protected int xOff, yOff;
	
	public Renderer(Init i){
		if(!i.isCustomImage()){
			throw new IllegalStateException("Not using a custom draw surface!");
		}
		
		BufferedImage img = i.image;
		this.img = img;
		this.w = img.getWidth();
		this.h = img.getHeight();
		
		this.pixles = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		this.dirty = new boolean[h];
		setOffset(0, 0);
		setScale(1);
	}
	
	public void finaliseRender(){
		for(int y = 0; y < this.h; y++){
			if(this.dirty[y]){
				img.setRGB(0, y, w, 1, getPixels(y), 0, w);
				this.dirty[y] = false;
			}
		}
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
	
	public int getShadedColor(int col, float amt){
		int r = (col >> 16) & 0xFF;
		int g = (col >> 8) & 0xFF;
		int b = (col) & 0xFF;
		return getShadedColor(r, g, b, amt);
	}
	
	private boolean isValid(int x, int y){
		if(x < 0 || y < 0 || x >= w || y >= h)return false;
		else return true;
	}
	
	public int makeRGB(int r, int g, int b){
		r = clamp(r);
		g = clamp(g);
		b = clamp(b);
		return (r << 16) | (g << 8) | b;
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
		if(c == -1)c = makeRGB(255,0,0);
		for(int xx = -r; xx < r; xx++){
			for(int yy = -r; yy < r; yy++){
				int x2 = xx * xx;
				int y2 = yy * yy;			
				if(x2 + y2 < r * r){
					this.setPixel(x + xx, y + yy, c);
				}
			}
		}
	}
	
	public void drawLine(int x0, int y0, int x1, int y1, int c){
		 int w = x1 - x0;
		    int h = y1 - y0;
		    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
		    if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
		    if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
		    if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
		    int longest = Math.abs(w) ;
		    int shortest = Math.abs(h) ;
		    if (!(longest>shortest)) {
		        longest = Math.abs(h) ;
		        shortest = Math.abs(w) ;
		        if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
		        dx2 = 0 ;            
		    }
		    int numerator = longest >> 1 ;
		    for (int i=0;i<=longest;i++) {
		        setPixel(x0,y0,c) ;
		        numerator += shortest ;
		        if (!(numerator<longest)) {
		            numerator -= longest ;
		            x0 += dx1 ;
		            y0 += dy1 ;
		        } else {
		            x0 += dx2 ;
		            y0 += dy2 ;
		        }
		    }
	}
	
	public void drawLine(float x0, float y0, float x1, float y1, int c){
		this.drawLine((int)x0, (int)y0, (int)x1, (int)y1, c);
	}
	
	public int getColor(int x,int y){
		if(!isValid(x,y))return -1;
		return this.pixles[x + y * this.w];
	}
	
	public void setScale(int s){
		if(s < 0)s = -s;
		if(s == 0)s = 1;
		this.scale = s;
	}
	
	public int clamp(int x){
		if(x < 0) x = 0;
		if(x > 255) x = 255;
		return x;
	}
}
