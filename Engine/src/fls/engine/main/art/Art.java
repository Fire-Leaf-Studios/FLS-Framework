package fls.engine.main.art;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import fls.engine.main.Init;
import fls.engine.main.art.font.Font;

public class Art {

    public static final int FONTSIZE = 6;
    public static final BufferedImage FONT = new SplitImage("/WText.png").load();
    public static BufferedImage[][] currentFont;
    
    private static HashMap<ABSColor,BufferedImage[][]> coloredFonts = new HashMap<ABSColor,BufferedImage[][]>();
    private static HashMap<String, ABSColor> userColors = new HashMap<String,ABSColor>();

    public static void renderMultiple(BufferedImage image, Graphics g, int amount, int x, int y, int spaceBetween, boolean hoz) {
        for (int i = 0; i < amount; i++) {
            if (hoz) {
                g.drawImage(image, x + (spaceBetween * i), y, null);
            } else {
                g.drawImage(image, x, y + (spaceBetween * i), null);

            }
        }
    }
    
    static{
    	setTextColor(ABSColor.white);
    }

    private static String[] chars = {
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "!?[]()\"'£<>:;+-=0123456789", "/\\.,$"
    };

    public static void drawString(String string, Graphics g, int x, int y) {
    	String[] part = string.split("\n");
    	int i = 0;
    	for(String s : part){
    		Font.draw(s, g, x, y + FONTSIZE * 2 * i);
    		i++;
    	}
    }

    public static void drawScaledText(String msg, Graphics g, int x, int y, int scale) {
    	if(currentFont == null)return;
        msg = msg.toUpperCase();
        for (int i = 0; i < msg.length(); i++) {
            char ch = msg.charAt(i);
            for (int ys = 0; ys < chars.length; ys++) {
                int xs = chars[ys].indexOf(ch);
                if (xs >= 0) {
                    g.drawImage(currentFont[x][y].getScaledInstance(6 * scale, 6 * scale, Image.SCALE_AREA_AVERAGING), x + i * 6 * scale, y, null);
                }
            }
        }
    }

    public static void fillScreen(Init i, Graphics g, ABSColor c) {
        g.setColor(c.getRegColor());
        g.fillRect(0, 0, i.getWidth(), i.getHeight());
    }
    
    private static void addColoredFont(ABSColor c){
    	BufferedImage[][] bi = new SplitImage(FONT).changeImageColor(ABSColor.white, c).split(FONTSIZE,FONTSIZE);
    	coloredFonts.put(c, bi);
    }
    
    public static void setTextColor(ABSColor c){
    	if(coloredFonts.get(c) == null)addColoredFont(c);
    	currentFont = coloredFonts.get(c);
    }
    
    public static void randomColorFont(String name,int ll){
    	Random rand = new Random();
    	int r = (int)((255 - ll)  * rand.nextDouble()); 
    	int g = (int)((255 - ll)  * rand.nextDouble());
    	int b = (int)((255 - ll)  * rand.nextDouble());
    	ABSColor c = new ABSColor(r,g,b);
    	addUserColor(name, c);
    }
    
    public static void addUserColor(String name,ABSColor c){
    	if(userColors.get(name) != null)return;
    	userColors.put(name, c);
    	addColoredFont(c);
    	//setTextColor(c);
    }
    
    public static void useUserColor(String name){
    	ABSColor c = userColors.get(name);
    	if(c != null){
    		setTextColor(c);
    	}
    }
    
    public static BufferedImage getGameImage(Init init){
    	
    	try {
			return new Robot().createScreenCapture(init.frame.getBounds());
		} catch (AWTException e) {
			e.printStackTrace();
			return null;
		}
    	
    }

    /**
     * Creates a directory called Screenshots within the same folder as the .jar</br> and creates a screenshot whit in when this is called</br> screenshot wil be called 'screenshot.png' if it is the first</br> else it will be call 'screenshot_x.png'
     * 
     * @param init
     */
    public static void saveScreenShot(Init init) {
        int atmpt = 1;
        try {
            File dir = new File(init.title + " Screenshots");
            if (!dir.exists()) dir.mkdir();
            File file = new File(dir.getPath() + "/" + init.title + " screenshot.png");
            while (file.exists()) {
                file = new File(dir.getPath() + "/" + init.title + " screenshot-" + atmpt + ".png");
                atmpt++;
            }
            ImageIO.write(getGameImage(init), "png", file);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Somthing has gone wrong while, takeing a screen shot");
        }
        System.out.println("Screenshot has been saved");
    }
    
    public static void showImage(BufferedImage i){
    	JOptionPane.showMessageDialog(null, null,"Image",JOptionPane.OK_OPTION,new ImageIcon(i));
    }
}