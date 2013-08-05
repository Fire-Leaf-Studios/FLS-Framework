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

    public Input(Init game, int type) {
        switch (type) {
        case 0:
            game.addKeyListener(this);
            break;
        case 1:
            game.addMouseListener(this);
            game.addMouseMotionListener(this);
            break;
        case 2:
            game.addKeyListener(this);
            game.addMouseListener(this);
            game.addMouseMotionListener(this);
            break;
        default:
            System.err.println("Not a valid type entered");
            break;

        }
    }

    public class Key {
        private int numTimesPressed = 0;
        private boolean pressed = false;

        public int getNumTimesPressed() {
            return numTimesPressed;
        }

        public boolean isPressed() {
            return pressed;
        }

        public void toggle(boolean isPressed) {
            pressed = isPressed;
            if (isPressed) numTimesPressed++;
        }
    }

    public class Mouse {
        private int numTimesClicked = 0;
        private boolean clicked = false;
        private int x, y;

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

        public void toggle(boolean isClicked) {
            clicked = isClicked;
            if (isClicked) numTimesClicked++;
        }
    }

    public void releaseAllKeys() {
        for (int i = 0; i < keys.size(); i++) {
            keys.get(i).pressed = false;
        }
    }

    public List<Key> keys = new ArrayList<Key>();

    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();
    public Key shift = new Key();
    public Key enter = new Key();
    public Key esc = new Key();
    public Key space = new Key();

    public List<Mouse> mb = new ArrayList<Mouse>();

    public Mouse leftMouseButton = new Mouse();
    public Mouse rightMouseButton = new Mouse();
    public Mouse mouse = new Mouse(); // the ONLY mouse the get X & Y;

    public void keyPressed(KeyEvent e) {
        toggleKey(e.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent e) {
        toggleKey(e.getKeyCode(), false);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void toggleKey(int keyCode, boolean isPressed) {
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
            up.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
            down.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
            left.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
            right.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_SHIFT) {
            shift.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_ENTER) {
            enter.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            esc.toggle(isPressed);
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            space.toggle(isPressed);
        }
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
    }

    public void toggleMouse(int mouseButton, boolean isClicked) {
        if (mouseButton == MouseEvent.BUTTON1) leftMouseButton.toggle(isClicked);
        if (mouseButton == MouseEvent.BUTTON3) rightMouseButton.toggle(isClicked);
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        mouse.x = e.getX();
        mouse.y = e.getY();
    }
}