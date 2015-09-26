package fls.engine.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import fls.engine.main.art.Art;
import fls.engine.main.art.SplitImage;
import fls.engine.main.input.Input;
import fls.engine.main.screen.NonRenderScreen;
import fls.engine.main.screen.Screen;

@SuppressWarnings("serial")
public class Init extends Canvas implements Runnable {

    public static int width = 500;
    public static int height;
    public static int scale = 1;
    private boolean running = false;
    public BufferedImage image;
    public BufferedImage icon;
    public Thread thread;
    public String title;
    private boolean started = false;
    private boolean printFPS = false;
    private boolean skipInit = false;
    private int ticks = 0;
    public int exframes;
    public JFrame frame;
    public final String version = "0.3.6";
    private String[] creators;

    private double desTicks = 60D;
    private Screen screen;
    private Input input;

    public Init(String name,int width,int height) {
    	 createWindow("Default window", width,height);
         setVisible(true);
         image = createNewImage(width, height, 2);
    	 this.frame = new JFrame(title);
         // frame.setPreferredSize(new Dimension(Init.width * Init.scale, Init.height * Init.scale));
         frame.setResizable(false);
         frame.add(this);
         frame.pack();
         frame.setVisible(true);
         frame.setDefaultCloseOperation(3);
         frame.setLocationRelativeTo(null);
         if (this.icon != null) {
             frame.setIconImage(this.icon);
         }
         setScreen(new NonRenderScreen());
         this.start();
    }
    
    public Init(){
    	this("Default Window",600,400);
    }
    
    public Init(String name,int width){
    	this(name,width,width/16 * 9);
    }

    public void run() {
        requestFocus();
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / desTicks;

        int ticks = 0;
        int frames = 0;
        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        System.out.println("Made with [2D Engine]");

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
                ticks++;
                initTick();
                delta -= 1;
                shouldRender = true;
            }

            try {
                Thread.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (shouldRender) {
                frames++;
                initRender();
            }
            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                exframes = frames;
                if (printFPS) {
                    System.out.println("Frames : " + frames + " Ticks : " + ticks);
                }
                frames = 0;
                ticks = 0;
            }
        }
        stop();
    }

    private final void initTick() {
        if (!skipInit) {
            if (started) screen.update();
            if (!started) ticks++;
        } else {
            screen.update();
        }
    }

    private final void initRender() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        if (!skipInit) {
            if (started) this.screen.render(g);
            else splash(g);
        } else {
            this.screen.render(g);
        }
        g.drawImage(image, 0, 0, width * scale, height * scale, null);
        g.dispose();
        bs.show();
    }

    public void altRender() {

    }

    private BufferedImage splash;

    private void splash(Graphics g) {
        if (splash == null) splash = new SplitImage("/Splash.png").load();
        Art.fillScreen(this, g, Color.black);
        g.drawImage(splash, (width * scale / 2) - (splash.getWidth() / 2), (height * scale / 2) - (splash.getHeight() / 2), null);
        String msg = "Version :" + version;
        Art.setTextCol(Color.white);
        Art.drawString(msg, g, (getWidth() / 2) - msg.length() * 2, getHeight() - 75);
        if (ticks > 60 * 4) {
            Art.fillScreen(this, g, Color.black);
            started = true;
            ticks = 0;
        }
    }

    public void start() {
        if (running) return;
        thread = new Thread(this, this.title);
        thread.start();
        running = true;
    }

    public void stop() {
        if (!running) return;
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
        if (creators.length != 0) {
            System.out.println(title + " made by:");
            if (creators.length > 1) {
                for (int i = 0; i < creators.length - 1; i++) {
                    System.out.print(creators[i] + ", ");
                }
            }
            System.out.println(creators[creators.length - 1] + ".");
        }
        System.out.println("");
        System.out.println("[2D Engine] created by Elliot Lee-Cerrino");
        System.exit(0);
    }

    /**
     * Call if you want to do somthing will the game is closing
     */
    public void afterClose() {

    }

    /**
     * determains how often the game updates
     * 
     * @param ticks
     */
    public void setDestieredAmtOfTicks(int ticks) {
        desTicks = ticks;
    }

    /**
     * Replaces the old alterSize method with major improvments<br>
     * like setting the title
     * 
     * @param title
     * @param width
     * @param height
     */
    public void createWindow(String title, int width, int height) {
        this.title = title;
        Init.width = width;
        Init.height = height;
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
        Init.width = width;
        int height = Init.height = (width / 12) * 9;
        setTitle(title);
        setSize(width * scale, height * scale);
        setPreferredSize(new Dimension(width * scale, height * scale));
    }

    public BufferedImage createNewImage(int width, int height, int hints) {
        return new BufferedImage(width, height, hints);
    }
    
    public void setScreen(Screen s){
    	if(s == null)return;
    	s.init(this, this.input);
    	this.screen = s;
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
        printFPS = true;
    }

    /**
     * can be used to set the current FPS to a variable
     * 
     * @return
     */
    public int getFPS() {
        return exframes;
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
     * replace image with a custom sied and type BufferedImage
     * 
     * @param x
     * @param y
     * @param type
     */
    public void useCustomBufferedImage(int x, int y, int type) {
        BufferedImage b = new BufferedImage(x, y, type);
        image = b;
    }

    /**
     * use this to put your's and others names in the output stream when the game is closed
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
        skipInit = true;
    }

    public static void main(String[] args) {
        new Init();
    }
}
