package fls.engine.main.util.rendertools;

public class SpriteParser {

	
	private int[][] data;
	
	private int w;
	
	public SpriteParser(int s, String data){
		this(data);
		this.w = s;
	}
	
	public SpriteParser(String data){
		String[] lines = data.split("\n");
		int s = 8;
		for(String l : lines){
			if(!l.startsWith("?"))continue;
			l = l.substring(1);
			String key = l.substring(0, l.indexOf(":")).trim();
			String value = l.substring(l.indexOf(":")+1).trim();
			if(key.equals("Size")){
				s = Integer.parseInt(value);
			}
		}
		
		int compressionType = -1;
		for(String line : lines){
			if(line.startsWith("?") && compressionType == -1) {
				int t = CompressionManager.getVersion(line);
				if(t > -1){
					compressionType = t;
				}
			}
		}
		
		int ts = 64 / s;
		int numCells = (ts * ts);
		this.data = new int[numCells][];
		int dataPos = 0;
		for(int i = 0; i < lines.length; i++){
			String l = lines[i];
			if(!l.startsWith("#"))continue;
			l = l.substring(1);
			String[] nd = l.trim().split(",");
			int[] d = new int[s * s];
			if(compressionType < 3){
				for(int j = 0; j < nd.length; j++){
					d[j] = CompressionManager.decompress(nd[j]);
				}
				this.data[dataPos] = d;
			}else{
				this.data[dataPos] = CompressionManager.superDecompress(s, lines[i]);
			}
			dataPos ++;
		}
		
		this.w = s;
	}
	
	public int[] getData(int x, int y){
		int[] res = new int[this.w * this.w];
		int tx = x;
		int ty = y;
		if(tx < 0 || ty < 0 || tx >= this.w  || ty >= this.w)return res;
		for(int i = 0; i < this.w * this.w; i++){
			int dx = i % this.w;
			int dy = i / this.w;
			res[dx + dy * this.w] = this.data[tx + ty * getNumCells()][i];
		}
		return res;
	}
	
	public int[] getData(int i){
		int tx = i % this.getNumCells();
		int ty = i / this.getNumCells();
		return getData(tx, ty);
	}
	
	public int getNumCells(){
		return 64 / this.w;
	}
	
	public int getTotalNumCells(){
		return getNumCells() * getNumCells();
	}
	
	public int getCellWidth(){
		return this.w;
	}
}
