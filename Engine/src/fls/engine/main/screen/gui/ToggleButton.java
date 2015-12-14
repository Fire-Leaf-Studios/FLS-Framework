package fls.engine.main.screen.gui;

import fls.engine.main.input.Input;
import fls.engine.main.screen.gui.listener.GUIEvent;
import fls.engine.main.util.Point;

public class ToggleButton extends Button{

	private boolean isToggled;
	public String on;
	public String off;
	private int delay = 0;
	
	public ToggleButton(Point pos) {
		this("False",pos);
	}
	
	public ToggleButton(int x,int y){
		this(new Point(x,y));
	}
	
	public ToggleButton(String s,int x,int y){
		this(s, new Point(x,y));
	}
	
	public ToggleButton(String s, Point p){
		super("",p);
		this.isToggled = false;
		this.on = "True";
		this.off = "False";
	}
	
	public void update(Input i){
		if(this.selected && delay-- <= 0){
			this.manager.fireEvent(new GUIEvent(this,this));
			this.delay = 10;
		}
		if(on != null && off != null)this.label.setText(this.isToggled?on:off);
	}
	
	public boolean isToggled(){
		return this.isToggled;
	}
	
	public void toggle(){
		this.isToggled = !this.isToggled;
		this.selected = false;
	}
}
