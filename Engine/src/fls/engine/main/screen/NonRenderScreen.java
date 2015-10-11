package fls.engine.main.screen;

import java.awt.Color;
import java.awt.Graphics;

import fls.engine.main.art.ABSColor;
import fls.engine.main.art.Art;

public class NonRenderScreen extends Screen{

	
	@Override
	public void update() {
		
	}

	@Override
	public void render(Graphics g) {
		Art.fillScreen(this.game, g, Color.black);
        String msg = "You need to set a screen before this will work";
        Art.setTextColor(ABSColor.white);
        Art.drawString(msg, g, (this.game.width / 2) - (msg.length() * Art.FONTSIZE) / 2, this.game.height * this.game.scale / 2 + 3);
	}
}
