package fls.engine.main.screen.gui;

import java.awt.Graphics;

import fls.engine.main.art.Art;
import fls.engine.main.art.font.Font;
import fls.engine.main.input.Input;
import fls.engine.main.util.Point;

public class Label extends GUIElement{

	private String text;
	public Label(String s,Point pos) {
		super("id"+s,pos);
		this.text = s;
		this.width = Font.getStringWidth(s);
	}
	
	public Label(String s,int x,int y){
		this(s,new Point(x,y));
	}
	
	@Override
	public void render(Graphics g) {
		Art.drawString(text, g, this.pos.getIX(), this.pos.getIY());
	}
	
	@Override
	public void update(Input i) {
		
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getText(){
		return this.text;
	}

}
