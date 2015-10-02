package fls.engine.main.screen.gui.listener;

import java.util.EventObject;

import fls.engine.main.screen.gui.GUIElement;

@SuppressWarnings("serial")
public class GUIEvent extends EventObject{
	
	private GUIElement type;

	public GUIEvent(Object source, GUIElement e) {
		super(source);
		this.type = e;
		
	}
	
	public GUIElement getElement(){
		return this.type;
	}

}
