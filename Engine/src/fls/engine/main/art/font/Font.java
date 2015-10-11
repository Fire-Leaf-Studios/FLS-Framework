package fls.engine.main.art.font;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import fls.engine.main.art.Art;

public class Font {

    public static String letters = "" + //
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + //
            "!?[]()\"'�<>:;+-=0123456789" + //
            "/\\.,|                     ";

    public static int getStringWidth(String s) {
        return s.length() * 6;
    }

    public static void draw(String msg, Graphics g, int x, int y) {
        msg = msg.toUpperCase();
        int length = msg.length();
        BufferedImage[][] font = Art.currentFont;
        for (int i = 0; i < length; i++) {
            int c = letters.indexOf(msg.charAt(i));
            if (c > 56) continue;
            g.drawImage(font[c % 26][c / 26], x + (i * 6), y, null);
        }
    }
    
    public static void drawColored(String msg,Graphics g,BufferedImage[][] cc,int x,int y){
    	 msg = msg.toUpperCase();
         int length = msg.length();
         for (int i = 0; i < length; i++) {
             int c = letters.indexOf(msg.charAt(i));
             if (c > 56) continue;
             g.drawImage(cc[c % 26][c / 26], x + (i * 6), y, null);
         }
     }
}
