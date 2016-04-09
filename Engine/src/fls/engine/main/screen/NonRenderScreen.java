package fls.engine.main.screen;

import java.awt.Graphics;

import fls.engine.main.art.ABSColor;
import fls.engine.main.art.Art;
import fls.engine.main.input.CustomController;

public class NonRenderScreen extends Screen{
	
	@Override
	public void update() {
		this.input.setPrimaryController(CustomController.a);
		if(this.input.getController() != null){
			System.out.println(this.input.getController().isA());
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void render(Graphics g) {
		Art.fillScreen(this.game, g, ABSColor.black);
        String msg = "You need to set a screen before this will work";
        Art.setTextColor(ABSColor.white);
        Art.drawString(msg, g, (this.game.width / 2) - (msg.length() * Art.FONTSIZE) / 2, this.game.height * this.game.scale / 2 + 3);
	}
}
