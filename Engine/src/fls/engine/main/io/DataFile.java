package fls.engine.main.io;

import java.util.HashMap;

public class DataFile extends FileIO {

	private String pos;
	private HashMap<String, AtributeValue> ats;

	public DataFile(String pos) {
		this.pos = "DataFiles/"+pos+".dat";
		this.ats = new HashMap<String, AtributeValue>();
		this.fillAts();
	}

	private void fillAts() {
		if(!this.doesFileExist(pos))this.writeFile(pos, "");
		String fileData = this.loadFile(this.pos);
		String[] chunks = fileData.split("\n");
		for (String cChunk : chunks) {
			cChunk = cChunk.trim();
			int colon = cChunk.indexOf(":");
			if(colon == -1)return;
			String key = cChunk.substring(0, colon).trim();
			String value = cChunk.substring(colon + 1, cChunk.length()).trim();
			AtributeValue val = new AtributeValue(value);
			this.ats.put(key, val);
		}
	}
	
	public void writeFile(){
		Object[] a = ats.keySet().toArray();
		String res = "";
		for(int i = 0; i < ats.size(); i++){
			String key = ""+a[i];
			String value = ats.get(key).getString();
			res += key+":"+value+"\n";
		}
		writeFile(pos, res);
	}
	
	@Override
	public void writeFile(String pos,String... lines){
		if(!doesFileExist(pos)){
			createDir("DataFiles");
		}
		super.writeFile(pos, lines);
	}
	
	public void setValue(String key, String val){
		if(this.ats.get(key) == null)this.ats.put(key, new AtributeValue(val));
		else this.ats.get(key).setValue(val);
		writeFile();
	}
	
	public void increaseValue(String key,int amt){
		if(this.ats.get(key) != null){
			int prev = this.ats.get(key).getInt();
			prev += amt;
			setValue(key,""+prev);
		}
	}
	
	public void decreaseValue(String key,int amt){
		this.increaseValue(key, -amt);
	}
	
	public AtributeValue getData(String key){
		return this.ats.get(key);
	}
}
