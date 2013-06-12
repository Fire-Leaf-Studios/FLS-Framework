package fls.engine.main.input;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class Button {

    private Rectangle bounds;
    private boolean isPressed = false;

    public Button(BufferedImage img, Graphics g, int x, int y) {
        g.drawImage(img, x, y, null);
        bounds = new Rectangle(x, y, x + img.getWidth(), y + img.getHeight());
    }
    
    public abstract void onClick();
}
