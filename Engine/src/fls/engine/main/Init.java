package fls.engine.main;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import fls.engine.main.art.Sprites;

@SuppressWarnings("serial")
public class Init extends Canvas implements Runnable {

    public static int width = 220;
    public static int height = width / 9 * 12;
    public Input input;
    private boolean running = false;
    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    public BufferedImage icon;
    public Thread thread;
    public String title;
    private boolean started = false;
    private int ticks = 0;
    public final String version = "0.2";

    public Init() {
        setTitle("Default Title");
        setWidth(400);
        setSize(width, height);
        setVisible(true);
        addKeyListener(input);
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
                System.out.println("Frames : " + frames + " Ticks : " + ticks);
                setTitle("LD 26 <->  warm up : " + frames);
                frames = 0;
                ticks = 0;
            }
        }
        stop();
    }

    private void initTick() {
        tick();
        if (!started)
            ticks++;
    }

    public void tick() {

    }

    private void initRender() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        if (started)
            render();
        else
            splash(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    private void splash(Graphics g) {
        g.drawImage(Sprites.splash, 0, 0, getWidth(), getHeight(), null);
        if (ticks > 60 * 4) {
            g.clearRect(0, 0, getWidth(), getHeight());
            started = true;
            ticks = 0;
        }
    }

    public void render() {

    }

    public void start() {
        if (running)
            return;
        thread = new Thread(this, this.title);
        thread.start();
        running = true;
    }

    public void stop() {
        if (!running)
            return;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[2D Engine] created by Elliot Lee-Cerrino");
        System.exit(3);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWidth(int width) {
        Init.width = width;
    }

    public void setIcon(BufferedImage img) {
        this.icon = img;
    }

    public static void main(String[] args) {
        Init game = new Init();
        JFrame frame = new JFrame(game.title);
        frame.add(game);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(3);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(game.image);
        game.start();
    }
}
