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
	
	private float getData(String name){
		return getComonentData(name);
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
	
	public boolean isXBoxBtn(){
		return isButton("Mode");
	}
	
	public boolean isRightStickIn(){
		return isButton("Right Thumb 3");
	}
	
	public boolean isLeftStickIn(){
		return isButton("Left Thumb 3");
	}
	
	public boolean isRB(){
		return isButton("Right Thumb");
	}
	
	public boolean isLB(){
		return isButton("Left Thumb");
	}
	
	public float getLeftStickX(){
		return getData("x");
	}
	
	public float getLeftStickY(){
		return getData("y");
	}
	
	public float getLeftTrigger(){
		return getData("z");
	}
	
	public boolean isLeftTrigger(){
		return isButton("z");
	}
	
	public float getRightStickX(){
		return getData("rx");
	}
	
	public float getRightStickY(){
		return getData("ry");
	}
	
	public float getRightTrigger(){
		return getData("rz");
	}
	
	public boolean isRightTrigger(){
		return isButton("rz");
	}
	
	public int getPOV(){
		double n = 0.25;
		double e = 0.5;
		double s = 0.75;
		double w = 1.0;
		float data = getData("pov");
		
		if(data > 0.0 && data < n)return 0;
		else if(data == n)return 1;
		else if(data > n && data < e)return 2;
		else if(data == e)return 3;
		else if(data > e && data < s)return 4;
		else if(data == s)return 5;
		else if(data > s && data < w)return 6;
		else if(data == w)return 7;
		else return -1;
	}
	
	public void listComponents(){
		this.base.poll();
		for(Component c : this.base.getComponents()){
			System.out.println(c.getName() + ": "+c.getPollData());
		}
	}
	
	public String getCoponentReadout(){
		String res = "";
		this.base.poll();
		for(Component c : this.base.getComponents()){
			res += c.getName() + ": "+c.getPollData();
		}
		return res;
	}
}
