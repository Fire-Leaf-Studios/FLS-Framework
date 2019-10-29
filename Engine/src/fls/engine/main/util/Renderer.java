package fls.engine.main.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import fls.engine.main.Init;
import fls.engine.main.screen.Screen;
import fls.engine.main.util.rendertools.Sprite;

public class Renderer {

	private BufferedImage img;

	protected int[] pixles;
	private boolean[] dirty;
	protected int w, h;

	private int scale;
	protected int xOff, yOff;
	
	public Renderer(Screen s) {
		this(s.game);
	}

	public Renderer(Init i) {
		Font.newInstance(i);
		if (!i.isCustomImage()) {
			throw new IllegalStateException("Not using a custom draw surface!");
		}

		BufferedImage img = i.image;
		this.img = img;
		this.w = img.getWidth();
		this.h = img.getHeight();

		this.pixles = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		this.dirty = new boolean[h];
		setOffset(0, 0);
		setScale(1);
	}

	public void finaliseRender() {
		for (int y = 0; y < this.h; y++) {
			if (this.dirty[y]) {
				img.setRGB(0, y, w, 1, getPixels(y), 0, w);
				this.dirty[y] = false;
			}
		}
	}

	private int[] getPixels(int y) {
		int[] res = new int[this.w];
		for (int i = 0; i < this.w; i++) {
			res[i] = this.pixles[i + y * this.w];
		}
		return res;
	}

	public void setPixel(int x, int y, int c) {
		if(c < 0)return;
		
		x += this.xOff;
		y += this.yOff;
		
		if (!isValid(x, y)) {
			return;	
		}
		if (this.pixles[x + y * this.w] == c) {
			return;
		}
		
		this.pixles[x + y * this.w] = c;
		this.dirty[y] = true;
	}
	
	public void renderSprite(Sprite s) {
		if(s.getBitmapData() == null || s.getWidth() == 0) return;
		this.renderSection(s.getBitmapData(), s.getPos().getIX(), s.getPos().getIY(), s.getWidth());
	}

	public void renderSection(int[] data, int x, int y, int xs) {
		for (int i = 0; i < xs * xs; i++) {
			int xx = i % xs;
			int yy = i / xs;
			setPixel(x + xx, y + yy, data[xx + yy * xs]);
		}
	}

	public void renderImage(BufferedImage img, int x, int y, int w, int h) {
		int[] res = new int[w * h];
		img.getRGB(0, 0, w, h, res, 0, w);
		this.renderSection(res, x, y, w);
	}

	public void renderImage(BufferedImage img, int x, int y) {
		renderImage(img, x, y, img.getWidth(), img.getHeight());
	}

	public void shade(int x, int y) {
		x += this.xOff;
		y += this.yOff;
		if (!isValid(x, y))
			return;
		int c = this.pixles[x + y * this.w];
		int r = (c >> 16) & 0xFF;
		int g = (c >> 8) & 0xFF;
		int b = (c) & 0xFF;
		int rgb = getShadedColor(r, g, b, 0.7f);
		setPixel(x - this.xOff, y - this.yOff, rgb);
	}

	public void lighten(int x, int y) {
		x += this.xOff;
		y += this.yOff;
		if (!isValid(x, y))
			return;
		int c = this.pixles[x + y * this.w];
		int r = (c >> 16) & 0xFF;
		int g = (c >> 8) & 0xFF;
		int b = (c) & 0xFF;
		int rgb = getShadedColor(r, g, b, 1 / 0.7f);
		setPixel(x - this.xOff, y - this.yOff, rgb);
	}

	public int getShadedColor(int r, int g, int b, float amt) {
		if (amt >= 1)
			return makeRGB(r, g, b);
		else if (amt <= 0)
			return 0;
		r *= amt;
		g *= amt;
		b *= amt;
		return makeRGB(r, g, b);
	}

	public int getShadedColor(int col, float amt) {
		if (amt >= 1)
			return col;
		else if (amt <= 0)
			return 0;
		int r = (col >> 16) & 0xFF;
		int g = (col >> 8) & 0xFF;
		int b = (col) & 0xFF;
		return getShadedColor(r, g, b, amt);
	}

	private boolean isValid(int x, int y) {
		if (x < 0 || y < 0 || x >= w || y >= h)
			return false;
		else
			return true;
	}

	public int makeRGB(int r, int g, int b) {
		r = clamp(r);
		g = clamp(g);
		b = clamp(b);
		return (r << 16) | (g << 8) | b;
	}

	public int makeRGB(String r, String g, String b) {
		return makeRGB(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b));
	}

	public void fill(int c) {
		for (int i = 0; i < this.pixles.length; i++) {
			this.pixles[i] = c;
		}
	}

	public void checkerBoard(int x, int y, int w, int h) {
		int r = 117;
		int g = 163;
		int b = 0;
		checkerBoard(x, y, w, h, r, g, b);
	}

	public void checkerBoard(int x, int y, int w, int h, int r, int g, int b) {
		int rgb = makeRGB(r, g, b);
		int shade = getShadedColor(r, g, b, 0.7f);
		int size = 20;
		for (int xx = x; xx <= x + w + 1; xx++) {
			for (int yy = y; yy <= y + h / 2 + 1; yy++) {
				boolean xb = xx % size >= size/2;
				boolean yb = yy % size >= size/2;
				int c = ((xb && !yb) || (yb && !xb))?shade:rgb;
				this.setPixel(xx, yy, c);
			}
		}
	}

	public void shadeCircle(int x, int y, int r) {
		int top = y - r;
		int left = x - r;
		for (int dx = 0; dx < r * 2; dx++) {
			for (int dy = 0; dy < r * 2; dy++) {

				int px = left + dx;
				int py = top + dy;

				int tx = px - x;
				int ty = py - y;

				if ((tx * tx) + (ty * ty) < (r * r) - 16)
					shade(px, py);
			}
		}

	}

	public void shadeElipse(int x, int y, int w, int h) {
		w += 3;
		x++;
		for (int yy = -h + 1; yy < h; yy++) {
			for (int xx = -w; xx <= w; xx++) {
				if (xx * xx * h * h + yy * yy * w * w <= h * h * w * w) {
					int dx = (x) + xx;
					shade(dx, y + yy);
				}
			}
		}
	}

	public void lightenElipse(int x, int y, int w, int h) {
		w += 3;
		x++;
		for (int yy = -h + 1; yy < h; yy++) {
			for (int xx = -w; xx <= w; xx++) {
				if (xx * xx * h * h + yy * yy * w * w <= h * h * w * w) {
					int dx = (x) + xx;
					lighten(dx, y + yy);
				}
			}
		}
	}

	public void setOffset(int xo, int yo) {
		this.xOff = xo;
		this.yOff = yo;
	}
	
	public Point getOffset() {
		return new Point(this.xOff, this.yOff);
	}

	public int getWidth() {
		return this.w;
	}

	public int getHeight() {
		return this.h;
	}

	public boolean isOnScreen(int x, int y) {
		return isValid(x + xOff, y + yOff);
	}

	public void fillCircle(int x, int y, int r, int c) {
		if (c == -1)
			c = makeRGB(255, 255, 255);
		for (int xx = -r; xx < r; xx++) {
			for (int yy = -r; yy < r; yy++) {
				int x2 = xx * xx;
				int y2 = yy * yy;
				if (x2 + y2 < r * r) {
					this.setPixel(x + xx, y + yy, c);
				}
			}
		}
	}
	
	public void drawCircle(int x, int y, int r, int c) {
		if (c == -1)
			c = makeRGB(255, 255, 255);
		for (int xx = -r; xx < r; xx++) {
			for (int yy = -r; yy < r; yy++) {
				int min = r - 1;
				if(Math.abs(xx) < min && Math.abs(yy) < min)continue;
				int x2 = xx * xx;
				int y2 = yy * yy;
				if (x2 + y2 < (r * r)) {
					this.setPixel(x + xx, y + yy, c);
				}
			}
		}
	}

	public void drawLine(int x0, int y0, int x1, int y1, int c) {
		int w = x1 - x0;
		int h = y1 - y0;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0)
			dx1 = -1;
		else if (w > 0)
			dx1 = 1;

		if (h < 0)
			dy1 = -1;
		else if (h > 0)
			dy1 = 1;

		if (w < 0)
			dx2 = -1;
		else if (w > 0)
			dx2 = 1;

		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0)
				dy2 = -1;
			else if (h > 0)
				dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			setPixel(x0, y0, c);
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x0 += dx1;
				y0 += dy1;
			} else {
				x0 += dx2;
				y0 += dy2;
			}
		}
	}

	public void drawLine(int x0, int y0, int x1, int y1) {
		this.drawLine(x0, y0, x1, y1, 0xFFFFFF);
	}

	public void drawLine(float x0, float y0, float x1, float y1, int c) {
		this.drawLine((int) x0, (int) y0, (int) x1, (int) y1, c);
	}

	public void drawLine(float x0, float y0, float x1, float y1) {
		this.drawLine((int) x0, (int) y0, (int) x1, (int) y1, 0xFFFFFF);
	}

	public void drawLine(Point p1, Point p2, int c) {
		this.drawLine(p1.x, p1.y, p2.x, p2.y, c);
	}

	public void drawLine(Point p1, Point p2) {
		this.drawLine(p1.x, p1.y, p2.x, p2.y, 0xFFFFFF);
	}

	public int getColor(int x, int y) {
		if (!isValid(x, y))
			return -1;
		return this.pixles[x + y * this.w];
	}

	public void setScale(int s) {
		if (s < 0)
			s = -s;
		if (s == 0)
			s = 1;
		this.scale = s;
	}

	public int clamp(int x) {
		if (x < 0)
			return 0;
		if (x > 255)
			return 255;
		else
			return x;
	}

	public int larp(int c1, int c2, float amt) {

		amt = Math.max(0f, Math.min(1f, amt));

		if (amt >= 1)
			return c2;
		else if (amt <= 0)
			return c1;
		int r1 = (c1 >> 16) & 0xFF;
		int g1 = (c1 >> 8) & 0xFF;
		int b1 = (c1) & 0xFF;

		int r2 = (c2 >> 16) & 0xFF;
		int g2 = (c2 >> 8) & 0xFF;
		int b2 = (c2) & 0xFF;

		int minR = Math.min(r1, r2);
		int minG = Math.min(g1, g2);
		int minB = Math.min(b1, b2);

		int maxR = Math.max(r1, r2);
		int maxG = Math.max(g1, g2);
		int maxB = Math.max(b1, b2);

		int rr = (int) (minR + ((maxR - minR) * amt));
		int gg = (int) (minG + ((maxG - minG) * amt));
		int bb = (int) (minB + ((maxB - minB) * amt));

		rr = clamp(rr);
		gg = clamp(gg);
		bb = clamp(bb);

		return this.makeRGB(rr, gg, bb);
	}

	public void fillRect(int x, int y, int w, int h, int col) {
		for (int xx = 0; xx <= w; xx++) {
			for (int yy = 0; yy <= h; yy++) {
				setPixel(x + xx, y + yy, col);
			}
		}
	}
	
	public void drawRect(int x, int y, int w, int h, int col) {
		for(int xx = x; xx <= x + w; xx++) {
			for(int yy = y; yy <= y + h; yy++) {
				if(xx == x || yy == y || xx == x + w | yy == y + h) {
					setPixel(xx, yy, col);
				}
			}
		}
	}
	
	public int generateColor() {
		return this.generateColor(180, 230);
	}
	
	public int generateColor(int min) {
		return this.generateColor(min, 255);
	}
	
	public int generateColor(int min, int max) {
		int r = (int)(min + ((max - min) * Math.random()));
		int g = (int)(min + ((max - min) * Math.random()));
		int b = (int)(min + ((max - min) * Math.random()));
		
		return r << 16 | g << 8 | b;
	}
	
	
	
	// x0, y0 : transformation centre
	// A : Length of X transform arm
	// C : Amount of X transform
	// B : Amount of Y transform
	// D : Length of Y transform
	
	public int[] afine(int[] data, int w, int x0, int y0, int a, int b, int c, int d) {
		int[] res = new int[data.length];
		int mh = data.length / w;
		for(int i = 0; i < data.length; i++) {
			int tx = i % w;
			int ty = i / w;
			
			int xi = tx - x0;
			int yi = ty - y0;
			
			int xp = (a * xi) + (b * yi);
			int yp = (c * xi) + (d * yi);
			
			if(xp < 0 || yp < 0 || xp > w - 1 || yp > mh - 1) {
				res[i] = -1;
			}else {
				res[xp + yp * w] = data[i];
			}
		}
		return res;
	}
	
	
	public int[] rotate(int[] data, int w, float theta) {
		while(theta > 360) {
			theta -= 360;
		}
		theta = (float)((theta / 180f) * Math.PI);
		float[] mat = new float[] {
				(float)Math.sin(theta), (float)Math.cos(theta),
				(float)Math.cos(theta), (float)-Math.sin(theta)
		};
		
		int[] res = new int[data.length];
		int mh = res.length / w;
		int cx = w / 2;
		int cy = (mh) / 2;
		
		for(int i = 0; i < data.length; i++) {
			int tx = i % w;
			int ty = i / w;
			
			int dx = tx - cx;
			int dy = ty - cy;
			
			int rx = (int)(cx + dx * mat[0] + dy * mat[1]);
			int ry = (int)(cx + dx * mat[2] + dy * mat[3]);
			
			if(rx >= 0 && rx < w && ry >= 0 && ry < mh) {
				res[i] = data[rx + ry * w];
			}
		}
		return res;
	}
}
