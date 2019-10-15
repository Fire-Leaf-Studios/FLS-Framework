package fls.engine.main.util;

import fls.engine.main.Init;
import fls.engine.main.io.FileIO;
import fls.engine.main.util.rendertools.SpriteParser;

public class Font {

	
	public static Font instance = new Font();
	private final String letters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ+-()!,’.:/><@^~#¬_=|[]";
	private final SpriteParser sp = new SpriteParser(FileIO.instance.readInternalFile("/font.art"));
	private int w = 0;
	private int h = 0;
	
	public static void newInstance(Init i){
		Font f = new Font();
		if(i.isCustomImage()){
			f.w = i.image.getWidth();
			f.h = i.image.getHeight();
		}else{
			f.w = i.getWidth();
			f.h = i.getHeight();
		}
		Font.instance = f;
	}
	
	public void drawString(Renderer r, String msg, int x, int y){
		if(msg == null || msg.isEmpty()) return;
		msg = msg.toUpperCase();
		String[] l = msg.split("\n");
		for(int m = 0; m < l.length; m++){
			String line = l[m];
			for(int i = 0; i < line.length(); i++){
				String c = line.substring(i,i+1);
				if(c.equals(" "))continue;
				int ind = letters.indexOf(c.charAt(0));
				if(ind == -1)continue;
				int[] data = sp.getData(ind);
				r.renderSection(data, x + i * 8, y + m * 8, 8);
			}
		}
	}
	
	public void drawString(Renderer r, String msg, int x, int y, int col){
		if(msg == null || msg.isEmpty()) return;
		msg = msg.toUpperCase();
		String[] l = msg.split("\n");
		for(int m = 0; m < l.length; m++){
			String line = l[m];
			for(int i = 0; i < line.length(); i++){
				String c = line.substring(i,i+1);
				if(c.equals(" "))continue;
				int ind = letters.indexOf(c.charAt(0));
				int[] data = sp.getData(ind);
				for(int j = 0; j < 8 * 8; j++){
					int cc = data[j];
					if(cc == -1)continue;
					int tx = j % 8;
					int ty = j / 8;
					r.setPixel(x + tx + (i *8), y + ty + m * 8, cc & col);
				}
			}
		}
	}
	
	public void drawStringCenter(Renderer r, String msg, int y){
		String[] p = msg.split("\n");
		for(int i = 0; i < p.length; i++){
			int w = p[i].trim().length() * 8;
			int xo = (this.w - w) / 2;
			drawString(r, p[i], xo, y + i * 8);
		}
	}
	
	public void drawStringCenter(Renderer r, String msg, int y, int col){
		String[] p = msg.split("\n");
		for(int i = 0; i < p.length; i++){
			int w = p[i].trim().length() * 8;
			int xo = (this.w - w) / 2;
			drawString(r, p[i], xo, y + i * 8, col);
		}
	}
	
	public void drawStringCenter(Renderer r, String msg){
		String[] lines =  msg.split("\n");
		int gyo = lines.length * 4;
		for(int i = 0; i < lines.length; i++){
			String c = lines[i];
			int w = c.trim().length() * 8;
			if(c.indexOf("%") != -1){
				String middle = c.substring(c.indexOf("%") + 1, c.lastIndexOf("%"));
				String[] parts = middle.split("%");
				w -= (parts[0].length()+3) * 8;
			}
			int xo = (this.w - w) / 2;
			int yo = (this.h - 8) / 2 + i * 8;
			
			if(c.indexOf("%") != -1){//we want to color this one
				String before = c.substring(0, c.indexOf("%"));
				String after = c.substring(c.lastIndexOf("%") + 1);
				
				String middle = c.substring(c.indexOf("%") + 1, c.lastIndexOf("%"));
				String[] parts = middle.split("%");
				String[] cParts = parts[0].split(",");
				int col = r.makeRGB(cParts[0], cParts[1], cParts[2]);
				drawString(r, before, xo, yo - gyo);
				drawString(r, parts[1], xo + before.length()*8, yo - gyo, col);
				drawString(r, after, xo + (parts[1]+before).length()*8, yo - gyo);
			}else{
				drawString(r, c, xo, yo - gyo);
			}
		}
	}
	
	public void drawStringCenterWithColor(Renderer r, String msg, int col){
		String[] lines =  msg.split("\n");
		int gyo = lines.length * 4;
		for(int i = 0; i < lines.length; i++){
			String c = lines[i].trim();
			int w = (c.trim().length()+1) * 8;
			int xo = (this.w - w) / 2;
			int yo = (this.h - 8) / 2 + i * 8;
			drawString(r, c, xo, yo - gyo, col);
		}
	}
	
	public void drawCursor(Renderer r, int x, int y){
		drawString(r, "¬", x, y);
	}
	
	public void drawCursor(Renderer r, int x, int y, int col){
		drawString(r, "¬", x, y, col);
	}
}
