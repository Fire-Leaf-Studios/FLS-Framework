package fls.engine.main.screen.gui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fls.engine.main.input.Input;
import fls.engine.main.screen.Screen;
import fls.engine.main.util.Point;

public class Container extends GUIElement {
	
	public List<GUIElement> elements = new ArrayList<GUIElement>();
	public List<GUIElement> elementsToAdd = new ArrayList<GUIElement>();
	private HashMap<String,Integer> stringIds = new HashMap<String,Integer>();

	public Container(String id, Point pos) {
		super(id,pos);
	}

	@Override
	public void render(Graphics g) {
		for(GUIElement e: this.elements)e.render(g);
	}

	@Override
	public void update(Input i) {
		int x = i.mouse.getX();
		int y = i.mouse.getY();
		for(GUIElement e: this.elements){
			if(i.leftMouseButton.justClicked())e.checkMousePos(x, y);

			e.update(i);
		}
		
		if(!this.elementsToAdd.isEmpty()){
			for(int j = 0; j < this.elementsToAdd.size(); j++){
				this.elements.add(this.elementsToAdd.remove(j));
			}
		}
	}
	
	public void addComponent(GUIElement g){
		this.addComponent(g,"");
	}
	
	public void addComponent(GUIElement g, String id){
		g.setManager(this.manager);
		elementsToAdd.add(g);
		g.postInit();
	}
	
	public void removeComponent(GUIElement g){
		this.elements.remove(g);
	}
	
	public GUIElement getSelected(){
		for(GUIElement e : elements){
			if(e.selected)return e;
		}
		return null;
	}
	
	public GUIElement getElementById(String id){
		for(GUIElement e : elements){
			if(e.getId() == id)return e;
		}
		return null;
	}
}
