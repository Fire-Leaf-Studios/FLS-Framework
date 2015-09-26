package fls.engine.main.input;

import net.java.games.input.Component;
import net.java.games.input.Controller;

public class CustomController {

	private Controller base;
	
	public CustomController(Controller c){
		this.base = c;
	}
	
	private float getComonentData(String s){
		this.base.poll();
		Component[] comps = this.base.getComponents();
		for(Component com : comps){
			if(com.getName() == s)return com.getPollData();
		}
		return 0.0f;
	}
	
	private boolean isButton(String btn){
		return getComonentData(btn)==1.0f?true:false;
	}
	
	public boolean isA(){
		return isButton("A");
	}
	
	public boolean isB(){
		return isButton("B");
	}
	
	public boolean isX(){
		return isButton("X");
	}
	
	public boolean isY(){
		return isButton("Y");
	}
	
	public boolean isStart(){
		return isButton("Start");
	}
	
	public boolean isSelect(){
		return isButton("Select");
	}
}
