package fls.engine.main;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JFrame;

import fls.engine.main.art.Art;
import fls.engine.main.input.Input;
import fls.engine.main.screen.NonRenderScreen;
import fls.engine.main.screen.Screen;
import fls.engine.main.screen.SplashScreen;
import fls.engine.main.util.DeviceScreen;

@SuppressWarnings("serial")
public class Init extends Canvas implements Runnable {

	public int width = 500;
	public int height;
	public int scale = 1;
	public static int imageScale = 1;
	private boolean running = false;
	public BufferedImage image;
	public BufferedImage icon;
	public Thread thread;
	public String title;
	private boolean printFPS = false;
	private int ticks = 0;
	private int lastFrames;
	public int exFrames;
	public JFrame frame;
	public final String version = "0.4A";
	private String[] creators;

	private boolean isAndroid;
	private boolean isUsingCustom;

	private double desTicks = 60D;
	private Screen screen;
	private SplashScreen splashScreen;
	private boolean splash;
	private Input input;
	public boolean fullScreen;
	public boolean waitingForScreenShot;
	private BufferedImage customCursor;
	
	public Init(String name, int w, int h, boolean f, int iw, int ih){
		this(name, w, h, f);
		useCustomBufferedImage(iw, ih, false);
	}

	public Init(String name, int width, int height) {
		this(name, width, height, false);
	}

	public Init(String name, int w, int h, boolean full) {
		createWindow(name, w, h);
		setVisible(true);
		useCustomBufferedImage(w, h, false);
		this.frame = new JFrame(title);
		frame.add(this);
		frame.setDefaultCloseOperation(3);

		if (full) {
			if(!DeviceScreen.setFullScreen(this)){
				frame.setUndecorated(false);
				frame.pack();
				frame.setVisible(true);
				frame.setResizable(false);
			}else {
				this.fullScreen = true;
			}
			frame.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {
					frame.setAlwaysOnTop(true);
				}

				@Override
				public void focusLost(FocusEvent arg0) {
					frame.setAlwaysOnTop(false);
				}
			});
		} else {
			frame.pack();
			frame.setResizable(false);
			frame.setVisible(true);
		}
		
		if (this.icon != null) {
			frame.setIconImage(this.icon);
		}

		frame.setLocationRelativeTo(null);
		
		this.splash = true;
		// this.setInput(new Input(this, Input.CONTROLLER));
		this.lastFrames = 0;
		this.waitingForScreenShot = false;
		this.setScreen(new SplashScreen());
		//skipInit();
	}

	public Init() {
		this("Default Window", 600, 400);
	}

	public Init(String name, int width) {
		this(name, width, width / 16 * 9);
	}

	public void run() {
		requestFocus();
		
		if(this.screen == null){
			setScreen(new NonRenderScreen());
		}

		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / this.desTicks;
		long lastTimer1 = System.currentTimeMillis();

		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;

			while (unprocessed >= 1) {
				this.ticks++;
				initTick();
				unprocessed -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				initRender();
				this.exFrames++;
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				if(this.printFPS)System.out.println("Frames: " + this.exFrames + ", Ticks: " + this.ticks);
				this.lastFrames = this.exFrames;
				this.exFrames = 0;
				this.ticks = 0;
			}
		}
		stop();
	}

	private final void initTick() {
		if(this.splash) {
			this.splashScreen.inputTick();
			this.splashScreen.update();
			
			if(this.splashScreen.done) this.splash = false;
		}else{ // Update the regular screen
			this.screen.inputTick();
			this.screen.update();
		}
	}

	private final void initRender() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		(this.splash?this.splashScreen:this.screen).render(g);
		g.drawImage(image, 0, 0, width * imageScale, height * imageScale, null);
		g.dispose();
		bs.show();
		
		if(this.waitingForScreenShot){
			Art.saveScreenShot(this, isUsingCustom);
			this.waitingForScreenShot = false;
		}
		this.exFrames++;
	}

	public void altRender() {

	}

	public void start() {
		if (running)
			return;
		//Art.randomColorFont("Splash-random", 50);
		thread = new Thread(this, this.title);
		thread.start();
		running = true;
	}

	public void stop() {
		if (!running)
			return;
		running = false;
		try {
			postClose();
			thread.join();
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void postClose() {
		afterClose();
		if (this.creators != null) {
			System.out.println(title + " made by:");
			if (creators.length > 1) {
				for (int i = 0; i < creators.length - 1; i++) {
					System.out.print(creators[i] + ", ");
				}
			}
			System.out.println(creators[creators.length - 1] + ".");
		}
		System.out.println("");
		System.out.println("[Pixel Chucker] created by Elliot Lee-Cerrino");
		System.exit(0);
	}

	/**
	 * Call if you want to do something when the game is closing
	 */
	public void afterClose() {

	}

	/**
	 * Determines how often the game updates
	 * 
	 * @param ticks
	 */
	public void setDestieredAmtOfTicks(int ticks) {
		desTicks = ticks;
	}

	/**
	 * Replaces the old alterSize method with major improvements<br>
	 * like setting the title
	 * 
	 * @param title
	 * @param width
	 * @param height
	 */
	public void createWindow(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		setTitle(title);
		setSize(width * scale, height * scale);
		setPreferredSize(new Dimension(width * scale, height * scale));
	}

	/**
	 * Use this to change what scale the window is
	 * 
	 * @param s
	 */
	public void setScale(int s) {
		scale = s;
		createWindow(this.title, this.width, this.height);
	}

	/**
	 * Is the exact same method except it sets the height<br>
	 * like so.<br>
	 * height = (width / 12) * 9
	 * 
	 * @param title
	 * @param width
	 */
	public void createWindow(String title, int width) {
		this.title = title;
		this.width = width;
		int height = this.height = (width / 16) * 9;
		setTitle(title);
		setSize(width * scale, height * scale);
		setPreferredSize(new Dimension(width * scale, height * scale));
	}

	public BufferedImage createNewImage(int width, int height, int hints) {
		return new BufferedImage(width, height, hints);
	}

	public void setInput(Input i) {
		this.input = i;
	}

	public void setScreen(Screen s) {
		if (s == null)
			return;
		s.init(this, this.input);
		System.out.println("Loaded a new screen: " + s.getClass().getSimpleName());
		s.postInit();
		
		if(this.splashScreen == null) { // Splash screen
			this.splashScreen = (SplashScreen) s;
		}else {
			this.screen = s;
		}
	}

	/**
	 * 
	 * Used to set the title of the frame
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the icon of the frame
	 * 
	 * @param img
	 */
	public void setIcon(BufferedImage img) {
		this.icon = img;
	}

	/**
	 * 
	 * use if you want the FPS to be outputed to the System.out stream
	 * 
	 * @param show
	 */
	public void showFPS() {
		this.printFPS = true;
	}

	/**
	 * can be used to set the current FPS to a variable
	 * 
	 * @return
	 */
	public int getFPS() {
		return this.lastFrames;
	}

	/**
	 * Checks that there is a BufferStrategy<br>
	 * if there isn't one then it will return false
	 * 
	 * @return true -if found<br>
	 *         false -if not found
	 */
	public boolean BSExists() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			return false;
		}
		return true;
	}

	/**
	 * replace image with a custom sized and type BufferedImage
	 * 
	 * @param x
	 * @param y
	 * @param type
	 */
	public BufferedImage useCustomBufferedImage(int x, int y, boolean alpha) {
		this.isUsingCustom = true;
		BufferedImage b = new BufferedImage(x, y, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		image = b;
		
		if(this.splashScreen != null) {
			this.splashScreen.init(this, input);
			this.splashScreen.postInit();
		}
		
		if(this.screen != null) {
			this.screen.init(this, input);
			this.screen.postInit();
		}
		return b;
	}

	public BufferedImage getImage() {
		return this.image;
	}

	/**
	 * use this to put your's and others names in the output stream when the
	 * game is closed
	 * 
	 * @param name
	 */
	public final void setCreators(String... name) {
		creators = new String[name.length];
		for (int i = 0; i < name.length; i++) {
			creators[i] = name[i];
		}
	}

	public void skipInit() {
		this.splash = false;
	}

	public boolean isCustomImage() {
		return this.isUsingCustom;
	}

	public int getDrawScale() {
		return Init.imageScale;
	}

	public void setImageScale(int i) {
		Init.imageScale = i;
	}

	/**
	 * Gives a way of letting you know if your on android or not
	 * 
	 * @return
	 */
	public boolean isDesktop() {
		return !this.isAndroid;
	}

	public Screen getScreen() {
		return this.screen;
	}
	
	public void hiderCursor(){
		this.setCursor(this.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0,0), ""));
	}
	
	public void showCursor(){
		if(this.customCursor != null)this.setCursor(this.getToolkit().createCustomCursor(customCursor, new Point(0,0), ""));
		else this.setCursor(Cursor.getDefaultCursor());
	}
	
	public void setCursor(int[] data, int xs){
		BufferedImage c = new BufferedImage(xs, xs, BufferedImage.TYPE_INT_RGB);
		c.setRGB(0, 0, xs, xs, data, 0, xs);
		this.setCursor(this.getToolkit().createCustomCursor(c, new Point(0,0), ""));
		this.customCursor = c;
	}
	
	public boolean isFullScreen() {
		return this.fullScreen;
	}
	
	public boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
	
	public boolean isLinux() {
		String os = System.getProperty("os.name").toLowerCase();
		return os.contains("nix") || os.contains("nux") || os.contains("aix");
	}
	
	// Desktop ENV
	public boolean isRunningGnome() {
		String desk  = System.getProperty("sun.desktop").toLowerCase();
		return desk.contains("gnome");
	}
	
	public boolean isMac() {
		return System.getProperty("os.name").toLowerCase().contains("mac");
	}
	
	public void printProperties() {
		Properties props = System.getProperties();
	    Enumeration<?> e = props.propertyNames();
	    while (e.hasMoreElements()) {
	      String key = (String) e.nextElement();
	      System.out.println(key + " -- " + props.getProperty(key) + "\n");
	    }
	}

	public static void main(String[] args) {
		new Init().start();
	}
}
