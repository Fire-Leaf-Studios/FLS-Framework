package fls.engine.main.io;

import java.util.Calendar;
import java.util.HashMap;

public class DataFile extends FileIO {

	private String pos;
	private HashMap<String, AttributeValue> ats;
	private boolean canSave;
	private String name;
	private String dir;

	public DataFile(String pos) {
		this(pos, true);
	}

	public DataFile(String pos, boolean usual) {
		if (usual) {
			this.pos = path + "/data/" + pos + ".dat";
		} else {
			this.pos = path + "/" + pos + ".dat";
		}
		this.ats = new HashMap<String, AttributeValue>();
		this.canSave = true;
		this.name = this.pos.substring(this.pos.lastIndexOf("/")+1, this.pos.lastIndexOf("."));
		this.dir = this.pos.substring(0, this.pos.lastIndexOf("/"));
		this.fillAts();
	}
	
	public DataFile(String file, String dir){
		this.pos = path + "/"+dir+"/"+file+".dat";
		this.ats = new HashMap<String, AttributeValue>();
		this.canSave = true;
		this.name = file;
		this.dir = this.pos.substring(0, this.pos.lastIndexOf("/"));
		this.fillAts();
	}
	
	public DataFile(String[] data){
		this.ats = new HashMap<String, AttributeValue>();
		this.fillAts(data);
	}

	private void fillAts() {
		if (!this.doesFileExist(pos)){
			int d = Calendar.getInstance().get(Calendar.DATE);
			int m = Calendar.getInstance().get(Calendar.MONTH);
			int y = Calendar.getInstance().get(Calendar.YEAR);
			this.writeFile(pos, "lastUsed:"+d+"/"+m+"/"+y);
		}
		String fileData = this.loadFile(this.pos);
		String[] chunks = fileData.split("\n");
		for (String cChunk : chunks) {
			cChunk = cChunk.trim();
			int colon = cChunk.indexOf(":");
			if (cChunk.startsWith("#") || colon == -1) continue; //Commented out or not valid
			String key = cChunk.substring(0, colon).trim();
			String value = cChunk.substring(colon + 1, cChunk.length()).trim();
			AttributeValue val = new AttributeValue(this, value);
			this.ats.put(key, val);
		}
	}
	
	private void fillAts(String[] data){
		for (String cChunk : data) {
			cChunk = cChunk.trim();
			int colon = cChunk.indexOf(":");
			if (cChunk.startsWith("#") || colon == -1)
				continue;
			String key = cChunk.substring(0, colon).trim();
			String value = cChunk.substring(colon + 1, cChunk.length()).trim();
			AttributeValue val = new AttributeValue(this, value);
			this.ats.put(key, val);
		}
	}

	public void writeFile() {
		if(!this.canSave)return;
		Object[] a = ats.keySet().toArray();
		String res = "";
		for (int i = 0; i < ats.size(); i++) {
			String key = "" + a[i];
			String value = ats.get(key).getString();
			res += key + ":" + value + "\n";
		}
		writeFile(pos, res);
	}

	@Override
	public void writeFile(String pos, String... lines) {
		if(!this.canSave){
			err("Can't save this file");
			return;
		}
		if (!doesFileExist(pos)) {
			log("No datafiles dir, probably a first run. Creating one");
			createDir(this.dir);
			log("Finished making datafiles folder");
			createFile(pos);
		}
		super.writeFile(pos, lines);
	}

	public void setValue(String key, String val) {
		if(!this.canSave)return;
		if (this.ats.get(key) == null)
			this.ats.put(key, new AttributeValue(this, val));
		else
			this.ats.get(key).setValue(val);
		writeFile();
	}

	public void increaseValue(String key, int amt) {
		if(!this.canSave)return;
		if (this.ats.get(key) != null) {
			int prev = this.ats.get(key).getInt();
			prev += amt;
			setValue(key, "" + prev);
		}
	}

	public void decreaseValue(String key, int amt) {
		this.increaseValue(key, -amt);
	}

	public AttributeValue getData(String key) {
		return this.ats.get(key);
	}
	
	public void log(String msg){
		System.out.println("[Data File: "+this.name+"] " + msg);
	}
	
	public void err(String msg){
		System.err.println("[Data File: "+this.name+"] " + msg);
	}
	
	public String getLastUsed(){
		if(this.getData("lastUsed") != null){
			return this.getData("lastUsed").getString();
		}	
		return "-1/-1/-1";
	}
}
