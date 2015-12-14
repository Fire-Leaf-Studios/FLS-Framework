package fls.engine.main.screen.gui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fls.engine.main.input.Input;
import fls.engine.main.screen.Screen;
import fls.engine.main.screen.gui.listener.GUIEvent;
import fls.engine.main.screen.gui.listener.GUIListener;

public class GUIManager implements GUIListener{

	
	public List<Container> views;
	public Screen parent;
	public Popup currentPopup;
	public HashMap<String, Container> ids;
	
	public GUIManager(){
		this.views = new ArrayList<Container>();
		this.ids = new HashMap<String,Container>();
	}
	
	public void update(Input i){
		if(currentPopup != null){
			if(currentPopup.isVisble){
				currentPopup.update(i);
				if(currentPopup.remove)this.currentPopup = null;
			}
		}else{
			for(Container c : this.views){
				c.update(i);
			}
		}
	}
	
	public void addContainer(Container c){
		if(c == null)return;
		c.setManager(this);
		if(c instanceof Popup)this.currentPopup = (Popup)c;
		else this.views.add(c);
		c.postInit();
	}
	
	public void removeComponent(Container c){
		this.views.remove(c);
	}
	
	public void render(Graphics g){
		for(Container c : this.views)c.render(g);
		if(this.currentPopup != null)this.currentPopup.render(g);
	}

	@Override
	public void fireEvent(GUIEvent e) {
		this.parent.eventRecieved(e);
	}
	
	public boolean isPopupVisible(){
		if(this.currentPopup == null) return false;
		if(this.currentPopup.isVisble) return true;
		return false;
	}
	
	public GUIElement getElementById(String id){
		for(Container c : this.views){
			for(GUIElement e : c.elements){
				if(e.getId() == id)return e;
			}
		}
		return null;
	}
}
