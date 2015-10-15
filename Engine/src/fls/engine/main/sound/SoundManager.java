package fls.engine.main.sound;

import java.util.HashMap;


class SoundManager{
	
	public static SoundManager instance = new SoundManager();
	
	public HashMap<String,Sound> cont;
	
	public SoundManager(){
		this.cont = new HashMap<String,Sound>();
	}
	
	public void addSound(String name,String pos){
		this.cont.put(name, new Sound(pos));
	}
	
	public void playSound(String n){
		getSound(n).playSound();
	}
	
	public void stopSound(String n){
		getSound(n).stopSound();
	}
	
	public void loopSound(String n){
		getSound(n).loopSound();
	}
	
	private Sound getSound(String n){
		return this.cont.get(n);
	}
	
	public void stopAll(){
		Object[] a = this.cont.keySet().toArray();
		for(int i = 0; i < this.cont.size(); i++){
			String key = ""+a[i];
			this.cont.get(key).stopSound();
		}
	}
	
}