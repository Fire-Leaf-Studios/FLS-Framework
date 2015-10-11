package fls.	engine.main.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FileIO {

	public static FileIO instance = new FileIO();
	
	public static final String path = new File("").getAbsolutePath();
	
	public void writeFile(String pos, String... lines){
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter(new File(pos)));
			for(int i = 0; i < lines.length; i++){
				bw.write(lines[i]);
				bw.newLine();
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void appendFile(String pos, String... acont){
		if(!doesFileExist(pos)){
			writeFile(pos,"Nothing was here so I made this for you");
		}else{
			String prev = loadFile(pos);
			for(String s : acont){
				prev += s.trim() + "\n";
			}
			
			writeFile(pos,prev);
		}
	}
	
	public String loadFile(String pos){
		BufferedReader br = null;
		String res = "";
		try{
			br = new BufferedReader(new FileReader(new File(pos)));
			String c;
			while((c = br.readLine()) != null){
				res += c+"\n";
			}
			br.close();
			return res;
		}catch(Exception e){
			e.printStackTrace();
			return "It broke, sorry";
		}
	}
	
	protected boolean doesFileExist(String pos){
		return new File(pos).exists();
	}
	
	protected String createDir(String sdir){
		File dir = new File(sdir);
		if(!dir.exists())dir.mkdir();
		
		return sdir;
	}
}
