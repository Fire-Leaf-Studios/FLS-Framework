package fls.engine.main.screen;

import java.awt.Graphics;

import fls.engine.main.util.Font;

public class NonRenderScreen extends Screen{
	
	private int tick;
	
	public void postInit(){
		this.tick = 0;
	}
	
	@Override
	public void update() {
		this.tick = (this.tick + 1) % 180;
	}

	@Override
	public void render(Graphics g) {
		this.r.fill(0);
        String msg = "You need to set a screen before this will work\nCurrent window size: " + this.game.getWidth() + "x" + this.game.getHeight()+"\nCurrent renderer size: " + this.game.image.getWidth() + "x"+this.game.image.getHeight()+"\n\nEnjoy the stats!";
        Font.instance.drawStringCenterWithColor(r, msg, this.tick>90?0xFF0000:0xFFFFFF);
	}
}
