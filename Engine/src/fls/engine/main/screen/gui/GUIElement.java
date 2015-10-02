package fls.engine.main.screen.gui;

import java.awt.Graphics;

import fls.engine.main.input.Input;
import fls.engine.main.util.Point;

public abstract class GUIElement {

	public int id;
	public Point pos;
	public int width,height;
	public boolean selected = false;
	public GUIManager manager;
	
	public GUIElement(Point pos){
		this.pos = pos;
	}
	
	
	public abstract void render(Graphics g);
	
	public abstract void update(Input i);
	
	public void checkMousePos(int x,int y){
		if(x >= this.pos.getIX() && y >= this.pos.getIY() && x <= this.pos.getIX() + this.width && y <= this.pos.getIY() + height){
			this.selected = true;
		}else this.selected = false;
	}
}
