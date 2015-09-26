package fls.engine.main.art;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import fls.engine.main.Init;
import fls.engine.main.art.font.Font;

public class Art {

    private static int pref = -1;
    public static final BufferedImage[][] WText = new SplitImage("/WText.png", 6, 6).split();// split(load("/WText.png"), 6, 6);
    public static final BufferedImage[][] BText = new SplitImage("/BText.png", 6, 6).split();

    public static void renderMultiple(BufferedImage image, Graphics g, int amount, int x, int y, int spaceBetween, boolean hoz) {
        for (int i = 0; i < amount; i++) {
            if (hoz) {
                g.drawImage(image, x + (spaceBetween * i), y, null);
            } else {
                g.drawImage(image, x, y + (spaceBetween * i), null);

            }
        }
    }

    private static String[] chars = {
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "!?[]()\"'�<>:;+-=0123456789", "/\\.,|"
    };

    public static void drawString(String string, Graphics g, int x, int y) {
        Font.draw(string, g, x, y);
    }

    public static void drawScaledText(String msg, Graphics g, int x, int y, int scale) {
        msg = msg.toUpperCase();
        for (int i = 0; i < msg.length(); i++) {
            char ch = msg.charAt(i);
            for (int ys = 0; ys < chars.length; ys++) {
                int xs = chars[ys].indexOf(ch);
                if (xs >= 0) {
                    g.drawImage(pref == 1 ? WText[xs][ys].getScaledInstance(6 * scale, 6 * scale, Image.SCALE_AREA_AVERAGING) : BText[xs][ys].getScaledInstance(6 * scale, 6 * scale, Image.SCALE_AREA_AVERAGING), x + i * 6 * scale, y, null);
                }
            }
        }
    }

    public static void fillScreen(Init i, Graphics g, Color c) {
        g.setColor(c);
        g.fillRect(0, 0, i.getWidth(), i.getHeight());
    }

    public static void setTextCol(Color col) {
        if (col == Color.white) pref = 1;
        if (col == Color.black) pref = -1;
    }

    /**
     * Creates a directory called Screenshots within the same folder as the .jar</br> and creates a screenshot whit in when this is called</br> screenshot wil be called 'screenshot.png' if it is the first</br> else it will be call 'screenshot_x.png'
     * 
     * @param init
     */
    public static void saveScreenShot(Init init) {
        int atmpt = 1;
        System.out.println("Screenshot has been saved");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            BufferedImage image = new Robot().createScreenCapture(new Rectangle((int) (screen.getWidth() / 2 - init.getWidth() / 2), (int) (screen.getHeight() / 2 - init.getHeight() / 2) - 9, init.getWidth(), init.getHeight()));
            File dir = new File(init.title + " Screenshots");
            if (!dir.exists()) dir.mkdir();
            File file = new File(dir.getPath() + "/" + init.title + " screenshot.png");
            while (file.exists()) {
                file = new File(dir.getPath() + "/" + init.title + " screenshot_" + atmpt + ".png");
                atmpt++;
            }
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}