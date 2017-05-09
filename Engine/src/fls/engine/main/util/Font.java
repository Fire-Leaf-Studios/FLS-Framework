package fls.engine.main.util;

import fls.engine.main.Init;
import fls.engine.main.io.FileIO;
import fls.engine.main.util.Renderer;
import fls.engine.main.util.rendertools.SpriteParser;

public class Font {

	
	public static Font instance = new Font();
	private final String letters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ+-()!,’.:/><@^~#¬";
	private final SpriteParser sp = new SpriteParser(FileIO.instance.readInternalFile("/font.art"));
	private static int w = 0;
	
	public static void newInstance(Init i){
		Font f = new Font();
		if(i.isCustomImage()){
			f.w = i.image.getWidth();
		}else{
			f.w = i.getWidth();
		}
		Font.instance = f;
	}
	
	public void render(Renderer r, String msg, int x, int y){
		if(msg == null || msg.isEmpty()) return;
		msg = msg.toUpperCase().trim();
		for(int i = 0; i < msg.length(); i++){
			String c = msg.substring(i,i+1);
			if(c.equals(" "))continue;
			int ind = letters.indexOf(c.charAt(0));
			r.renderSection(sp.getData(ind), x + (i * 8), y, 8);
		}
	}
	
	public void render(Renderer r, String msg, int x, int y, int col){
		if(msg == null || msg.isEmpty()) return;
		msg = msg.toUpperCase().trim();
		for(int i = 0; i < msg.length(); i++){
			String c = msg.substring(i,i+1);
			if(c.equals(" "))continue;
			int ind = letters.indexOf(c.charAt(0));
			int[] data = sp.getData(ind);
			for(int j = 0; j < 8 * 8; j++){
				int cc = data[j];
				if(cc == -1)continue;
				int tx = j % 8;
				int ty = j / 8;
				r.setPixel(x + tx + (i *8), y + ty, cc & col);
			}
		}
	}
	
	public void renderCenter(Renderer r, String msg, int y){
		int w = msg.trim().length() * 8;
		int xo = (this.w - w) / 2;
		render(r, msg, xo, y);
	}
	
	public void renderCenter(Renderer r, String msg, int y, int col){
		int w = msg.trim().length() * 8;
		int xo = (this.w - w) / 2;
		render(r, msg, xo, y, col);
	}
	
	public void drawCursor(Renderer r, int x, int y){
		render(r, "¬", x, y);
	}
	
	public void drawCursor(Renderer r, int x, int y, int col){
		render(r, "¬", x, y, col);
	}
}
