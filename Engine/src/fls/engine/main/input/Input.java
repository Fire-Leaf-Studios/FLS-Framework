package fls.engine.main.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fls.engine.main.Init;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;
import net.java.games.input.DirectAndRawInputEnvironmentPlugin;

public class Input implements KeyListener, MouseListener, MouseMotionListener, ControllerListener {

    public static final int KEYS = 0, MOUSE = 1, CONTROLLER = 2;
    private int photoKey;
    public List<Key> keys;
    
    private boolean addedMouse = false;
    private boolean addedKeyboard = false;
    private boolean addedControllers = false;
    private boolean alreadyGotControllers = false;
    private KeyEvent lastKeyPress;
    private boolean lastKeyDown;
    private final int keyDelay = 10;
    private int currentKeyDelay = 0;
    private boolean shifting = false;
    
    private HashMap<String,int[]> preDefs;
    private Controller[] conts;
    private Controller[] controllers;
    private CustomController primaryController;

    public MouseButton leftMouseButton = new MouseButton();
    public MouseButton rightMouseButton = new MouseButton();
    public Mouse mouse = new Mouse(); // the ONLY mouse the get X & Y;
    
    private ControllerEnvironment ce;
    
    
    //Key definitions
    public Key w,a,s,d,e,q;
    public Key up,down,left,right;
    public Key space,esc,z,x,c,shift;
    private Init game;
    private boolean usingImage;

    /**
     * The class that handles all of the input in our games
     * @param game - Any class that extends the Init class
     * @param type - Any number of identifiers set from {@link #KEYS},{@link #MOUSE} and {@link #CONTROLLER}
     */
    public Input(Init game, int... type) {
    	for(int i : type)
        switch (i) {
        case KEYS:
        	if(this.preDefs != null)break;
            System.out.println("Added Key input");
            game.addKeyListener(this);
            this.keys = new ArrayList<Key>();
            this.preDefs = new HashMap<String,int[]>();
            this.addedKeyboard = true;
            
            this.w = new Key(this,KeyEvent.VK_W);
            this.a = new Key(this,KeyEvent.VK_A);
            this.s = new Key(this,KeyEvent.VK_S);
            this.d = new Key(this,KeyEvent.VK_D);
            this.e = new Key(this,KeyEvent.VK_E);
            this.q = new Key(this,KeyEvent.VK_Q);
            
            this.up = new Key(this,KeyEvent.VK_UP);
            this.down = new Key(this,KeyEvent.VK_DOWN);
            this.left = new Key(this,KeyEvent.VK_LEFT);
            this.right = new Key(this,KeyEvent.VK_RIGHT);
            
            this.space = new Key(this,KeyEvent.VK_SPACE);
            this.esc = new Key(this,KeyEvent.VK_ESCAPE);
            this.z = new Key(this,KeyEvent.VK_Z);
            this.x = new Key(this,KeyEvent.VK_X);
            this.c = new Key(this,KeyEvent.VK_C);
            this.shift = new Key(this,KeyEvent.VK_SHIFT);
            setScreenshotKey(KeyEvent.VK_P, game.isCustomImage());
            break;
        case MOUSE:
        	this.addedMouse = true;
            System.out.println("Added Mouse input");
            game.addMouseListener(this);
            game.addMouseMotionListener(this);
            break;
        case CONTROLLER:
        	this.addedControllers = true;
            System.out.println("Added Controller input");
            this.scanForControllers(true);
            break;
        default:
            System.err.println("Not a valid type entered");
            break;
        }
    	this.game = game;
    }
    
    
    /**
     * A function called to set the primary controller, useful for things like start screens
     */
    public void setPrimaryController(String key){
    	if(!this.addedControllers || this.primaryController != null)return;
    	this.scanForControllers(false);
    	for(Controller c : this.controllers){
    		c.poll();
    		Component[] comps = c.getComponents();
    		System.out.println("---------");
    		for(Component comp : comps){
    			System.out.println(comp.getIdentifier().getName() + " : " + comp.getPollData());
    			if(c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK){
    				String xb = CustomController.getConrtollerCorrectButton(key, true);
    				String nxb = CustomController.getConrtollerCorrectButton(key, false);
    				String n = comp.getIdentifier().getName(); 
    				if(n.equals(xb) || n.equals(nxb)){
	    				if(comp.getPollData() == 1.0f){
	    					if(n == xb){
	    						this.primaryController = new CustomController(c,true);
	    					}else{
	    						this.primaryController = new CustomController(c,false);
	    					}
	    					return;
	    				}
    				}
    			}
    		}
    		System.out.println("---------");
    	}
    }
    
    public void listControllers(){
    	if(!this.addedControllers)return;
		System.out.println("---------");
    	for(Controller c : this.controllers){
    		System.out.println(c.getName() + " : " + c.getPortNumber());
    	}
    	System.out.println("");
    }
    
    public void setPrimaryControllerByName(String name, boolean xbox){
    	if(!this.addedControllers || this.primaryController != null)return;
    	this.scanForControllers(false);
    	for(Controller c : this.controllers){
    		if(c.getName().equals(name)){
    			this.primaryController = new CustomController(c, xbox);
    			return;
    		}
    	}
    }
    
    public void scanForControllers(boolean names){
    	if(!this.addedControllers || this.alreadyGotControllers)return;
    	
    	DirectAndRawInputEnvironmentPlugin directEnv = new DirectAndRawInputEnvironmentPlugin();
    	if(this.ce.isSupported()){
    		System.out.println("Using DIRECT env!");
    		this.conts = directEnv.getControllers();
    	}else{
    		this.ce = ControllerEnvironment.getDefaultEnvironment();
            this.conts = this.ce.getControllers();
    	}
        this.ce.addControllerListener(this);
    	
    	int num = 0;
        for(Controller c : conts){
        	if(c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK){
        		if(names) System.out.println(c.getName());
        		num++;
        	}
        }
        
        this.controllers = new Controller[num];
        
        num = 0;
        for(Controller c : conts){
        	if(c.getType() == Controller.Type.GAMEPAD || c.getType() == Controller.Type.STICK){
        		this.controllers[num] = c;
        		num++;
        	}
        }
    }
    
    public void showData(Controller c){
    	if(!c.poll()){
        	System.out.println("--------------");
        	System.out.println(c.getName() + " -  DISCONNECTED");
        	System.out.println("--------------");
        	return;
    	}
    	
    	Component[] comps = c.getComponents();
    	System.out.println("--------------");
    	System.out.println(c.getName());
		for(Component comp : comps){
			System.out.println(comp.getIdentifier().getName() + ":" + comp.getPollData());
		}
    	System.out.println("--------------");
    }
    
    public void setPrimaryControllerWithStart(){
    	this.setPrimaryController(CustomController.start);
    }
    
    public CustomController getController(){
    	return this.primaryController;
    }
    
    public void tick(){
    	if(this.addedKeyboard){
    		for(Key k : this.keys){
    			k.tick();
    		}
    		if(currentKeyDelay > 0)currentKeyDelay--;
    	}
    	
    	if(this.addedMouse){
    		leftMouseButton.tick();
    		rightMouseButton.tick();
    	}
    }

    /**
     * A small function that sets the 'Screen-cap' key
     * @param key
     */
    public void setScreenshotKey(int key, boolean image) {
        this.photoKey = key;
        this.usingImage = image;
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
     * @param i - The key value
     * @return boolean
     */
    public boolean isKeyPressed(Key k){
    	if(!this.addedKeyboard)return false;
    	else return k.clicked;
    }
    
    /**
     * Helper function to check if a key is being pressed
     * @param i - The key value
     * @return boolean
     */
    public boolean isKeyHeld(Key k){
    	if(!this.addedKeyboard)return false;
    	return k.down;
    }

    /**
     * A class that holds mouse data such as its x&y cords, dx&dy mouse drag cords
     * @author h2n0
     *
     */
    public class Mouse {
        private int x, y, dx, dy;
        private boolean beingDraged = false;

        /** 
         * Returns the mouses 'x' value with respect to the game frame 
         * @return x
         */
        public int getX() {
            return x;
        }

        /**
         * Returns the mouses 'y' value with respect to the game frame
         * @return y
         */
        public int getY() {
            return y;
        }

        /**
         * Returns the mouses 'dx' position which is
         * {@link Input.Mouse#getX()} + the difference between the new and old mouse position 
         * @return dx
         */
        public int getDX() {
            return getX() + dx;
        }

        /**
         * Returns the mouses 'dy' position which is
         * {@link Input.Mouse#getY()} + the difference between the new and old mouse position 
         * @return dy
         */
        public int getDY() {
            return getY() + dy;
        }

        /**
         * Returns whether the mouse is being dragged or not
         * @return boolean
         */
        public boolean beingDragged() {
            return beingDraged;
        }
    }
    
    /**
     * A class that held mouse button data
     * @author h2n0
     *
     */
    public class MouseButton{
    	private boolean clicked = false;
    	private boolean lastState = false;
    	
    	public void toggle(boolean click){
    		this.clicked = click;
    	}
    	
    	public boolean isHeld(){
    		if(this.lastState == false && this.clicked == false)return false;
    		return this.clicked == this.lastState;
    	}
    	
    	public boolean justClicked(){
    		return this.clicked;// && !this.lastState;
    	}
    	
    	public void tick(){
    		this.lastState = this.clicked;
    	}
    	
    	public void flush(){
    		this.clicked = false;
    		this.lastState = false;
    	}
    
    }
    
    

    /**
     * A function that sets all key Values to false, mainly for house keeping
     */
    public void releaseAllKeys() {
    	if(this.addedKeyboard){
	    	for(Key k : this.keys){
	    		k.toggle(false);
	    	}
	    	this.lastKeyDown = false;
    	}
    }
    
    /**
     * A function that "flushes" the input que of the 2 mouse button
     */
    public void relaseMouseButtons(){
    	if(this.addedMouse){
    		this.leftMouseButton.flush();
    		this.rightMouseButton.flush();
    	}
    }
    
    public String getKeyTyped(String msg){
    	if(!this.addedKeyboard)return msg;
    	if(this.lastKeyPress == null || currentKeyDelay > 0)return msg;
    	for(int i = 0; i < KeyEvent.KEY_LAST; i++){
    		if(lastKeyDown && i == this.lastKeyPress.getKeyCode()){
    			String res = msg;
    			if(i == KeyEvent.VK_SPACE)res = res + " ";
    			else if(i == KeyEvent.VK_BACK_SPACE){
    				this.currentKeyDelay = this.keyDelay / 2;
    				return msg.length()!=0?msg.substring(0,msg.length()-1):msg;
    			}
    			else if(i == KeyEvent.VK_ENTER)return msg + "\n";
    			else if(i == KeyEvent.VK_SLASH){
    				if(shifting){
    					res = res + "?";
    				}else res = res + "/";
    			}
    			else if(i == KeyEvent.VK_BACK_SLASH)res = res + "\\";
    			else if(i == KeyEvent.VK_MINUS){
    				if(shifting){
    					res = res + "_";
    				}else res = res + "-";
    			}
    			else if(i == KeyEvent.VK_EQUALS){
    				if(shifting)res = res + "+";
    				else res = res + "=";
    			}
    			else if(i == KeyEvent.VK_PERIOD){
    				if(shifting)res = res + ">";
    				else res = res + ".";
    			}
    			else if(i == KeyEvent.VK_COMMA){
    				if(shifting)res = res + "<";
    				else res = res +",";
    			}
    			else if(i == KeyEvent.VK_SHIFT)return res;
    			else res = res + KeyEvent.getKeyText(i);
    			shifting = false;
    	    	currentKeyDelay = this.keyDelay;
    			return res;
    		}
    	}
    	return msg;
    }

    public void keyTyped(KeyEvent e) {
    	this.game.getScreen().keyTyped(e.getKeyChar());
    }

    public void mouseClicked(MouseEvent arg0) {

    }

    public void mouseEntered(MouseEvent arg0) {

    }

    public void mouseExited(MouseEvent arg0) {

    }

    public void mousePressed(MouseEvent e) {
        mouse.dx = e.getPoint().x - mouse.x;
        mouse.dy = e.getPoint().y - mouse.y;
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
        mouse.x = e.getX();
        mouse.y = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
        mouse.x = e.getX();
        mouse.y = e.getY();
    }


	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == this.photoKey && !this.game.waitingForScreenShot){
			this.game.waitingForScreenShot = true;
			return;
		}
		if(e.getKeyCode() == KeyEvent.VK_SHIFT)shifting = true;
		toggle(e.getKeyCode(),true);
		lastKeyPress = e;
		lastKeyDown = true;
	}


	@Override
	public void keyReleased(KeyEvent e) {
		toggle(e.getKeyCode(),false);
		lastKeyDown = false;
	}
	
	
	private void toggle(int ke, boolean p){
		for(int i = 0; i < this.keys.size(); i++){
			Key k = this.keys.get(i);
			if(k.key == ke)k.toggle(p);
		}
		/**if(ke == this.w.key)this.w.toggle(p);
		if(ke == this.s.key)this.s.toggle(p);
		if(ke == this.a.key)this.a.toggle(p);
		if(ke == this.d.key)this.d.toggle(p);
		
		if(ke == this.up.key)this.up.toggle(p);
		if(ke == this.down.key)this.down.toggle(p);
		if(ke == this.left.key)this.left.toggle(p);
		if(ke == this.right.key)this.right.toggle(p);
		
		if(ke == this.space.key)this.space.toggle(p);
		if(ke == this.esc.key)this.esc.toggle(p);
		if(ke == this.z.key)this.z.toggle(p);
		if(ke == this.x.key)this.x.toggle(p);
		if(ke == this.c.key)this.c.toggle(p);
		if(ke == this.photoKey.key)this.photoKey.toggle(p);
		if(ke == this.shift.key)this.shift.toggle(p);**/
	}
	
	public void addKey(Key k){
		this.keys.add(k);
	}


	@Override
	public void controllerAdded(ControllerEvent e) {
		System.out.println(e.getController().getName());
	}


	@Override
	public void controllerRemoved(ControllerEvent e) {
		System.out.println(e.getController().getName());
	}
	
	public Controller[] getAllControllers(){
		if(!this.addedControllers)return null;
		scanForControllers(false);
		return this.controllers;
	}
}