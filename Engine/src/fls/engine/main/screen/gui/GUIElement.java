package fls.engine.main.screen.gui;

import java.awt.Graphics;

import fls.engine.main.input.Input;
import fls.engine.main.util.Point;

public abstract class GUIElement {

	private final String id;
	public Point pos;
	public int width,height;
	public boolean selected = false;
	public GUIManager manager;
	protected int screenWidth, screenHeight;
	
	public GUIElement(String id,Point pos){
		this.pos = pos;
		this.id = id;
	}
	
	public void setManager(GUIManager m){
		this.manager = m;
		this.screenWidth = this.manager.parent.game.width;
		this.screenHeight = this.manager.parent.game.height;
	}
	
	
	public abstract void render(Graphics g);
	
	public abstract void update(Input i);
	
	public void checkMousePos(int x,int y){
		if(x >= this.pos.getIX() && y >= this.pos.getIY() && x <= this.pos.getIX() + this.width && y <= this.pos.getIY() + height){
			this.selected = true;
		}else this.selected = false;
	}
	
	public GUIElement setPos(int x,int y){
		this.pos = new Point(x,y);
		return this;
	}
	
	public void postInit(){
		
	}
	
	public String getId(){
		return this.id;
	}
}
