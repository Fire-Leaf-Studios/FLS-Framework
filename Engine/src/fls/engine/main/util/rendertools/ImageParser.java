package fls.engine.main.util.rendertools;

import java.awt.image.BufferedImage;

import fls.engine.main.art.SplitImage;

public class ImageParser {

	
	public static int[][][] parseImage(String loc, int xs){
		return ImageParser.parseImage(loc, xs, xs);
	}
	
	public static int[][][] parseImage(String loc, int xs, int ys) {
		int[][][] out = new int[xs][ys][];
		BufferedImage img = new SplitImage(loc).getImage();
		
		int ws = img.getWidth() / xs;
		int hs = img.getHeight() / ys;
		
		for(int x = 0; x < ws; x++) {
			for(int y = 0; y < hs; y++) {
				out[x][y] = prepImage(img, x, y, xs, ys);
			}
		}
		
		return out;
	}
	
	
	private static int[] prepImage(BufferedImage img, int x, int y, int xs, int ys) {
		int[] res = new int[xs * ys];
		img.getRGB(x * xs, y * ys, xs, ys, res, 0, xs);
		for (int i = 0; i < xs * ys; i++) {
			int c = res[i];

			int rr = (c >> 16) & 0xFF;
			int gg = (c >> 8) & 0xFF;
			int bb = (c) & 0xFF;
			
			res[i] = (rr << 16) | (gg << 8) | bb;
		}
		
		return res;
	}
}
