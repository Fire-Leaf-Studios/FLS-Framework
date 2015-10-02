package fls.engine.main.screen.gui;

import java.awt.Graphics;

import fls.engine.main.art.Art;
import fls.engine.main.input.Input;
import fls.engine.main.util.Point;

public class Label extends GUIElement{

	public String text;
	public Label(String s,Point pos) {
		super(pos);
		this.text = s;
	}
	
	@Override
	public void render(Graphics g) {
		Art.drawString(text, g, this.pos.getIX(), this.pos.getIY());
	}
	
	@Override
	public void update(Input i) {
		
	}

}
