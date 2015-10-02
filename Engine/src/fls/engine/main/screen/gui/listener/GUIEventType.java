package fls.engine.main.screen.gui.listener;

public class GUIEventType {

	public static final GUIEventType buttonPressed = new GUIEventType("buttonPressed");
	
	
	private String type;
	
	private GUIEventType(String name){
		this.type = name;
	}
	
	public String toString(){
		return this.type;
	}
}
