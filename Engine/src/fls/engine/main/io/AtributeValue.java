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
	
	public String[] getValues(){
		int s = this.val.indexOf("[");
		int e = this.val.indexOf("]");
		String[] items = this.val.substring(s + 1, e).split(",");
		int len = items.length;
		String[] res = new String[len];
		for(int i = 0; i < len; i++){
			res[i] = items[i];
		}
		return res;
	}
}
