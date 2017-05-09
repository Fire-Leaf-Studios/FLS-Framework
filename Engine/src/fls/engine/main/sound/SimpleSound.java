package fls.engine.main.sound;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class SimpleSound implements Runnable{

	private Player player;
	private boolean running;
	
	public SimpleSound(String name){
		this.running = false;
	}
	
	public void play(){
		if(this.running)return;
		run();
	}

	@Override
	public void run() {
		try {
			this.player.play();
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}
}
