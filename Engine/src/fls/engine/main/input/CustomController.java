package fls.engine.main.input;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Rumbler;

public class CustomController {

	private Controller base;
	
	private final boolean xbox;
	
	public static final String start = "Start";
	public static final String select = "6";
	public static final String a = "A";
	public static final String b = "1";
	public static final String x = "2";
	public static final String y = "3";
	public static final String leftTumbStickBtn = "8";
	public static final String rightTunmbStickBtn = "9";
	public static final String leftStickX = "x";
	public static final String leftStickY = "y";
	public static final String rightStickX = "rx";
	public static final String rightStickY = "ry";
	public static final String leftBumper = "5";
	public static final String rightBumper = "4";
	
	public static final String leftTrigger = "z";
	public static final String rightTrigger = "rz";
	
	public static final int povN = 0;
	public static final int povNE = 1;
	public static final int povE = 2;
	public static final int povSE = 3;
	public static final int povS = 4;
	public static final int povSW = 5;
	public static final int povW = 6;
	public static final int povNW = 7;
	
	private final boolean canRumble;
	private boolean connected;
	private int rumbleTimer;
	private Component[] comps;
	
	public CustomController(Controller c, boolean x){
		this.base = c;
		this.xbox = x;
		if(c.getRumblers().length > 0)canRumble = true;
		else canRumble = false;
		this.rumbleTimer = 0;
		this.connected = false;
		this.comps = this.base.getComponents();
		rumble(0.5f, 1f);
	}
	
	private float getComonentData(String s){
		this.connected = this.base.poll(); 
		for(Component com : this.comps){
			if(com.getIdentifier().getName().equals(getConrtollerCorrectButton(s, this.xbox)))return com.getPollData();
		}
		return -1f;
	}
	
	public float getRawComonentData(String s){
		this.connected = this.base.poll();
		for(Component com : this.comps){
			if(com.getIdentifier().getName().equals(getConrtollerCorrectButton(s ,this.xbox)))return com.getPollData();
		}
		return -1;
	}
	
	private boolean isButton(String btn){
		float d = getComonentData(btn);
		return d==1.0f?true:false;
	}
	
	private float getDeadZone(String id){
		Component[] comps = this.base.getComponents();
		for(Component com : comps){
			if(com.getIdentifier().getName().equals(getConrtollerCorrectButton(id,this.xbox))){
				return com.getDeadZone();
			}
		}
		return -1;
	}
	
	public float getData(String name){
		return getComonentData(name);
	}
	
	public boolean isA(){
		return isButton(a);
	}
	
	public boolean isB(){
		return isButton(b);
	}
	
	public boolean isX(){
		return isButton(x);
	}
	
	public boolean isY(){
		return isButton(y);
	}
	
	public boolean isStart(){
		return isButton(start);
	}
	
	public boolean isSelect(){
		return isButton(select);
	}
	
	public boolean isRightStickIn(){
		return isButton(rightTunmbStickBtn);
	}
	
	public boolean isLeftStickIn(){
		return isButton(leftTumbStickBtn);
	}
	
	public boolean isRB(){
		return isButton("Right Thumb");
	}
	
	public boolean isLB(){
		return isButton("Left Thumb");
	}
	
	public float getLeftStickX(){
		float d = getDeadZone(leftStickX);
		float res = getData(leftStickX);
		return res > d ? res : res < -d ? res : 0;
	}
	
	public float getLeftStickY(){
		float d = getDeadZone(leftStickY);
		float res = getData(leftStickY);
		return res > d ? res : res < -d ? res : 0;
	}
	
	public float getLeftTrigger(){
		return getData(leftTrigger);
	}
	
	public boolean isLeftTrigger(){
		return isButton(leftTrigger);
	}
	
	public float getRightStickX(){
		return getData(rightStickX);
	}
	
	public float getRightStickY(){
		return getData(rightStickY);
	}
	
	public float getRightTrigger(){
		return getData(rightTrigger);
	}
	
	public boolean isRightTrigger(){
		return isButton(rightTrigger);
	}
	
	public int getPOV(){
		double n = 0.25;
		double e = 0.5;
		double s = 0.75;
		double w = 1.0;
		float data = getData("pov");
		
		if(data > 0.0 && data < n)return povNW;
		else if(data == n)return povN;
		else if(data > n && data < e)return povNE;
		else if(data == e)return povW;
		else if(data > e && data < s)return povSE;
		else if(data == s)return povS;
		else if(data > s && data < w)return povSW;
		else if(data == w)return povE;
		else return -1;
	}
	
	public void listComponents(){
		this.base.poll();
		for(Component c : this.base.getComponents()){
			System.out.println(c.getIdentifier().getName() + ": "+c.getPollData());
		}
	}
	
	public String getComponentReadout(){
		String res = "";
		this.base.poll();
		for(Component c : this.base.getComponents()){
			res += c.getIdentifier().getName() + ": "+c.getPollData() + "\n";
		}
		return res;
	}
	
	public void update(){
		if(this.rumbleTimer > 0)this.rumbleTimer--;
		else if(this.rumbleTimer == 0){
			this.stopRumble();
			this.rumbleTimer = -1;
		}
	}
	
	public static String getConrtollerCorrectButton(String key, boolean xb){
		if(xb){
			switch(key){
			case leftTrigger:
				return "z";
			case rightTrigger:
				return "z";
			default:
				return key;
			}
		}else{
			switch(key){
			case a:
				return "1";
			case b:
				return "2";
			case x:
				return "0";
			case y:
				return "3";
			case start:
				return "9";
			case select:
				return "8";
			case leftTumbStickBtn:
				return "10";
			case rightTunmbStickBtn:
				return "11";
			case leftBumper:
				return "4";
			case rightBumper:
				return "5";
			default:
				return key;
			}
		}
	}
	
	public void rumble(float strength, float length){
		if(!this.canRumble)return;
		Rumbler[] r = this.base.getRumblers();
		r[0].rumble(strength);
		this.rumbleTimer = (int)(60 * length);
	}
	
	private void stopRumble(){
		if(!this.canRumble)return;
		Rumbler[] r = this.base.getRumblers();
		r[0].rumble(0f);
	}
	
	public boolean connected(){
		return this.connected;
	}
}
