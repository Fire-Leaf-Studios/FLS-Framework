package fls.engine.main.screen.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import fls.engine.main.input.Input;
import fls.engine.main.util.Point;

public class Popup extends Container{

	public final int width,height;
	private Color faded;
	public boolean isVisble = false;
	public boolean remove = false;
	
	public List<GUIElement> compnents;
	public Popup() {
		this(100,150);
	}
	
	public Popup(int w,int h){
		super("Popup",null);
		this.width = w;
		this.height = h;
		this.faded = new Color(0,0,0,0.5f);
		this.isVisble = true;
		this.compnents = new ArrayList<GUIElement>();
	}
	
	public void postInit(){
		this.pos = new Point((screenWidth - width) / 2,(screenHeight - height)/2);
	}
	
	public void update(Input i){
		super.update(i);
		for(int j = 0; j < this.compnents.size(); j++)this.compnents.get(j).update(i);
	}
	
	public void addCompnent(GUIElement e,String id){
		this.addComponent(e,id);
	}
	
	public void render(Graphics g){
		g.setColor(faded);
		g.fillRect(0, 0, screenWidth, screenHeight);
		g.setColor(Color.darkGray);
		g.fillRect(this.pos.getIX(), this.pos.getIY(), width, height);
		g.setColor(Color.white);
		g.drawRect(this.pos.getIX() - 1, this.pos.getIY() - 1, width + 1, height + 1);
		super.render(g);
	}
	
	public void addComponentRelativeTo(GUIElement e,String id){
		addCompnent(e,id);
		e.setPos(this.pos.getIX() + e.pos.getIX(), this.pos.getIY() + e.pos.getIY());
	}
	
	public void addComponentRelativeTo(GUIElement e){
		this.addComponentRelativeTo(e, e.getId());
	}
	
	public void close(){
		this.remove = true;
	}
	
	public void toggleVisible(){
		this.isVisble = !this.isVisble;
	}
	
	public void addComponentHorizontalCenterTo(GUIElement e){
		this.addComponentRelativeTo(e);
		e.pos.x = this.pos.x + (this.width/2) - (e.width/2);
	}

}
