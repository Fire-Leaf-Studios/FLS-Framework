package fls.engine.main.screen.gui;

import java.awt.Color;
import java.awt.Graphics;

import fls.engine.main.art.Art;
import fls.engine.main.art.font.Font;
import fls.engine.main.input.Input;
import fls.engine.main.screen.gui.listener.GUIEvent;
import fls.engine.main.util.Point;

public class Button extends GUIElement{

	private Label label;
	
	public Button(String text,Point pos) {
		super(pos);
		this.label = new Label(text,pos);
		this.width = 50;
		this.height = 10;
	}

	@Override
	public void render(Graphics g) {
		int off = 2;
		g.setColor(Color.white);
		g.fillRect(this.pos.getIX() - off, this.pos.getIY() - off, width + off * 2,height + off * 2);
		g.setColor(Color.black);
		g.fillRect(this.pos.getIX(), this.pos.getIY(), width, height);
		Art.drawString(label.text, g, this.pos.getIX() + (width/2) - Font.getStringWidth(label.text)/2,this.pos.getIY() + (height/2) - (Art.FONTSIZE/2));
		
	}

	@Override
	public void update(Input i) {
		if(this.selected){
			this.manager.fireEvent(new GUIEvent(this,this));
			this.selected = false;
		}
		
	}

}
