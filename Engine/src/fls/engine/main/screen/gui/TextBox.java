package fls.engine.main.screen.gui;

import java.awt.Color;
import java.awt.Graphics;

import fls.engine.main.art.Art;
import fls.engine.main.art.font.Font;
import fls.engine.main.input.Input;
import fls.engine.main.util.Point;

public class TextBox extends GUIElement {
	
	public String text;
	public Label label;
	private int ticks = 0;

	public TextBox(String labelText, Point p) {
		super(p);
		this.height = 18;
		this.width = 100;
		this.label = new Label(labelText,new Point(p.getIX() - Font.getStringWidth(labelText + " "),p.getIY() + (this.height/2) - Art.FONTSIZE/2));
		this.text = "";
	}
	
	public TextBox(Point p){
		this("",p);
	}

	@Override
	public void render(Graphics g) {
		int off = 2;
		g.setColor(Color.white);
		g.fillRect(this.pos.getIX() - off, this.pos.getIY() - off, width + off * 2,height + off * 2);
		g.setColor(Color.black);
		g.fillRect(this.pos.getIX(), this.pos.getIY(), width, height);
		String visibleMessage = text;
		while(Font.getStringWidth(visibleMessage)-3 > this.width - 15){
			visibleMessage = visibleMessage.substring(1);
		}
		Art.drawString(visibleMessage, g, this.pos.getIX() + 3, this.pos.getIY() + (this.height / 2) - (Art.FONTSIZE / 2));
		if(this.selected && ticks%60 > 30)Art.drawString("|", g, this.pos.getIX() + 3 + Font.getStringWidth(visibleMessage), this.pos.getIY() + (this.height / 2) - (Art.FONTSIZE / 2));
		this.label.render(g);
	}

	@Override
	public void update(Input i) {
		if(selected){
			this.text = i.getKeyTyped(text);
			ticks++;
		}
	}

}
