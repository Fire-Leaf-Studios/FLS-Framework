package fls.engine.main.art;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Sprites {

    public static final BufferedImage splash = load("/Splash.png");
    public static final BufferedImage[][] textw = split(load("/WText.png"), 6, 6);
    public static final BufferedImage[][] textb = split(load("/BText.png"), 6, 6);

    public static BufferedImage load(String name) {
        try {
            BufferedImage org = ImageIO.read(Sprites.class.getResource(name));
            BufferedImage res = new BufferedImage(org.getWidth(), org.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = res.getGraphics();
            g.drawImage(org, 0, 0, null);
            g.dispose();
            return res;
        } catch (Exception e) {
            throw new RuntimeException(name + " : can't be found");
        }
    }

    public static BufferedImage[][] split(BufferedImage src, int xs, int ys) {
        int xSlide = src.getWidth() / xs;
        int ySlide = src.getHeight() / ys;
        BufferedImage[][] res = new BufferedImage[xSlide][ySlide];
        for (int x = 0; x < xSlide; x++) {
            for (int y = 0; y < ySlide; y++) {
                res[x][y] = new BufferedImage(xs, ys, BufferedImage.TYPE_INT_ARGB);
                Graphics g = res[x][y].getGraphics();
                g.drawImage(src, -xs * x, -ys * y, null);
                g.dispose();
            }
        }
        return res;
    }

    public static BufferedImage[][] altsplit(BufferedImage src, int xs, int ys) {
        int xSlide = src.getHeight() / xs;
        int ySlide = src.getWidth() / ys;
        BufferedImage[][] res = new BufferedImage[xSlide][ySlide];
        for (int x = 0; x < xSlide; x++) {
            for (int y = 0; y < ySlide; y++) {
                res[x][y] = new BufferedImage(xs, ys, BufferedImage.TYPE_INT_ARGB);
                Graphics g = res[x][y].getGraphics();
                g.drawImage(src, xs, 0, 0, ys, x * xs, y * ys, (x + 1) * xs, (y + 1) * ys, null);

            }
        }
        return res;
    }

    public static BufferedImage scale(BufferedImage src, int scale) {
        int w = src.getWidth() * scale;
        int h = src.getHeight() * scale;
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = res.getGraphics();
        g.drawImage(src.getScaledInstance(w, h, BufferedImage.SCALE_AREA_AVERAGING), 0, 0, null);
        return res;
    }

    private static String[] chars = {
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ","!?[]()\"'£<>:;+-=0123456789","/\\.,"
    };

    public static void drawWString(String string, Graphics g, int x, int y) {
        string = string.toUpperCase();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            for (int ys = 0; ys < chars.length; ys++) {
                int xs = chars[ys].indexOf(ch);
                if (xs >= 0) {
                    g.drawImage(textw[xs][ys], x + i * 6, y, null);
                }
            }
        }
    }

    public static void drawBString(String string, Graphics g, int x, int y) {
        string = string.toUpperCase();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            for (int ys = 0; ys < chars.length; ys++) {
                int xs = chars[ys].indexOf(ch);
                if (xs >= 0) {
                    g.drawImage(textb[xs][ys], x + i * 6, y, null);
                }
            }
        }
    }
}
