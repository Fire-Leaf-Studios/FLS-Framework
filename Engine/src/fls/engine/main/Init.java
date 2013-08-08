package fls.engine.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import fls.engine.main.art.Art;
import fls.engine.main.input.Input;

@SuppressWarnings("serial")
public class Init extends Canvas implements Runnable {

    public static int width = 400;
    public static int height = width / 12 * 9;
    public Input input;
    private boolean running = false;
    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    public BufferedImage icon;
    public Thread thread;
    public String title;
    private boolean started = false;
    private boolean printFPS = false;
    private int ticks = 0;
    public int exframes;
    public final String version = "0.3.2";

    public Init() {
        setTitle("Default title");
        setSize(width, height);
        setVisible(true);
    }

    public void run() {
        requestFocus();
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;

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
        if (started) tick();
        if (!started) ticks++;
    }

    public void tick() {

    }

    private final void initRender() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        if (started) render(g);
        else splash(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    private void splash(Graphics g) {
        BufferedImage img = null;
        Art.fillScreen(this, g, Color.black);
        try {
            img = ImageIO.read(Init.class.getResource("/Splash.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.drawImage(img, (width / 2) - (img.getWidth() / 2), (height / 2) - (img.getHeight() / 2), img.getWidth(), img.getHeight(), null);
        String msg = "Version :" + version;
        Art.drawWString(msg, g, (getWidth() / 2) - msg.length() * 2, getHeight() - 75);
        if (ticks > 60 * 4) {
            Art.fillScreen(this, g, Color.black);
            started = true;
            ticks = 0;
        }
    }

    public void render(Graphics g) {
        Art.fillScreen(this, g, Color.black);
        String msg = "You haven't used render yet";
        Art.drawWString(msg, g, width / 2 - msg.length() * 2 - 20, height / 2 + 3);
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
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[2D Engine] created by Elliot Lee-Cerrino");
        System.exit(0);
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
        setSize(width, height);
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
        setSize(width, height);
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
    public boolean checkBS() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) return false;
        return true;
    }

    public static void main(String[] args) {
        Init game = new Init();
        JFrame frame = new JFrame(game.title);
        frame.add(game);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(3);
        frame.setLocationRelativeTo(null);
        if (game.icon != null) {
            frame.setIconImage(game.icon);
        }
        game.start();
    }
}
