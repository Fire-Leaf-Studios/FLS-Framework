package fls.engine.main.sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound extends Thread{

	
	private String path;
	private Clip clip;
	public AudioInputStream ais;
	private boolean looping = true;
	
	public Sound(String p){
		this.path = p;
		try{
			this.ais = AudioSystem.getAudioInputStream(new File(this.path).getAbsoluteFile());
			this.clip = AudioSystem.getClip();
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("The sound file couldn't be loaded");
		}
	}
	
	public void playSound(){
		this.start();
		this.clip.start();
	}
	
	public void stopSound(){
		this.looping = false;
		this.clip.stop();
		try {
			this.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void loopSound(){
		this.looping = true;
		while(this.looping){
			if(!this.clip.isRunning())playSound();
		}
	}
	
	public void noLoop(){
		this.looping = false;
	}
}
