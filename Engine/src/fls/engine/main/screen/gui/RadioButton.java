package fls.engine.main.screen.gui;

import java.awt.Color;
import java.awt.Graphics;

import fls.engine.main.input.Input;
import fls.engine.main.screen.gui.listener.GUIEvent;
import fls.engine.main.util.Point;

public class RadioButton extends GUIElement{
	
	private final Label label;
	private boolean isSelected;
	private int delay;
	public int id;

	public RadioButton(String label, Point pos) {
		super("Rad", pos);
		this.width = 10;
		this.height = 10;
		this.label = new Label(label,new Point(pos.getIX() + width + 5,pos.getIY() + (this.height - 6) / 2));
		this.delay = 0;
	}

	@Override
	public void render(Graphics g) {
		this.label.render(g);
		
		int off = 2;
		g.setColor(Color.white);
		g.fillRect(this.pos.getIX() - off, this.pos.getIY() - off, width + off * 2,height + off * 2);
		g.setColor(this.isSelected?Color.green:Color.black);
		g.fillRect(this.pos.getIX(), this.pos.getIY(), width, height);
	}

	@Override
	public void update(Input i) {
		if(delay > 0)delay--;
		if(this.selected && delay == 0){
			this.isSelected = true;
			this.selected = false;
			this.manager.fireEvent(new GUIEvent(this.manager,this));
			this.delay = 10;
		}
	}
	
	public boolean isSelected(){
		return this.isSelected;
	}
	
	public void deselect(){
		this.isSelected = false;
	}
	
	
	

}
