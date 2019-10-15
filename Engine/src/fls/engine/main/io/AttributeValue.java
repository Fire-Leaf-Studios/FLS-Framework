package fls.engine.main.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttributeValue {

	
	private String val;
	private boolean isArray;
	private boolean isObject;
	private DataFile df;
	private HashMap<String, AttributeValue> properties;
	
	public AttributeValue(DataFile df, String value){
		this.df = df;
		setValue(value.trim());
	}
	
	public int getInt(){
		return Integer.parseInt(val);
	}
	
	public float getFloat(){
		return Float.parseFloat(val);
	}
	
	public String getString(){
		if(this.isObject && !this.isArray){
			String[] keys  = new String[this.properties.size()];
			this.properties.keySet().toArray(keys);
			String res = "{";
			for(int i = 0; i < keys.length; i++){
				res += keys[i] + ":" +this.properties.get(keys[i]).getString() + ",";
			}
			res = res.substring(0, res.length()-1)+"}";
			return res.trim();
		}else if(this.isObject && this.isArray){
			String[] keys  = new String[this.properties.size()];
			this.properties.keySet().toArray(keys);
			String res = "{";
			for(int i = 0; i < keys.length; i++){
				res += keys[i] + ":" +this.properties.get(keys[i]).getString() + ",";
			}
			res = res.substring(0, res.length()-1)+"}";
			return res.trim();
		}else{
			return val;
		}
	}
	
	public boolean getBool(){
		if(val.equalsIgnoreCase("true") || val.equalsIgnoreCase("t"))return true;
		else return false;
	}
	
	public void setValue(String val){
		this.val = val;
		initialise(val);
	}
	
	public String[] getValues(){
		if(!this.isArray)return new String[]{getString()};
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
	
	public String getValue(int i){
		String[] v = getValues();
		return v[i];
	}
	
	public AttributeValue getProperty(String name){
		if(!this.isObject)this.properties.put(name, new AttributeValue(df, ""));
		return this.properties.get(name);
	}
	
	public void addProperty(String key, String val){
		key = key.trim();
		val = val.trim();
		if(!this.isObject){
			this.isObject = true;
			this.properties = new HashMap<String, AttributeValue>();
		}
		this.properties.put(key, new AttributeValue(this.df, val));
		
		if(this.properties.keySet().contains(key))return;
		
		String[] keys  = new String[this.properties.size()];
		this.properties.keySet().toArray(keys);
		String res = "{";
		for(int i = 0; i < keys.length; i++){
			res += keys[i] + ":" +this.properties.get(keys[i]).getString() + ",";
		}
		res = res.substring(0, res.length()-1)+"}";
		setValue(res.trim());
	}
	
	private void initialise(String value){
		if(value.indexOf("[") == 0){//Looks like we have an array
			this.isArray = true;
		}
		
		if(value.indexOf("{") != -1){//Looks like we have a object somewhere in here
			this.isObject = true;
			this.properties = new HashMap<String, AttributeValue>();
			if(isArray){
				int s = this.val.indexOf("[");
				int e = this.val.lastIndexOf("]");
				value = this.val.substring(s + 1, e);
				System.out.println(value);
				List<String> items = new ArrayList<String>();
				boolean inside = false;
				int st = 0;
				for(int i = 0; i < value.length(); i++){
					 char c = value.charAt(i);
					 if(c == "{".charAt(0)){
						 if(!inside){
							 inside = true;
							 st = i+1;
							 continue;
						 }
					 }else if(c == "}".charAt(0)){
						 if(inside){
							 inside = false;
							 items.add(value.substring(st, i+2));
							 i += 2;
							 st = i;
							 continue;
						 }
					 }
				}
				
				items.add(value.substring(st));
				
				for(int i = 0; i < items.size(); i++){
					String citem = items.get(i).trim();
					System.out.println(citem);
					citem = citem.substring(citem.indexOf("{")+1, citem.lastIndexOf("}"));
					String[] parts = citem.split(",");
					for(int j = 0; j < parts.length; j++){
						String cc = parts[j];
						int col = cc.indexOf(":");
						String name = cc.substring(0, col);
						String val = cc.substring(col+1);
						this.properties.put(name, new AttributeValue(this.df, val));
					}
				}
			}else{
				String nd = value.substring(val.indexOf("{")+1,val.lastIndexOf("}"));
				String[] nds = nd.split(",");
				for(int i = 0; i < nds.length; i++){
					int col = nds[i].indexOf(":");
					String name = nds[i].substring(0, col);
					String val = nds[i].substring(col+1);
					this.properties.put(name, new AttributeValue(this.df, val));
				}
			}
		}
		this.df.writeFile();
	}
	
	public String toString(){
		return this.val;
	}
}
