package fls.engine.main.screen.gui;

import java.awt.Graphics;

import fls.engine.main.input.Input;
import fls.engine.main.util.Point;

public class RadioButtons extends GUIElement {

	public RadioButton[] btns;
	private int currentButton;

	public RadioButtons(String id, Point pos, String... values) {
		super(id, pos);
		this.btns = new RadioButton[values.length];
		for (int i = 0; i < this.btns.length; i++) {
			this.btns[i] = new RadioButton(values[i], new Point(pos.getIX(), pos.getIY() + 16 * i));
		}
	}

	@Override
	public void render(Graphics g) {
		for (int i = 0; i < this.btns.length; i++) {
			this.btns[i].render(g);
		}

	}

	@Override
	public void update(Input in) {
		if(!areAnySelected())this.currentButton = -1;
		for (int i = 0; i < this.btns.length; i++) {
			RadioButton b = this.btns[i];
			b.update(in);
			//if(b.selected)this.currentButton = i;
			//if(i != this.currentButton)b.deselect();
		}
	}
	
	public int getCurrent(){
		return this.currentButton;
	}
	
	private boolean areAnySelected(){
		for(int i = 0; i < this.btns.length; i++){
			if(this.btns[i].isSelected())return true;
		}
		return false;
	}

}
