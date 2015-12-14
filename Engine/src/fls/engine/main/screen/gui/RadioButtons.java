package fls.engine.main.screen.gui;

import java.awt.Graphics;

import fls.engine.main.input.Input;
import fls.engine.main.util.Point;

public class RadioButtons extends GUIElement{
	
	
	private Label[] labels;
	private ToggleButton[] toggels;
	private int current;
	private int pCurrent;

	public RadioButtons(String id, Point pos,String... values) {
		super(id, pos);
		this.current = -1;
		this.labels = new Label[values.length];
		this.toggels = new ToggleButton[values.length];
		for(int i = 0; i < values.length; i++){
			this.labels[i] = new Label(values[i],pos.getIX(),pos.getIY());
			this.toggels[i] = new ToggleButton(pos.getIX(), pos.getIY());
		}
	}

	@Override
	public void render(Graphics g) {
		for(int i = 0; i < this.labels.length; i++){
			this.labels[i].render(g);
			this.toggels[i].render(g);
		}
	}

	@Override
	public void update(Input in) {
		this.pCurrent = this.current;
		for(int i = 0; i < this.toggels.length; i++){
			this.toggels[i].update(in);
			if(this.toggels[i].isToggled())this.current = i;
		}
	}
	
	
	public int getSelectedValue(){
		
	}

}
