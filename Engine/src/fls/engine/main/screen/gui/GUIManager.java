package fls.engine.main.screen.gui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import fls.engine.main.input.Input;
import fls.engine.main.screen.Screen;
import fls.engine.main.screen.gui.listener.GUIEvent;
import fls.engine.main.screen.gui.listener.GUIListener;

public class GUIManager implements GUIListener{

	public List<GUIElement> elements = new ArrayList<GUIElement>();
	public Screen parent;
	
	
	public void update(Input i){
		for(GUIElement e : elements){
			int x = i.mouse.getX();
			int y = i.mouse.getY();
			if(i.leftMouseButton.isClicked())e.checkMousePos(x, y);

			e.update(i);
		}
	}
	
	public void render(Graphics g){
		for(GUIElement e : elements)e.render(g);
	}
	
	public void addCompenent(GUIElement g){
		g.id = elements.size();
		g.manager = this;
		elements.add(g);
	}
	
	public GUIElement getSelected(){
		for(GUIElement e : elements){
			if(e.selected)return e;
		}
		return null;
	}
	
	public GUIElement getElementByID(int id){
		for(GUIElement e : elements){
			if(e.id == id)return e;
		}
		return null;
	}

	@Override
	public void fireEvent(GUIEvent e) {
		this.parent.eventRecieved(e);
	}
}
