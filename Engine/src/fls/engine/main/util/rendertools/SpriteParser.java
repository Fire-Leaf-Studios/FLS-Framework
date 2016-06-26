package fls.engine.main.util.rendertools;

public class SpriteParser {

	
	private int[][] data;
	
	private int w;
	
	public SpriteParser(int s, String data){
		this.w = s;
		String[] lines = data.split("\n");
		int compressionType = -1;
		for(String line : lines){
			if(line.startsWith("?") && compressionType == -1) {
				int t = CompressionManager.getVersion(line);
				if(t > -1){
					compressionType = t;
				}
			}
		}
		this.data = new int[(s * s) * (s * s)][];
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
				this.data[dataPos] = CompressionManager.superDecompress(lines[i]);
			}
			dataPos ++;
		}
	}
	
	public int[] getData(int x, int y){
		int[] res = new int[this.w * this.w];
		int tx = x;
		int ty = y;
		if(tx < 0 || ty < 0 || tx >= this.w  || ty >= this.w)return new int[8*8];
		for(int i = 0; i < this.w * this.w; i++){
			int dx = i % this.w;
			int dy = i / this.w;
			res[dx + dy * this.w] = this.data[tx + ty * this.w][i];
		}
		return res;
	}
}
