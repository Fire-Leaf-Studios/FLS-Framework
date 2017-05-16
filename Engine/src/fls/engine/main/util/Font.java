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
		int w = msg.trim().length() * 8;
		int xo = (this.w - w) / 2;
		drawString(r, msg, xo, y);
	}
	
	public void drawStringCenter(Renderer r, String msg, int y, int col){
		int w = msg.trim().length() * 8;
		int xo = (this.w - w) / 2;
		drawString(r, msg, xo, y, col);
	}
	
	public void drawStringCenter(Renderer r, String msg){
		String[] lines =  msg.split("\n");
		int gyo = lines.length * 4;
		for(int i = 0; i < lines.length; i++){
			String c = lines[i].trim();
			int w = c.trim().length() * 8;
			int xo = (this.w - w) / 2;
			int yo = (this.h - 8) / 2 + i * 8;
			drawString(r, c, xo, yo - gyo);
		}
	}
	
	public void drawStringCenterWithColor(Renderer r, String msg, int col){
		String[] lines =  msg.split("\n");
		int gyo = lines.length * 4;
		for(int i = 0; i < lines.length; i++){
			String c = lines[i].trim();
			int w = c.trim().length() * 8;
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
