package fls.engine.main.screen;

import java.awt.Graphics;

import fls.engine.main.Init;
import fls.engine.main.input.Input;
import fls.engine.main.screen.gui.GUIManager;
import fls.engine.main.screen.gui.listener.GUIEvent;

public abstract class Screen{

	public Init game;
	public Input input;
	public GUIManager manager;
	
	/** Called when the screen is about to be set in the  class
	 * 
	 * @param game
	 * @param in
	 */
	public final void init(Init game,Input in){
		this.manager = new GUIManager();
		this.manager.parent = this;
		this.game = game;
		this.input = in;
	}
	
	public abstract void update();
	
	public abstract void render(Graphics g);
	
	public void setScreen(Screen s){
		this.game.setScreen(s);
	}
	
	public final void inputTick(){
		if(this.input != null)this.input.tick();
	}
	
	public void guiUpdate(){
		manager.update(this.input);
	}
	
	public void guiRender(Graphics g){
		manager.render(g);
	}
	
	public void postInit(){
		
	}
	
	public void eventRecieved(GUIEvent e){
		
	}
}
