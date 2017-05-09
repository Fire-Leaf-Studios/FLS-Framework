package fls.engine.main.util.rendertools;

import java.util.Arrays;

public class CompressionManager {

	
	public static int decompress(String s){
		int res = 0;
		if(s.equals("-1")){
			res= -1;
		}else res = Integer.parseInt(s, 16);
		return res;
	}
	
	public static int[] superDecompress(int size, String s){
		if(s.startsWith("#")){
			int[] d = new int[size * size];
			Arrays.fill(d, -1);// Make sure it's all transparent at the start
			s = s.substring(1);
			String[] secs = s.split(",");
			int off = 0;// the possition in the array
			for(int j = 0; j < secs.length; j++){
				String c = secs[j].trim();
				boolean multi = c.indexOf(":") != -1;
				
				int amt = -1;
				int col = -1;
				if(multi){
					amt = Integer.parseInt(c.substring(0, c.indexOf(":")), 16);
					col = Integer.parseInt(c.substring(c.indexOf(":")+1), 16);
				}else{
					col = Integer.parseInt(c, 16);
					amt = 1;
				}

				//System.out.println(off + " : " + amt);
				for(int i = 0; i < amt; i++){
					d[off + i] = col;
				}
				
				off += amt;
			}
			return d;
		}else{
			return null;
		}
	}
	
	public static int getVersion(String l){
		if(l.startsWith("?")){
			l = l.substring(1);
			String data = l.substring(0, l.indexOf(":")).trim();
			String value = l.substring(l.indexOf(":")+1).trim();
			if(data.equals("Version")){
				return Integer.parseInt(value);
			}
		}
		
		return -1;
	}
}
