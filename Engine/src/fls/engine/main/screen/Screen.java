package fls.engine.main.screen;

import java.awt.Graphics;

import fls.engine.main.Init;
import fls.engine.main.input.Input;

public abstract class Screen {

	protected Init game;
	protected Input input;
	
	public final void init(Init game,Input in){
		this.game = game;
		this.input = in;
	}
	
	public abstract void update();
	
	public abstract void render(Graphics g);
	
	public void setScreen(Screen s){
		this.game.setScreen(s);
	}
}
