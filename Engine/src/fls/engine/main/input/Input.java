package fls.engine.main.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import fls.engine.main.Init;

public class Input implements KeyListener, MouseListener, MouseMotionListener {

    public static int KEYS = 0, MOUSE = 1, CONTROLLER = 2;
    private int photoKey = -1;
    private boolean[] keys;
    private boolean[] preKeys;
    
    private boolean addedMouse = false;
    private boolean addedKeyboard = false;
    private boolean addedControllers = false;
    
    private HashMap<String,int[]> preDefs;
    private Controller[] conts;
    private CustomController primaryController;

    public Mouse leftMouseButton = new Mouse();
    public Mouse rightMouseButton = new Mouse();
    public Mouse mouse = new Mouse(); // the ONLY mouse the get X & Y;

    /**
     * The class that handles all of the input in our games
     * @param game - Any class that extends the Init class
     * @param type - Any number of identifiers set from {@link #KEYS},{@link #MOUSE} and {@link #CONTROLLER}
     */
    public Input(Init game, int... type) {
    	for(int i : type)
        switch (i) {
        case 0:
        	if(this.preDefs != null)break;
            System.out.println("Added Key input");
            game.addKeyListener(this);
            this.keys = new boolean[65536];
            this.preKeys = new boolean[65536];
            this.preDefs = new HashMap<String,int[]>();
            this.setScreenshotKey(KeyEvent.VK_P);
            this.preDefs.put("up", new int[]{KeyEvent.VK_W,KeyEvent.VK_UP});
            this.preDefs.put("down", new int[]{KeyEvent.VK_S,KeyEvent.VK_DOWN});
            this.preDefs.put("left", new int[]{KeyEvent.VK_A,KeyEvent.VK_LEFT});
            this.preDefs.put("right", new int[]{KeyEvent.VK_D,KeyEvent.VK_RIGHT});
            this.addedKeyboard = true;
            break;
        case 1:
        	this.addedMouse = true;
            System.out.println("Added Mouse input");
            game.addMouseListener(this);
            game.addMouseMotionListener(this);
            break;
        case 2:
        	this.addedControllers = true;
            System.out.println("Added Controller input");
            conts = ControllerEnvironment.getDefaultEnvironment().getControllers();
            for(Controller c : conts){
            	System.out.println(c.getName());
            }
            break;
        default:
            System.err.println("Not a valid type entered");
            break;
        }
    }
    
    /**
     * A function called to set the primary controller, useful for things like start screens
     */
    public void setPrimaryContoller(){
    	if(!this.addedControllers)return;
    	for(Controller c : conts){
    		c.poll();
    		Component[] comps = c.getComponents();
    		for(Component comp : comps){
    			if(comp.getIdentifier().getName() == "Start" && c.getType() == Controller.Type.GAMEPAD){
    				if(comp.getPollData() == 1.0f){
    					this.primaryController = new CustomController(c);
    					return;
    				}
    			}
    		}
    	}
    }
    
    public CustomController getController(){
    	return this.primaryController;
    }
    
    public void tick(){
    	if(!this.addedKeyboard)return;
    	for(int i = 0; i < this.keys.length; i++){
    		this.preKeys[i] = this.keys[i];
    	}
    	this.releaseAllKeys();
    }

    /**
     * A small function that sets the 'Screen-cap' key
     * @param key
     */
    public void setScreenshotKey(int key) {
        this.photoKey = key;
    }

    /**
     * A function that returns the 'Screen-cap' key
     * @return int - The int set in {@link #setScreenshotKey(int)}
     */
    public int getScreenshotKey() {
        return this.photoKey;
    }
    
    /**
     * A small helper function that determines whether a key is pressed
     * @param i
     * @return true or false
     */
    public boolean isKeyPressed(int i){
    	if(!this.addedKeyboard)return false;
    	return this.keys[i];
    }
    
    public boolean isPreKeyPressed(int i){
    	if(!this.addedKeyboard)return false;
    	return this.preKeys[i];
    }
    
    /**
     * A function where uses can put groups of keys under one string
     * @param key
     * @param keys
     */
    public void addPreDefKeys(String key,int... keys){
    	this.preDefs.put(key, keys);
    }
    
    /**
     * The function that returns if anyone of a group of set keys is pressed
     * @param key
     * @return true or false
     */
    public boolean getDefKeyRes(String key){
    	int[] keys = this.preDefs.get(key);
    	for(int i:keys){
    		if(this.keys[i] == true)return true;
    	}
    	return false;
    }
    
    
    /**
     * Returns the value of the 'up' group
     * @return true or false
     */
    public boolean isUp(){
    	return getDefKeyRes("up");
    }
    
    /**
     * Returns the value of the 'down' group
     * @return true or false
     */
    public boolean isDown(){
    	return getDefKeyRes("down");
    }
    
    /**
     * Returns the value of the 'left' group
     * @return true or false
     */
    public boolean isLeft(){
    	return getDefKeyRes("left");
    }
    
    /**
     * Returns the value of the 'right' group
     * @return true or false
     */
    public boolean isRight(){
    	return getDefKeyRes("right");
    }

    /**
     * A class designed to hold mouse data
     * @author h2n0
     *
     */
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

    /**
     * A function that sets all key Values to false, mainly for house keeping
     */
    public void releaseAllKeys() {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code > 0 && code < keys.length){
        	keys[code] = true;
        }

    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code > 0 && code < keys.length){
        	keys[code] = false;
        }
    }
    
    public boolean isTyped(int key){
    	if(isKeyPressed(key) && !isPreKeyPressed(key)){
    		return true;
    	}else return false;
    }
    
    public String getKeyTyped(String msg){
    	if(!this.addedKeyboard)return msg;
    	for(int i = 0; i < this.keys.length; i++){
    		if(keys[i] == true){
    			if(i == KeyEvent.VK_SPACE)return msg + " ";
    			else if(i == KeyEvent.VK_BACK_SPACE)return msg.length()!=0?msg.substring(0,msg.length()-1):msg;
    			else if(i == KeyEvent.VK_ENTER)return msg + "\n";
    			else if(i == KeyEvent.VK_SLASH)return msg + "/";
    			else if(i == KeyEvent.VK_BACK_SLASH)return msg+"\\";
    			else if(i == KeyEvent.VK_MINUS)return msg + "-";
    			else if(i == KeyEvent.VK_EQUALS)return msg +"=";
    			else if(i == KeyEvent.VK_PERIOD)return msg + ".";
    			else if(i == KeyEvent.VK_COMMA)return msg + ",";
    			else return isTyped(i)?msg+KeyEvent.getKeyText(i):msg; 
    		}
    	}
    	return msg;
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
    	if(!this.addedMouse)return;
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