package fls.engine.main.screen;

import java.awt.Graphics;

import fls.engine.main.util.Font;

public class SplashScreen extends Screen{

	private int ticks;
	public boolean done;
	
	public void postInit() {
		this.done = false;
	}
	
	@Override
	public void update() {
		this.ticks++;
		if (this.ticks > 60 * 2) {
			this.done = true;
		}
	}

	@Override
	public void render(Graphics g) {
		int sy = (this.r.getHeight()) / 2;
		String name = "Pixel Chucker";
		String msg = "Version: " + this.game.version;
		Font.instance.drawStringCenter(r, name, sy-4);
		Font.instance.drawStringCenter(this.r, msg, sy+4);
	}

}
