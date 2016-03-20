package fls.engine.main.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

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
	
	/**
	 * Will read in the contents of the internal file
	 * @param pos
	 * @return String
	 */
	public String readInternalFile(String pos){
		String res = "";
		InputStream stream = this.getClass().getResourceAsStream(pos);
		
		if(stream == null){
			throw new RuntimeException("Unable to open: "+pos);
		}
		InputStreamReader is = new InputStreamReader(stream);
		try{
			BufferedReader reader = new BufferedReader(is);
			String line;
			while((line = reader.readLine()) != null){
				res += line+"\n";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
}
