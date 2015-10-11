package fls.engine.main.io;

public class AtributeValue {

	
	private String val;
	
	public AtributeValue(String value){
		this.val = value;
	}
	
	public int getInt(){
		return Integer.parseInt(val);
	}
	
	public String getString(){
		return val;
	}
	
	public boolean getBool(){
		if(val.equalsIgnoreCase("true"))return true;
		else return false;
	}
	
	public void setValue(String val){
		this.val = val;
	}
}
