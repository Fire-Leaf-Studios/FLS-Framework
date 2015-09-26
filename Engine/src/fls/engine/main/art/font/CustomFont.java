package fls.engine.main.art.font;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import fls.engine.main.art.SplitImage;

public class CustomFont extends Font {

    private static BufferedImage[][] font;
    private static int xlength, xSplit;

    public CustomFont(String fontLoc, int xs, int ys, int xL) {
        CustomFont.font = new SplitImage(fontLoc, xs, ys).split();
        CustomFont.xlength = xL;
        CustomFont.xSplit = xs;
    }

    public CustomFont(String fontLoc, int xs, int ys) {
        this(fontLoc, xs, ys, 26);
    }

    public static void draw(String msg, Graphics g, int x, int y) {
        msg = msg.toUpperCase();
        int length = msg.length();
        for (int i = 0; i < length; i++) {
            int c = letters.indexOf(msg.charAt(i));
            if (c > 56) continue;
            g.drawImage(font[c % xlength][c / xlength], x + (i * xSplit), y, null);
        }
    }
}
