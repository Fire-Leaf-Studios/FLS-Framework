package fls.engine.main.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

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
	
	public boolean createFile(String path){
		File f = new File(path);
		if(f.exists())return false;
		else{
			String folderPath = path.substring(0,path.lastIndexOf("/"));
			new File(folderPath).mkdirs();
			return true;
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
	
	public boolean deleteDir(String path){
		File f = new File(path);
		boolean res = f.delete();
		if(res){
			System.out.println("Deleted: " + path);
			return true;
		}else{
			System.out.println("Unable to delete: " + path);
			System.out.println("Checking for files in folders");
			String[] files = listFiles(path);
			if(files.length == 0){
				System.out.println("No files, just unable to delete");
			}else{
				System.out.println("Found some files, going to remove them");
				for(String s : files){
					String fpath = path+"/"+s;
					File tf = new File(fpath);
					tf.delete();
				}
			}
			
			res = f.delete();
			
			if(res){
				System.out.println("Finally deleted the path");
				return true;
			}else{
				System.out.println("Still unable to delete this path, check it out");
			}
			return false;
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
	
	public boolean doesFileExist(String pos){
		return new File(pos).exists();
	}
	
	public String createDir(String sdir){
		File dir = new File(sdir);
		if(!dir.exists())dir.mkdirs();
		return sdir;
	}
	
	/**
	 * Will read in the contents of the internal file
	 * @param pos
	 * @return String
	 */
	public String readInternalFile(String pos){
		if(!doesInternalFileExsist(pos)){
			System.err.println("Unable to open "+pos);
			return "";
		}
		
		String res = "";
		InputStream stream = this.getClass().getResourceAsStream(pos);
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
	
	private boolean doesInternalFileExsist(String pos){
		try{
			if(this.getClass().getResourceAsStream(pos) == null)return false;
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public String[] listFiles(String dir){
		File dirFolder = new File(dir);
		if(!dirFolder.exists())return null;
		if(dirFolder.isDirectory()){
			File[] files = dirFolder.listFiles();
			List<String> names = new ArrayList<String>();
			for(File f : files){
				if(f.isDirectory())continue;
				else {
					names.add(f.getName());
				}
			}
			String[] res = new String[names.size()];
			for(int i = 0; i < names.size(); i++){
				res[i] = names.get(i);
			}
			return res;
		}else{
			System.err.println(dir + " is not a directory or it does not exist");
			return null;
		}
	}
}
