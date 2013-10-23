package fls.engine.main.art;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class SplitImage {

    private String loc;
    private int xs, ys;
    private BufferedImage pre_image;
    private BufferedImage[][] images;

    public SplitImage(String loc, int xs, int ys) {
        this.loc = loc;
        this.xs = xs;
        this.ys = ys;
        pre_image=load();
    }

    public SplitImage(String loc) {
        this(loc, -1, -1);
    }

    public BufferedImage getImage() {
        return pre_image;
    }

    public BufferedImage getImage(int x, int y) {
        if (xs == -1 && ys == -1) return getImage();
        return images[x][y];
    }

    /**
     * 
     * @param location
     *            - this is the location that the image is loaded from
     * @return BufferedImage
     */
    public BufferedImage load() {
        try {
            BufferedImage org = ImageIO.read(Art.class.getResource(loc));
            BufferedImage res = new BufferedImage(org.getWidth(), org.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = res.getGraphics();
            g.drawImage(org, 0, 0, null);
            g.dispose();
            return res;
        } catch (Exception e) {
            throw new RuntimeException(loc + " : can't be found");
        }
    }

    public BufferedImage[][] split() {
        BufferedImage src = load();
        int xSlide = src.getWidth() / xs;
        int ySlide = src.getHeight() / ys;
        BufferedImage[][] res = new BufferedImage[xSlide][ySlide];
        for (int x = 0; x < xSlide; x++) {
            for (int y = 0; y < ySlide; y++) {
                res[x][y] = new BufferedImage(xs, ys, BufferedImage.TYPE_INT_ARGB);
                Graphics g = res[x][y].getGraphics();
                g.drawImage(src, -xs * x, -ys * y, null);
                g.dispose();
            }
        }
        return res;
    }

    public BufferedImage[][] altsplit() {
        BufferedImage src = load();
        int xSlide = src.getHeight() / xs;
        int ySlide = src.getWidth() / ys;
        BufferedImage[][] res = new BufferedImage[xSlide][ySlide];
        for (int x = 0; x < xSlide; x++) {
            for (int y = 0; y < ySlide; y++) {
                res[x][y] = new BufferedImage(xs, ys, BufferedImage.TYPE_INT_ARGB);
                Graphics g = res[x][y].getGraphics();
                g.drawImage(src, xs, 0, 0, ys, x * xs, y * ys, (x + 1) * xs, (y + 1) * ys, null);
                g.dispose();
            }
        }
        return res;
    }

    public static BufferedImage scale(BufferedImage src, int scale) {
        int w = src.getWidth() * scale;
        int h = src.getHeight() * scale;
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = res.getGraphics();
        g.drawImage(src.getScaledInstance(w, h, BufferedImage.SCALE_AREA_AVERAGING), 0, 0, null);
        return res;
    }

    public int getXSplit() {
        return xs;
    }

    public int getYSplit() {
        return ys;
    }

}
