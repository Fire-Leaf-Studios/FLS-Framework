package fls.engine.main.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import fls.engine.main.Init;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

    public static int KEYS = 0, MOUSE = 1, BOTH = 2;
    private int photoKey = -1;
    public boolean[] keys = new boolean[65536];

    public List<Mouse> mb = new ArrayList<Mouse>();

    public Mouse leftMouseButton = new Mouse();
    public Mouse rightMouseButton = new Mouse();
    public Mouse mouse = new Mouse(); // the ONLY mouse the get X & Y;

    public Input(Init game, int type) {
        switch (type) {
        case 0:
            System.out.println("Added Key input");
            game.addKeyListener(this);
            break;
        case 1:
            System.out.println("Added Mouse input");
            game.addMouseListener(this);
            game.addMouseMotionListener(this);
            break;
        case 2:
            System.out.println("Added Key and Mouse input");
            game.addKeyListener(this);
            game.addMouseListener(this);
            game.addMouseMotionListener(this);
            break;
        default:
            System.err.println("Not a valid type entered");
            break;
        }
    }

    public void setScreenshotKey(int key) {
        this.photoKey = key;
    }

    public int getScreenshtoKey() {
        return this.photoKey;
    }

    public int getPressedKey() {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i]) return i;
        }
        return -1;
    }

    public class Mouse {
        private int numTimesClicked = 0;
        private boolean clicked = false;
        private int x, y, dx, dy;
        private boolean beingDraged = false;

        public int getNumTimesClicked() {
            return numTimesClicked;
        }

        public boolean isClicked() {
            return clicked;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getDX() {
            return dx;
        }

        public int getDY() {
            return dy;
        }

        public boolean draged() {
            return beingDraged;
        }

        public void toggle(boolean isClicked) {
            clicked = isClicked;
            if (isClicked) numTimesClicked++;
        }
    }

    public void releaseAllKeys() {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code > 0 && code < keys.length) keys[code] = true;

    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code > 0 && code < keys.length) keys[code] = false;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent arg0) {

    }

    public void mouseEntered(MouseEvent arg0) {

    }

    public void mouseExited(MouseEvent arg0) {

    }

    public void mousePressed(MouseEvent e) {
        toggleMouse(e.getButton(), true);
    }

    public void mouseReleased(MouseEvent e) {
        toggleMouse(e.getButton(), false);
        mouse.beingDraged = false;
    }

    public void toggleMouse(int mouseButton, boolean isClicked) {
        if (mouseButton == MouseEvent.BUTTON1) leftMouseButton.toggle(isClicked);
        if (mouseButton == MouseEvent.BUTTON3) rightMouseButton.toggle(isClicked);
    }

    public void mouseDragged(MouseEvent e) {
        mouse.beingDraged = true;
        mouse.dx = e.getX();
        mouse.dy = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
        mouse.x = e.getX();
        mouse.y = e.getY();
    }
}